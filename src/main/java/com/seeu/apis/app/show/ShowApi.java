package com.seeu.apis.app.show;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@Api(tags = "展览信息", description = "展览列表／资源组信息")
@RestController
@RequestMapping("/api/v1/shows")
public class ShowApi {
    @Autowired
    private ShowService showService;


    @ApiOperation("封面展览列表（默认10条）")
    @GetMapping("/covers")
    public Page<Show> listCover10(@RequestParam(defaultValue = "10") Integer size) {
        PageRequest request = new PageRequest(0, size);
        return showService.findAll(request);
    }

    @ApiOperation("搜索展览列表")
    @GetMapping("/search")
    public Page<Show> list(@RequestParam(required = false) String word,
                           @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "10") Integer size) {
        PageRequest request = new PageRequest(page, size);
        if (word == null)
            return showService.findAll(request);
        else
            return showService.searchAll(word, request);
    }

    @ApiOperation("查看某一个展览信息")
    @GetMapping("/{showId}")
    public Show get(@PathVariable Long showId) throws ResourceNotFoundException {
        return showService.findOne(showId);
    }

    @ApiOperation("查看某一个展览下的所有资源组")
    @GetMapping("/{showId}/resources")
    public Page<ResourceGroup> getGroup(@PathVariable Long showId,
                                        @RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        return null;
    }

}
