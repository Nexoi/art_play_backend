package com.seeu.apis.admin.record;

import com.seeu.artshow.record.model.UserRecord;
import com.seeu.artshow.record.service.RecordService;
import com.seeu.artshow.record.vo.RecordArrayItem;
import com.seeu.artshow.utils.DateFormatterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(tags = "Record", description = "+")
@RestController("adminRecordApi")
@RequestMapping("/api/admin/v1/record")
@PreAuthorize("hasRole('ADMIN')")
public class RecordApi {

    @Autowired
    private RecordService recordService;
    @Autowired
    private DateFormatterService dateFormatterService;

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

    @GetMapping("/users/{type}")
    public List<RecordArrayItem> users(@PathVariable UserRecord.TYPE type,
                                       @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date start,
                                       @RequestParam(required = true) @DateTimeFormat(pattern = "yyyyMMdd") Date end) {
        String startStr = dateFormatterService.getyyyyMMdd().format(start);
        String endStr = dateFormatterService.getyyyyMMdd().format(end);
        return recordService.findUser(type, Integer.parseInt(startStr), Integer.parseInt(endStr));
    }
}
