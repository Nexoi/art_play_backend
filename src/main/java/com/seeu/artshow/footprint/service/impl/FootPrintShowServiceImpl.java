package com.seeu.artshow.footprint.service.impl;

import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.footprint.model.FootPrintShowPKeys;
import com.seeu.artshow.footprint.repository.FootPrintShowRepository;
import com.seeu.artshow.footprint.service.FootPrintShowService;
import com.seeu.artshow.show.model.Show;
import com.seeu.artshow.show.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FootPrintShowServiceImpl implements FootPrintShowService {
    @Resource
    private FootPrintShowRepository repository;
    @Autowired
    private ShowService showService;

    @Override
    public Page<FootPrintShow> findAll(Long uid, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page result = repository.findAllByUid(uid, request);
        List<FootPrintShow> footPrintList = result.getContent();
        List<Long> showIds = footPrintList.parallelStream().map(FootPrintShow::getShowId).collect(Collectors.toList());
        List<Show> shows = showService.findAll(showIds);
        // 填充数据
        Map<Long, Show> map = new HashMap();
        for (Show show : shows) {
            map.put(show.getId(), show);
        }
        for (FootPrintShow footPrint : footPrintList) {
            footPrint.setShow(map.get(footPrint.getShowId()));
        }
        return result;
    }

    @Override
    public FootPrintShow setFootPrint(Long uid, Show show) {
        if (show == null || show.getId() == null) return null;
        FootPrintShow footPrint = repository.findOne(new FootPrintShowPKeys(uid, show.getId()));
        if (footPrint == null) {
            footPrint = new FootPrintShow();
            footPrint.setUid(uid);
            footPrint.setShow(show);
            footPrint.setShowId(show.getId());
            footPrint.setViewTimes(0L);
        }
        footPrint.setViewTimes(footPrint.getViewTimes() + 1); // 浏览次数 +1
        footPrint.setUpdateTime(new Date());
        return repository.save(footPrint);
    }
}
