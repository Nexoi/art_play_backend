package com.seeu.artshow.record.service;

import com.seeu.artshow.record.model.UserRecord;
import com.seeu.artshow.record.model.UserRecordCache;
import com.seeu.artshow.record.vo.RecordArrayItem;

import java.util.List;

public interface RecordService {
    void recordAr(); // 自动加一

    void recordBeacon();

    void recordQRCode();

    void recordDeviceByType(VISIT_TYPE type);

    void recordResourceGroup(Long groupId);

    void recordResource(Long resourceId);

    void recordShow(Long showId);

    void recordUser(String sessionId, UserRecordCache.TYPE type);

    // update by day
    void updateRecordUsers();

    //-------------------------//

    List<RecordArrayItem> findAr(Integer startDay, Integer endDay);

    List<RecordArrayItem> findBeacon(Integer startDay, Integer endDay);

    List<RecordArrayItem> findQRCode(Integer startDay, Integer endDay);

    List<RecordArrayItem> findResourceGroup(Long groupId, Integer startDay, Integer endDay);

    List<RecordArrayItem> findResource(Long resourceId, Integer startDay, Integer endDay);

    List<RecordArrayItem> findShow(Long showId, Integer startDay, Integer endDay);

    List<RecordArrayItem> findUser(UserRecord.TYPE type, Integer startDay, Integer endDay);

    enum VISIT_TYPE {
        AR,
        BEACON,
        QRCODE,
    }
}
