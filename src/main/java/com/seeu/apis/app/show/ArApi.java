package com.seeu.apis.app.show;

import com.seeu.artshow.ar.service.ArService;
import com.seeu.artshow.exception.ResourceNotFoundException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "展览 AR", description = "压缩包、文件清单")
@Controller
@RequestMapping("/api/v1/show/{showId}/ar")
public class ArApi {
    @Autowired
    private ArService arService;

    @GetMapping("/file.zip")
    public Resource get(@PathVariable Long showId) {
        return arService.loadFile(showId);
    }

    @GetMapping("/config")
    @ResponseBody
    public List<ArService.ArConfig> list(@PathVariable Long showId) throws ResourceNotFoundException {
        return arService.loadConfig(showId);
    }
}
