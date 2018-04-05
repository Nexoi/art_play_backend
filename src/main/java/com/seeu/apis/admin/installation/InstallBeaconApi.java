package com.seeu.apis.admin.installation;

import com.seeu.artshow.installation.model.InstallBeacon;
import com.seeu.artshow.installation.service.InstallBeaconService;
import com.seeu.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;


@Api(tags = "BeaconInstall", description = "CRUD AdminX OneToMany")
@RestController("adminInstallBeaconApi")
@RequestMapping("/api/admin/v1/install-beacons")
@PreAuthorize("hasRole('ADMINX')")
public class InstallBeaconApi {
    @Autowired
    private InstallBeaconService installBeaconService;

    @GetMapping
    public Page<InstallBeacon> list(@RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        return installBeaconService.findAll(new PageRequest(page, size, new Sort(Sort.Direction.DESC, "id")));
    }

    @PostMapping
    public InstallBeacon add(String uuid,
                             Integer majorValue,
                             Integer minorValue) {
        InstallBeacon beacon = new InstallBeacon();
        beacon.setUuid(uuid);
        beacon.setMajorValue("" + majorValue);
        beacon.setMinorValue("" + minorValue);
        return installBeaconService.add(beacon);
    }

    @PutMapping("/{id}")
    public InstallBeacon update(@PathVariable Long id,
                                String uuid,
                                Integer majorValue,
                                Integer minorValue) {
        InstallBeacon beacon = new InstallBeacon();
        beacon.setId(id);
        beacon.setUuid(uuid);
        beacon.setMajorValue("" + majorValue);
        beacon.setMinorValue("" + minorValue);
        return installBeaconService.update(id, beacon);
    }

    @DeleteMapping("/{id}")
    public R.ResponseR delete(@PathVariable Long id) {
        installBeaconService.delete(id);
        return R.deleteSuccess();
    }

    @PostMapping("/append/{showId}")
    public R.ResponseR dispatch(@PathVariable Long showId,
                                @RequestParam(required = true) Long[] beaconIds) {
        if (null == beaconIds || beaconIds.length == 0)
            return R.code(400).message("请选择 beacon 再进行添加");
        installBeaconService.dispatch(showId, Arrays.asList(beaconIds));
        return R.code(200).message("添加成功");
    }

    @DeleteMapping("/remove/{showId}")
    public R.ResponseR remove(@PathVariable Long showId,
                              @RequestParam(required = true) Long beaconId) {
        installBeaconService.remove(showId, beaconId);
        return R.code(200).message("移除成功");
    }
}
