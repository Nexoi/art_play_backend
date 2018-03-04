package com.seeu.artshow.footprint.service;

import com.seeu.artshow.footprint.model.FootPrintShow;
import com.seeu.artshow.show.model.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FootPrintShowService {
    Page<FootPrintShow> findAll(Long uid, Pageable pageable); // 时间倒序

    FootPrintShow setFootPrint(Long uid, Show show);
}
