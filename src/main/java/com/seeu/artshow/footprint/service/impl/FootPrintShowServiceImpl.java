package com.seeu.artshow.footprint.service.impl;

import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.footprint.model.FootPrintShowPKeys;
import com.seeu.artshow.footprint.repository.FootPrintShowRepository;
import com.seeu.artshow.footprint.service.FootPrintShowService;
import com.seeu.artshow.show.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class FootPrintShowServiceImpl implements FootPrintShowService {
    @Resource
    private FootPrintShowRepository repository;

    @Override
    public Page<FootPrintShow> findAll(Long uid, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        PageRequest request = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return repository.findAllByUid(uid, request);
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
