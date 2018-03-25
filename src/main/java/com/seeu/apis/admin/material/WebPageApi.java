package com.seeu.apis.admin.material;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Audio;
import com.seeu.artshow.material.model.Folder;
import com.seeu.artshow.material.model.WebPage;
import com.seeu.artshow.material.service.AudioService;
import com.seeu.artshow.material.service.FolderService;
import com.seeu.artshow.material.service.WebPageService;
import com.seeu.artshow.material.vo.AudioPageVO;
import com.seeu.artshow.material.vo.WebPageVO;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Api(tags = "WebPage", description = "+")
@RestController("adminWebPageApi")
@RequestMapping("/api/admin/v1/webpage")
@PreAuthorize("hasRole('ADMIN')")
public class WebPageApi {
    @Autowired
    private WebPageService webPageService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WebPage get(@PathVariable Long id) throws ResourceNotFoundException {
        return webPageService.findOne(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public WebPage changeName(@PathVariable Long id,
                              @RequestBody WebPageVO vo) throws ResourceNotFoundException {
        return webPageService.update(id, vo);
    }

}
