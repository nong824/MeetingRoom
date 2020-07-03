package com.zhku.meeting.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhku.meeting.entities.MeetingRecord;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.mapper.MeetingRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MeetingRecordService {

    @Autowired
    private MeetingRecordMapper meetingRecordMapper;

    public Msg getHistory(Map<String, Object> para) {
        Integer pass = Integer.parseInt((String) para.get("pass"));
        long a = System.currentTimeMillis();
        Date meetingDate =new Date(a);
        Integer deptId = null;
        if(para.containsKey("deptId")){    //判断是否管理员
            deptId = Integer.parseInt((String)para.get("deptId"));
        }
        Integer roomId =null;
        if (para.containsKey("roomId")){
            roomId = Integer.parseInt((String) para.get("roomId"));
        }
        Integer meetSlot =null;
        if (para.containsKey("meetSlot")){
            meetSlot = Integer.parseInt((String) para.get("meetSlot"));
        }
        Integer pageSize = Integer.parseInt((String) para.get("pageSize"));
        Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setDeptId(deptId);
        meetingRecord.setMeetingDate(meetingDate);
        meetingRecord.setRoomId(roomId);
        meetingRecord.setMeetingSlot(meetSlot);
        PageHelper.startPage(currentPage,pageSize);
        List<MeetingRecord> meetingRecords = meetingRecordMapper.selectByCondition(meetingRecord,pass);
        PageInfo pageInfo = new PageInfo(meetingRecords);
        Map<String,Object> map = new HashMap<>();
        map.put("result",pageInfo);
        return Msg.success().add(map);
    }

    public void addRecord(MeetingRecord meetingRecord) {
        meetingRecordMapper.insert(meetingRecord);
    }

    public void revoke(Integer applyId) {
        meetingRecordMapper.deleteByPrimaryKey(applyId);
    }
//    @Autowired
//    private MeetingRecordDao meetingRecordDao;
//    public MeetingRecord getRecord(Integer id){
//        return meetingRecordDao.findById(id).get();
//    }
}
