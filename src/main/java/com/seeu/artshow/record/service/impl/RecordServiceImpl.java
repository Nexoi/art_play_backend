package com.seeu.artshow.record.service.impl;

import com.seeu.artshow.record.model.*;
import com.seeu.artshow.record.repository.*;
import com.seeu.artshow.record.service.RecordService;
import com.seeu.artshow.record.vo.RecordArrayItem;
import com.seeu.artshow.utils.DateFormatterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Resource
    private ArRecordRepository arRecordRepository;
    @Resource
    private BeaconRecordRepository beaconRecordRepository;
    @Resource
    private QRCodeRecordRepository qrCodeRecordRepository;
    @Resource
    private ResourceGroupRecordRepository resourceGroupRecordRepository;
    @Resource
    private ResourceRecordRepository resourceRecordRepository;
    @Resource
    private ShowRecordRepository showRecordRepository;
    @Resource
    private UserRecordCacheRepository userRecordCacheRepository;
    @Resource
    private UserRecordRepository userRecordRepository; // 每天更新一次时调用

    @Autowired
    private DateFormatterService dateFormatterService;

    private Integer today() {
        String today = dateFormatterService.getyyyyMMdd().format(new Date());
        return Integer.parseInt(today);
    }

    @Override
    public void recordAr() {
        Integer today = today();
        ArRecord record = arRecordRepository.findOne(today);
        if (null == record) {
            record = new ArRecord();
            record.setDay(today);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        arRecordRepository.save(record);
    }

    @Override
    public void recordBeacon() {
        Integer today = today();
        BeaconRecord record = beaconRecordRepository.findOne(today);
        if (null == record) {
            record = new BeaconRecord();
            record.setDay(today);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        beaconRecordRepository.save(record);

    }

    @Override
    public void recordQRCode() {
        Integer today = today();
        QRCodeRecord record = qrCodeRecordRepository.findOne(today);
        if (null == record) {
            record = new QRCodeRecord();
            record.setDay(today);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        qrCodeRecordRepository.save(record);

    }

    @Override
    public void recordResourceGroup(Long groupId) {
        Integer today = today();
        ResourceGroupRecord record = resourceGroupRecordRepository.findOne(new ResourceGroupRecordPKeys(today, groupId));
        if (null == record) {
            record = new ResourceGroupRecord();
            record.setDay(today);
            record.setGroupId(groupId);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        resourceGroupRecordRepository.save(record);
    }

    @Override
    public void recordResource(Long resourceId) {
        Integer today = today();
        ResourceRecord record = resourceRecordRepository.findOne(new ResourceRecordPKeys(today, resourceId));
        if (null == record) {
            record = new ResourceRecord();
            record.setDay(today);
            record.setResourceId(resourceId);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        resourceRecordRepository.save(record);
    }

    @Override
    public void recordShow(Long showId) {
        Integer today = today();
        ShowRecord record = showRecordRepository.findOne(new ShowRecordPKeys(today, showId));
        if (null == record) {
            record = new ShowRecord();
            record.setDay(today);
            record.setShowId(showId);
            record.setTimes(0L);
        }
        record.setTimes(1 + record.getTimes());
        showRecordRepository.save(record);
    }

    @Override
    public void recordUser(String sessionId, UserRecordCache.TYPE type) {
        UserRecordCache record = new UserRecordCache();
        record.setSessionId(sessionId);
        record.setType(type);
        userRecordCacheRepository.save(record);
    }

    @Override
    public void updateRecordUsers() {
        // count all
        Long countAll = userRecordCacheRepository.count();
        Long countNick = userRecordCacheRepository.countAllByType(UserRecordCache.TYPE.NICK);
        Integer today = today();
        UserRecord record = new UserRecord();
        record.setDay(today);
        record.setType(UserRecord.TYPE.NICK);
        record.setTimes(countNick);
        userRecordRepository.save(record);
        UserRecord record2 = new UserRecord();
        record2.setDay(today);
        record2.setType(UserRecord.TYPE.REGISTED);
        record2.setTimes(countAll - countNick);
        userRecordRepository.save(record2);
        userRecordCacheRepository.deleteAll();// 清空此表
    }

    @Override
    public List<RecordArrayItem> findAr(Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findBeacon(Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findQRCode(Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findResourceGroup(Long groupId, Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findResource(Long resourceId, Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findShow(Long showId, Integer startDay, Integer endDay) {
        return null;
    }

    @Override
    public List<RecordArrayItem> findUser(UserRecord.TYPE type, Integer startDay, Integer endDay) {
        return null;
    }
}
