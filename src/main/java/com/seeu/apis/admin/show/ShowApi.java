package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "展览", description = "基础信息")
@RestController("adminShowApi")
@RequestMapping("/api/admin/v1/show")
@PreAuthorize("hasRole('ADMIN')")
public class ShowApi {
    @Autowired
    private ShowService showService;

    // 过滤展览信息 by 管理员权限
    @GetMapping("/search")
    public Page<Show> list(@RequestParam(required = false) String word,
                           @RequestParam(defaultValue = "0") Integer page,
                           @RequestParam(defaultValue = "10") Integer size) {
        if (word == null || word.isEmpty())
            return showService.findAll(new PageRequest(page, size));
        else
            return showService.searchAll(word, new PageRequest(page, size));
    }

    @GetMapping("/{showId}")
    public Show get(@PathVariable Long showId) throws ResourceNotFoundException {
        return showService.findOne(showId);
    }

    // 将创建展览的管理员加入权限清单
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Show add(String title,
                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                    @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                    String introduceText,
                    Integer imageHeight,
                    Integer imageWidth,
                    String imageUrl,
                    String imageThumbUrl) throws ResourceNotFoundException, ActionParameterException {
        Show show = new Show();
        show.setTitle(title);
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        show.setIntroduceText(introduceText);
        Image image = new Image();
        image.setName(title + "海报");
        image.setUrl(imageUrl);
        image.setThumbUrl(imageThumbUrl);
        image.setWidth(imageWidth);
        image.setHeight(imageHeight);
        return showService.add(show, image);
    }

    @PutMapping("/{showId}")
    public Show update(@PathVariable Long showId,
                       String title,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                       @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                       String introduceText,
                       Integer imageHeight,
                       Integer imageWidth,
                       String imageUrl,
                       String imageThumbUrl) throws ResourceNotFoundException, ActionParameterException {
        Show show = new Show();
        show.setId(showId);
        show.setTitle(title);
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        show.setIntroduceText(introduceText);
        if (0 != imageHeight && 0 != imageWidth) {
            Image image = new Image();
            image.setName(title + "海报");
            image.setUrl(imageUrl);
            image.setThumbUrl(imageThumbUrl);
            image.setWidth(imageWidth);
            image.setHeight(imageHeight);
            return showService.update(show, image);
        } else
            return showService.update(show, null);
    }

    @DeleteMapping("/{showId}")
    public R.ResponseR delete(@PathVariable Long showId) throws ResourceNotFoundException {
        showService.delete(showId);
        return R.deleteSuccess();
    }

}
