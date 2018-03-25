package com.seeu.apis.admin.record;

import com.seeu.artshow.exception.ResourceNotFoundException;
import com.seeu.artshow.record.model.UserRecord;
import com.seeu.artshow.record.service.RecordService;
import com.seeu.artshow.record.vo.RecordArrayItem;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import com.seeu.artshow.utils.DateFormatterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = "Record", description = "+")
@RestController("adminRecordApi")
@RequestMapping("/api/admin/v1/record")
@PreAuthorize("hasRole('ADMIN')")
public class RecordApi {

    @Autowired
    private RecordService recordService;
    @Autowired
    private DateFormatterService dateFormatterService;
    @Autowired
    private ShowService showService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;

    @GetMapping("/devices")
    public Map device(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                      @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        Map map = new HashMap();
        map.put("ars", recordService.findAr(Integer.parseInt(startStr), Integer.parseInt(endStr)));
        map.put("beacons", recordService.findBeacon(Integer.parseInt(startStr), Integer.parseInt(endStr)));
        map.put("qrcodes", recordService.findQRCode(Integer.parseInt(startStr), Integer.parseInt(endStr)));
        return map;
    }

    @GetMapping("/shows")
    public Map shows(@RequestParam Long[] showIds,
                     @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                     @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) throws ResourceNotFoundException {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        List<Long> ids = Arrays.asList(showIds);
        Map map = new HashMap();
        for (Long id : ids) {
            Show show = showService.findOne(id);
            map.put("title", show.getTitle());
            map.put("showId", show.getId());
            map.put("records", recordService.findShow(id, Integer.parseInt(startStr), Integer.parseInt(endStr)));
        }
        return map;
    }

    @GetMapping("/resource-groups")
    public Map resourcegroups(@RequestParam Long[] groupIds,
                              @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                              @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) throws ResourceNotFoundException {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        List<Long> ids = Arrays.asList(groupIds);
        Map map = new HashMap();
        for (Long id : ids) {
            ResourceGroup group = resourceGroupService.findOne(id);
            map.put("name", group.getName());
            map.put("groupId", group.getId());
            map.put("records", recordService.findResourceGroup(id, Integer.parseInt(startStr), Integer.parseInt(endStr)));
        }
        return map;
    }

    @GetMapping("/resource-items")
    public Map resources(@RequestParam Long[] itemIds,
                         @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                         @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) throws ResourceNotFoundException {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        List<Long> ids = Arrays.asList(itemIds);
        Map map = new HashMap();
        for (Long id : ids) {
            ResourceItem item = resourceItemService.findOne(id);
            map.put("name", item.getName());
            map.put("itemId", item.getId());
            map.put("records", recordService.findResource(id, Integer.parseInt(startStr), Integer.parseInt(endStr)));
        }
        return map;
    }

    @GetMapping("/users")
    public Map users(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                     @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        Map map = new HashMap();
        map.put("nick", recordService.findUser(UserRecord.TYPE.NICK, Integer.parseInt(startStr), Integer.parseInt(endStr)));
        map.put("registed", recordService.findUser(UserRecord.TYPE.REGISTED, Integer.parseInt(startStr), Integer.parseInt(endStr)));
        return map;
    }


    @GetMapping("/ar")
    public List<RecordArrayItem> ar(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                    @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findAr(Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/beacon")
    public List<RecordArrayItem> beacon(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findBeacon(Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/qrcode")
    public List<RecordArrayItem> qrcode(@RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                        @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findQRCode(Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/show/{showId}")
    public List<RecordArrayItem> show(@PathVariable Long showId,
                                      @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                      @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findShow(showId, Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/resource-group/{groupId}")
    public List<RecordArrayItem> resourcegroup(@PathVariable Long groupId,
                                               @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                               @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findResourceGroup(groupId, Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/resource-item/{itemId}")
    public List<RecordArrayItem> resource(@PathVariable Long itemId,
                                          @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                          @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findResource(itemId, Integer.parseInt(startStr), Integer.parseInt(endStr));
    }

    @GetMapping("/user/{type}")
    public List<RecordArrayItem> user(@PathVariable UserRecord.TYPE type,
                                      @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                      @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findUser(type, Integer.parseInt(startStr), Integer.parseInt(endStr));
    }
}
