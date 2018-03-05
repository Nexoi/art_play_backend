package com.seeu.apis.admin.installation;

import com.seeu.artshow.exception.ActionParameterException;
import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.installation.model.Beacon;
import com.seeu.artshow.installation.model.ShowMap;
import com.seeu.artshow.installation.service.BeaconService;
import com.seeu.artshow.installation.service.ShowMapService;
import com.seeu.core.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController("adminBeaconApi")
@RequestMapping("/api/admin/v1/beacons")
public class BeaconApi {
    @Autowired
    private BeaconService beaconService;
    @Autowired
    private ShowMapService showMapService;

    @GetMapping
    public Page<Beacon> list(@RequestParam(defaultValue = "0") Integer page,
                             @RequestParam(defaultValue = "10") Integer size) {
        return beaconService.findAll(new PageRequest(page, size));
    }

    @GetMapping("/{uuid}")
    public Beacon get(@PathVariable String uuid) throws ResourceNotFoundException {
        return beaconService.findOne(uuid);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beacon add(@RequestParam(required = true) String name,
                      @RequestParam(required = true) String uuid,
                      @RequestParam(required = true) String majorValue,
                      @RequestParam(required = true) String minorValue,
                      @RequestParam(required = true) Beacon.RANGE availableRange,
                      @RequestParam(required = false) Beacon.STATUS status,
                      @RequestParam(required = false) Long mapId,
                      @RequestParam(required = false) Integer width,
                      @RequestParam(required = false) Integer height) throws ResourceNotFoundException, ActionParameterException {
        Beacon beacon = new Beacon();
        beacon.setName(name);
        beacon.setUuid(uuid);
        beacon.setMajorValue(majorValue);
        beacon.setMinorValue(minorValue);
        beacon.setAvailableRange(availableRange);
        beacon.setStatus(status);
        beacon.setPositionWidth(width);
        beacon.setPositionHeight(height);
        if (mapId != null)
            beacon.setShowMap(showMapService.findOne(mapId));

        return beaconService.add(beacon);
    }

    @PutMapping("/{uuid}")
    public Beacon update(@PathVariable(required = true) String uuid,
                         @RequestParam(required = false) String name,
                         @RequestParam(required = false) String majorValue,
                         @RequestParam(required = false) String minorValue,
                         @RequestParam(required = false) Beacon.RANGE availableRange,
                         @RequestParam(required = false) Beacon.STATUS status,
                         @RequestParam(required = false) Long mapId,
                         @RequestParam(required = false) Integer width,
                         @RequestParam(required = false) Integer height) throws ResourceNotFoundException, ActionParameterException {

        Beacon beacon = new Beacon();
        beacon.setName(name);
        beacon.setUuid(uuid);
        beacon.setMajorValue(majorValue);
        beacon.setMinorValue(minorValue);
        beacon.setAvailableRange(availableRange);
        beacon.setStatus(status);
        beacon.setPositionWidth(width);
        beacon.setPositionHeight(height);
        if (mapId != null)
            beacon.setShowMap(showMapService.findOne(mapId));
        return beaconService.update(beacon);
    }

    @PutMapping("/{uuid}/change-status")
    public Beacon changeStatus(@PathVariable String uuid) throws ResourceNotFoundException {
        return beaconService.changeStatus(uuid);
    }

    @DeleteMapping("/{uuid}")
    public R.ResponseR delete(@PathVariable String uuid) {
        beaconService.delete(uuid);
        return R.deleteSuccess();
    }

}
