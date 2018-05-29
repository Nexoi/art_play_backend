package com.seeu.artshow.material.service.impl;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.service.WebPageService;
import com.seeu.artshow.material.service.WxSyncMediaService;
import com.seeu.artshow.material.service.WxSyncShowService;
import com.seeu.artshow.material.vo.WxSyncItem;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WxSyncShowServiceImpl implements WxSyncShowService {
    @Autowired
    private ShowService showService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;
    @Autowired
    private WebPageService webPageService;
    @Autowired
    private WxSyncMediaService wxSyncMediaService;

    private static Hashtable<Long, List<WxSyncItem>> asyncResults = new Hashtable<>();

    @Override
    public List<WxSyncItem> listAllItems(Long showId) throws ResourceNotFoundException {
//        Show show = showService.findOne(showId);
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
        List<Long> groupIds = groups.parallelStream().map(ResourceGroup::getId).collect(Collectors.toList());
        List<ResourceItem> items = resourceItemService.findAll(groupIds);
        List<Long> itemIds = items.parallelStream().filter(it -> {
            return ResourceItem.TYPE.WEB == it.getType();
        }).map(ResourceItem::getId).collect(Collectors.toList());
        List<WebPage> webPages = webPageService.findAll(itemIds);

        List<WxSyncItem> wxSyncItems = new ArrayList<>();
        for (ResourceGroup group : groups) {
            List<ResourceItem> resourceItems = group.getResourceItems();
            if (!resourceItems.isEmpty())
                for (ResourceItem item : resourceItems) {
                    if (ResourceItem.TYPE.WEB != item.getType()) continue;
                    WxSyncItem wxSyncItem = new WxSyncItem();
                    wxSyncItem.setItemId(item.getId());
                    wxSyncItem.setItemName(group.getName() + "/" + item.getName());
                    wxSyncItems.add(wxSyncItem);
                }
        }
        Map<Long, WebPage.WECHAT_ASYNC> wxAsyncStatus = webPages.parallelStream().collect(Collectors.toMap(WebPage::getResourceItemId, WebPage::getWechatAsync));
        for (WxSyncItem item : wxSyncItems) {
            if (null == item) continue;
            Long itemId = item.getItemId();
            WebPage.WECHAT_ASYNC wechat_async = wxAsyncStatus.get(itemId);
            if (null == wechat_async) wechat_async = WebPage.WECHAT_ASYNC.no;
            item.setType(wechat_async == WebPage.WECHAT_ASYNC.yes ? WxSyncItem.TYPE.UPDATE : WxSyncItem.TYPE.ADD);
        }
        return wxSyncItems;
    }

    @Override
    public boolean syncShowResourceItem(Long webItemId) throws ResourceNotFoundException, ActionParameterException, WxErrorException {
        WebPage page = webPageService.findOne(webItemId);
        WebPage.WECHAT_ASYNC async = page.getWechatAsync();
        if (null == async || WebPage.WECHAT_ASYNC.no == async || null == page.getMediaId()) {
            // 新增
            String mediaId = wxSyncMediaService.syncHtml(
                    page.getTitle(),
                    page.getCoverImageUrl(),
                    page.getAuthor(),
                    page.getIntroduce(),
                    true,
                    fillContentStyle(page.getContentHtml()),
                    page.getArtUrl()
            );
            if (null != mediaId) {
                page.setMediaId(mediaId);
                webPageService.saveWithoutValid(page);
                return true;
            } else {
                return false;
            }
        } else {
            // 更新
            boolean result = wxSyncMediaService.syncUpdateHtml(
                    page.getMediaId(),
                    page.getTitle(),
                    page.getCoverImageUrl(),
                    page.getAuthor(),
                    page.getIntroduce(),
                    true,
                    fillContentStyle(page.getContentHtml()),
                    page.getArtUrl()
            );
            return result;
        }
    }

    private String fillContentStyle(String contentHtml) {
        return "<div>" +
                "<style>.image-wrap{text-align:center;} video, audio, img{max-width: 100%}</style>" +
                contentHtml +
                "</div>";
    }

    // 异步
    @Override
    public void asyncShow2Wx(Long showId) throws ResourceNotFoundException {
        // 重新执行该方法会将原来的数据清空
        // 准备任务清单
        List<WxSyncItem> list = listAllItems(showId);
        if (list.isEmpty()) return;
        for (WxSyncItem item : list) {
            if (null == item) continue;
            item.setStatus(WxSyncItem.STATUS.ING);
        }
        asyncResults.put(showId, list);
        // 开始执行任务
        for (WxSyncItem item : list) {
            asyncShowResourceItem(showId, item.getItemId(), item);
        }
        // end
    }

    @Override
    public List<WxSyncItem> listResultOfAsyncShow(Long showId) {
        List list = asyncResults.get(showId);
        if (null == list || list.isEmpty()) return new ArrayList<>();
        return list;
    }

    /**
     * @param showId
     * @param webItemId
     * @param wxSyncItem 接受结果的容器
     */
    @Override
    public void asyncShowResourceItem(Long showId, Long webItemId, WxSyncItem wxSyncItem) {
        //... 同步
        try {
//            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH); // MDZZ!!!! 所有入队列开始同步的，因为都设为 FINISH！总有异常抓不住！
            WebPage page = webPageService.findOne(webItemId);
            WebPage.WECHAT_ASYNC async = page.getWechatAsync();
            if (null == async || WebPage.WECHAT_ASYNC.no == async || null == page.getMediaId()) {
                // 新增
                String mediaId = wxSyncMediaService.syncHtml(
                        page.getTitle(),
                        page.getCoverImageUrl(),
                        page.getAuthor(),
                        page.getIntroduce(),
                        false,
                        page.getContentHtml(),
                        page.getArtUrl()
                );
                if (null != mediaId) {
                    page.setMediaId(mediaId);
                    page.setWechatAsync(WebPage.WECHAT_ASYNC.yes);
                    webPageService.saveWithoutValid(page); // 持久化
                    wxSyncItem.setMessage("文章增添成功");
                } else {
                    wxSyncItem.setMessage("文章增添失败");
                }
            } else {
                // 更新
                boolean result = wxSyncMediaService.syncUpdateHtml(
                        page.getMediaId(),
                        page.getTitle(),
                        page.getCoverImageUrl(),
                        page.getAuthor(),
                        page.getIntroduce(),
                        false,
                        page.getContentHtml(),
                        page.getArtUrl()
                );
                if (result)
                    wxSyncItem.setMessage("文章更新成功");
                else
                    wxSyncItem.setMessage("文章更新失败");
            }
            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH);
        } catch (ResourceNotFoundException e) {
            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH);
            wxSyncItem.setMessage(e.getMessage());
        } catch (ActionParameterException e) {
            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH);
            wxSyncItem.setMessage(e.getMessage());
        } catch (WxErrorException e) {
            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH);
            wxSyncItem.setMessage(e.getMessage());
        } catch (Exception e) {
            wxSyncItem.setStatus(WxSyncItem.STATUS.FINISH);
            wxSyncItem.setMessage(e.getMessage());
        }
    }
}
