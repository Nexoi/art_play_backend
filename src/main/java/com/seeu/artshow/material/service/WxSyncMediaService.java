package com.seeu.artshow.material.service;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;

public interface WxSyncMediaService {

//
//    WxSyncMedia getImage(String artUrl); // 因为图片素材无 media_id，所以单独封装出来

    WxSyncMedia getMedia(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException;
}
