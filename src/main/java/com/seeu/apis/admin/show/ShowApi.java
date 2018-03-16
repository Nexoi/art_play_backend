package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Api(tags = "展览", description = "基础信息")
@RestController("adminShowApi")
@RequestMapping("/api/admin/v1/show")
public class ShowApi {
    @Autowired
    private ShowService showService;
    @Autowired
    private ImageService imageService;

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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Show add(String title,
                    String showHallName,
                    @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                    @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime,
                    Long posterImageId,
                    String introduceText) throws ResourceNotFoundException, ActionParameterException {
        Show show = new Show();
        show.setTitle(title);
        show.setShowHallName(showHallName);
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        show.setIntroduceText(introduceText);
        if (posterImageId != null)
            show.setPosterImage(imageService.findOne(posterImageId));
        return showService.add(show);
    }

    @PutMapping("/{showId}")
    public Show update(@PathVariable Long showId,
                       String title,
                       String showHallName,
                       @DateTimeFormat(pattern="yyyy-MM-dd") Date startTime,
                       @DateTimeFormat(pattern="yyyy-MM-dd") Date endTime,
                       Long posterImageId,
                       String introduceText) throws ResourceNotFoundException, ActionParameterException {
        Show show = new Show();
        show.setId(showId);
        show.setTitle(title);
        show.setShowHallName(showHallName);
        show.setStartTime(startTime);
        show.setEndTime(endTime);
        show.setIntroduceText(introduceText);
        if (posterImageId != null)
            show.setPosterImage(imageService.findOne(posterImageId));
        return showService.update(show);
    }

    @DeleteMapping("/{showId}")
    public R.ResponseR delete(@PathVariable Long showId) {
        showService.delete(showId);
        return R.deleteSuccess();
    }

}
