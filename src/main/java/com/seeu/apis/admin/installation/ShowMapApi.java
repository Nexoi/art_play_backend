package com.seeu.apis.admin.installation;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.artshow.material.model.Image;
import com.seeu.artshow.material.service.ImageService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "地图", description = "CRUD")
@RestController("adminShowMapApi")
@RequestMapping("/api/admin/v1/show/{showId}/maps")
@PreAuthorize("hasRole('ADMIN')")
public class ShowMapApi {

    @Autowired
    private ShowMapService showMapService;
    @Autowired
    private ImageService imageService;

    @GetMapping
    public Page<ShowMap> list(@PathVariable Long showId,
                              @RequestParam(defaultValue = "0") Integer page,
                              @RequestParam(defaultValue = "10") Integer size) {
        if (showId <= 0){
            // 返回所有的地图信息
            return showMapService.findAll(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "updateTime")));
        }
        return showMapService.findAll(showId, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "updateTime")));
    }

    @GetMapping("/{mapId}")
    public ShowMap get(@PathVariable Long showId,
                       @PathVariable Long mapId) throws ResourceNotFoundException {
        return showMapService.findOne(mapId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShowMap add(@PathVariable Long showId,
                       String name,
                       String showHallName,
                       Integer floor,
                       Integer width,
                       Integer height,
                       Long imageId) throws ResourceNotFoundException, ActionParameterException {
        Image image = imageService.findOne(imageId);
        ShowMap map = new ShowMap();
        map.setShowId(showId);
        map.setFloor(floor);
        map.setImage(image);
        map.setName(name);
        map.setShowHallName(showHallName);
        map.setWidth(width);
        map.setHeight(height);
        return showMapService.add(map);
    }

    @PutMapping("/{mapId}")
    public ShowMap update(@PathVariable Long showId,
                          @PathVariable Long mapId,
                          @RequestParam(required = false) Integer floor,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String showHallName,
                          @RequestParam(required = false) Integer width,
                          @RequestParam(required = false) Integer height,
                          @RequestParam(required = false) Long imageId) throws ResourceNotFoundException, ActionParameterException {
        ShowMap map = new ShowMap();
        map.setShowId(showId);
        map.setId(mapId);
        map.setFloor(floor);
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
