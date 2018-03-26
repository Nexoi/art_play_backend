package com.seeu.apis.admin.record;

import com.seeu.artshow.record.vo.RecordResourceItem;
import com.seeu.artshow.show.model.ResourceGroup;
import com.seeu.artshow.show.model.ResourceItem;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ResourceGroupService;
import com.seeu.artshow.show.service.ResourceItemService;
import com.seeu.artshow.show.service.ShowService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "Record+", description = "+")
@RestController("adminRecordSupApi")
@RequestMapping("/api/admin/v1/record/sup")
@PreAuthorize("hasRole('ADMIN')")
public class RecordSupApi {
    @Autowired
    private ShowService showService;
    @Autowired
    private ResourceGroupService resourceGroupService;
    @Autowired
    private ResourceItemService resourceItemService;

    @GetMapping("/shows")
    public List<RecordResourceItem> listShows() {
        List<Show> shows = showService.findAll();
        if (shows.isEmpty()) return new ArrayList<>();
        List<RecordResourceItem> items = new ArrayList<>();
        for (Show show : shows) {
            if (show == null) continue;
            RecordResourceItem item = new RecordResourceItem();
            item.setId(show.getId());
            item.setTitle(show.getTitle());
            items.add(item);
        }
        return items;
    }

    @GetMapping("/{showId}/resource-groups")
    public List<RecordResourceItem> listGroups(@PathVariable Long showId) {
        List<ResourceGroup> groups = resourceGroupService.findAll(showId);
        if (groups.isEmpty()) return new ArrayList<>();
        List<RecordResourceItem> items = new ArrayList<>();
        for (ResourceGroup group : groups) {
            if (group == null) continue;
            RecordResourceItem item = new RecordResourceItem();
            item.setId(group.getId());
            item.setTitle(group.getName());
            items.add(item);
        }
        return items;
    }

    @GetMapping("/{showId}/resource-groups/{groupId}/items")
    public List<RecordResourceItem> listItems(@PathVariable Long groupId) {
        List<ResourceItem> resourceItems = resourceItemService.findAll(groupId);
        if (resourceItems.isEmpty()) return new ArrayList<>();
        List<RecordResourceItem> items = new ArrayList<>();
        for (ResourceItem resourceItem : resourceItems) {
            if (resourceItem == null) continue;
            RecordResourceItem item = new RecordResourceItem();
            item.setId(resourceItem.getId());
            item.setTitle(resourceItem.getName());
            items.add(item);
        }
        return items;
    }
}
