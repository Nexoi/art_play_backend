package com.seeu.apis.admin.export;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.material.model.WxSyncMedia;
import com.seeu.artshow.material.service.WxSyncMediaService;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping
public class WxSyncApi {

    @Autowired
    private WxSyncMediaService wxSyncMediaService;


    @Value("${wx.token}")
    private String token;

    @GetMapping("/wx")
    public String valiatedWx(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
        response.addHeader("NOT_TRANSFORM_BODY","true");
        if (SignUtil.checkSignature(token, signature, timestamp, nonce)) {
            return echostr;
        } else {
            return null;
        }
    }

    @GetMapping
    public String sync(String artUrl, WxSyncMedia.TYPE type, String videoTitle) throws ActionParameterException {
        wxSyncMediaService.testAll(artUrl, type, videoTitle);
        return "doing";
    }

    @GetMapping("/api/admin/v1/wx/token")
    public String token() throws ActionParameterException {
        return wxSyncMediaService.getToken();
    }

    @PostMapping("/api/admin/v1/wx/test")
    public String syncHtml(String title, String coverImageUrl, String author, String description, boolean showCoverImg, String originalSrcUrl,
                           String contentHtml) throws ActionParameterException, WxErrorException {
        return wxSyncMediaService.syncHtml(title, coverImageUrl, author, description, showCoverImg, contentHtml, originalSrcUrl);
    }
}
//<p></p><p></p><div class="media-wrap video-wrap"><video controls="" src="http://p2l82fhwg.bkt.clouddn.com/artshowf0973694-bf64-42fa-83d1-c99a328e9da1"></video></div><p></p><p>请在这里输入正文</p><div class="media-wrap image-wrap"><img src="http://p2l82fhwg.bkt.clouddn.com/artshowc199e253-1109-44d1-8198-f8038f6359bc"/></div><p></p><div class="media-wrap audio-wrap"><audio controls="" src="http://p2l82fhwg.bkt.clouddn.com/artshow47b2f221-e1f5-42cd-83ce-d82cc08f4d96"></audio></div><p></p>