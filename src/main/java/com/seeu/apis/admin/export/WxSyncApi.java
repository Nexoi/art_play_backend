package com.seeu.apis.admin.export;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import com.seeu.artshow.material.service.WxSyncMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/v1/wx")
public class WxSyncApi {

    @Autowired
    private WxSyncMediaService wxSyncMediaService;

    @GetMapping
    public Map sync(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException {
        return wxSyncMediaService.testAll(artUrl, type, videoTitle);
    }
}
