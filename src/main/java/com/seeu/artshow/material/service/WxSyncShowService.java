package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.vo.WxSyncItem;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface WxSyncShowService {

    // 机制 1
    /**
     * 下载所有需要同步的网页信息
     *
     * @param showId
     * @return
     */
    List<WxSyncItem> listAllItems(Long showId) throws ResourceNotFoundException;

    boolean syncShowResourceItem(Long webItemId) throws ResourceNotFoundException, ActionParameterException, WxErrorException;

    // 机制 2
    @Async
    void asyncShow2Wx(Long showId) throws ResourceNotFoundException; // by showId async

    void asyncShowResourceItem(Long showId, Long webItemId, WxSyncItem wxSyncItem);

    List<WxSyncItem> listResultOfAsyncShow(Long showId);
}
