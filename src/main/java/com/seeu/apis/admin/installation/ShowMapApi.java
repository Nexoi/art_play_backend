package com.seeu.apis.admin.installation;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.core.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("adminShowMapApi")
@RequestMapping("/api/admin/v1/show/maps")
public class ShowMapApi {

    @Autowired
    private ShowMapService showMapService;
    @Autowired
    private ImageService imageService;

    @GetMapping
    public Page<ShowMap> list(@RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        return showMapService.findAll(new PageRequest(page, size));
    }

    @GetMapping("/{mapId}")
    public ShowMap get(@PathVariable Long mapId) throws ResourceNotFoundException {
        return showMapService.findOne(mapId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowMap add(String name,
                       String showHallName,
                       Integer width,
                       Integer height,
                       Long imageId) throws ResourceNotFoundException, ActionParameterException {
        Image image = imageService.findOne(imageId);
        ShowMap map = new ShowMap();
        map.setImage(image);
        map.setName(name);
        map.setShowHallName(showHallName);
        map.setWidth(width);
        map.setHeight(height);
        return showMapService.add(map);
    }

    @PutMapping("/{mapId}")
    public ShowMap update(@PathVariable Long mapId,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String showHallName,
                          @RequestParam(required = false) Integer width,
                          @RequestParam(required = false) Integer height,
                          @RequestParam(required = false) Long imageId) throws ResourceNotFoundException, ActionParameterException {
        ShowMap map = new ShowMap();
        map.setId(mapId);
        map.setName(name);
        map.setShowHallName(showHallName);
        map.setWidth(width);
        map.setHeight(height);
        if (imageId != null)
            map.setImage(imageService.findOne(imageId));
        return showMapService.update(map);
    }

    @DeleteMapping("/{mapId}")
    public R.ResponseR delete(@PathVariable Long mapId) {
        showMapService.delete(mapId);
        return R.deleteSuccess();
    }

}
