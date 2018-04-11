package com.seeu.apis.admin.show;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.show.model.Beacon;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.service.BeaconService;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 普通管理员操作，在某展览下的 beacon
 *
 * update 2018-04-11 修改 uuid 为 id 标识，避免重复 beacon uuid 出现 bug
 */
@Api(tags = "Beacon", description = "CRUD Admin OneToMany")
@RestController("adminBeaconApi")
@RequestMapping("/api/admin/v1/show/{showId}/beacons")
@PreAuthorize("hasRole('ADMIN')")
public class BeaconApi {
    @Autowired
    private BeaconService beaconService;
    @Autowired
    private ShowMapService showMapService;

    @GetMapping
    public Page<Beacon> list(@PathVariable Long showId,
                             @RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return beaconService.findAllWithEmptyBeacons(showId, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "updateTime")));
    }

    @GetMapping("/{id}")
    public Beacon get(@PathVariable Long showId,
                      @PathVariable Long id) throws ResourceNotFoundException {
        return beaconService.findOne(showId, id); //TODO by showId and UUID
    }

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Beacon add(@RequestParam(required = true) String name,
//                      @RequestParam(required = true) Beacon.RANGE availableRange,
//                      @RequestParam(required = false) Beacon.STATUS status,
//                      @RequestParam(required = false) Long mapId,
//                      @RequestParam(required = false) Integer width,
//                      @RequestParam(required = false) Integer height) throws ResourceNotFoundException, ActionParameterException {
//        Beacon beacon = new Beacon();
//        beacon.setName(name);
////        beacon.setUuid(uuid);
////        beacon.setMajorValue(majorValue);
////        beacon.setMinorValue(minorValue);
//        beacon.setAvailableRange(availableRange);
//        beacon.setStatus(status);
//        beacon.setPositionWidth(width);
//        beacon.setPositionHeight(height);
//        if (mapId != null)
//            beacon.setShowMap(showMapService.findOne(mapId));
//
//        return beaconService.add(beacon);
//    }

    @PutMapping("/{id}")
    public Beacon update(@PathVariable Long showId,
                         @PathVariable Long id,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) Beacon.RANGE availableRange,
                         @RequestParam(required = false) Beacon.STATUS status,
                         @RequestParam(required = false) Long mapId,
                         @RequestParam(required = false) Integer width,
                         @RequestParam(required = false) Integer height) throws ResourceNotFoundException, ActionParameterException {
        Beacon beacon = new Beacon();
        beacon.setShowId(showId);
        beacon.setName(name);
//        beacon.setUuid(uuid);
//        beacon.setMajorValue(majorValue);
//        beacon.setMinorValue(minorValue);
        beacon.setAvailableRange(availableRange);
        beacon.setStatus(status);
        beacon.setPositionWidth(width);
        beacon.setPositionHeight(height);
        if (mapId != null)
            beacon.setShowMap(showMapService.findOne(mapId));
        return beaconService.update(showId, id, beacon);
    }

    @PutMapping("/{id}/change-status")
    public Beacon changeStatus(@PathVariable Long showId,
                               @PathVariable Long id) throws ResourceNotFoundException {
        return beaconService.changeStatus(showId, id);
    }

    // 把某一个 beacon 的绑定信息移除
    @DeleteMapping("/{id}/remove-beacon")
    public Beacon unbindBeacons(@PathVariable Long showId,
                                @PathVariable Long id) throws ResourceNotFoundException, ActionParameterException {
        return beaconService.removeBindInfo(showId, id);
    }
}
