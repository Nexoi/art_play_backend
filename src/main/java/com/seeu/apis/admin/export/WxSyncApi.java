package com.seeu.apis.admin.export;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.service.WxSyncMediaService;
import com.seeu.artshow.material.service.WxSyncShowService;
import com.seeu.artshow.material.vo.WxSyncItem;
import com.seeu.core.R;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RestController
@RequestMapping
public class WxSyncApi {

    @Autowired
    private WxSyncMediaService wxSyncMediaService;
    @Autowired
    private WxSyncShowService wxSyncShowService;


//    @Value("${wx.token}")
//    private String token;

    @GetMapping("/wx")
    public void valiatedWx(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
        response.addHeader("NOT_TRANSFORM_BODY", "true");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(echostr);
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
//            if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {
//                out.print(echostr);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // 获取 token
    @GetMapping("/api/admin/v1/wx/token")
    public String token() throws ActionParameterException, WxErrorException {
        return wxSyncMediaService.getToken();
    }

    @PostMapping("/api/admin/v1/wx/test")
    public String syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String originalSrcUrl,
                           String contentHtml) throws ActionParameterException, WxErrorException {
        return wxSyncMediaService.syncHtml(title, coverImageUrl, author, description, showCoverImg, contentHtml, originalSrcUrl);
    }

    // 机制 1：根据给出的清单一个个同步
    @GetMapping("/api/admin/v1/wx/sync/{showId}")
    public List<WxSyncItem> listAllItems(@PathVariable Long showId) throws ResourceNotFoundException {
        return wxSyncShowService.listAllItems(showId);
    }

    // 辅助实现机制 1
    @PostMapping("/api/admin/v1/wx/sync/{itemId}")
    public R.ResponseR syncWebPage(@PathVariable Long itemId) throws ActionParameterException, WxErrorException, ResourceNotFoundException {
        boolean result = wxSyncShowService.syncShowResourceItem(itemId);
        return result ? R.noCodeMessage("同步成功") : R.noCodeMessage("同步失败，请稍后再试");
    }

    // 机制 2：根据 showId 一次性后台同步完，单独接口给出同步结果
    @PostMapping("/api/admin/v1/wx/async/{showId}")
    @ResponseStatus(HttpStatus.CREATED)
    public R.ResponseR createAsyncTask(@PathVariable Long showId) throws ResourceNotFoundException {
        wxSyncShowService.asyncShow2Wx(showId);
        return R.code(201).message("同步任务创建成功！正在同步中...");
    }

    //  辅助实现机制 2，前端监测到所有的状态都 FINISH 的时候，需要停止刷新该接口
    @GetMapping("/api/admin/v1/wx/async/{showId}")
    public List<WxSyncItem> listResult(@PathVariable Long showId) {
        return wxSyncShowService.listResultOfAsyncShow(showId);
    }
}
