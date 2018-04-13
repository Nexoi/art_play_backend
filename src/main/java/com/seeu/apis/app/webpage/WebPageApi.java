package com.seeu.apis.app.webpage;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.service.WebPageService;
import com.seeu.artshow.material.vo.WebPageVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WebPage", hidden = true)
@RestController("WebPageApi")
@RequestMapping("/api/v1/webpage")
public class WebPageApi {
    @Autowired
    private WebPageService webPageService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WebPage get(@PathVariable Long id) throws ResourceNotFoundException {
        WebPage webPage = webPageService.findOne(id);
        webPageService.viewItOnce(webPage.getResourceItemId());
        webPage.setWechatAsync(null);
        webPage.setMediaId(null);
        return webPage;
    }
}
