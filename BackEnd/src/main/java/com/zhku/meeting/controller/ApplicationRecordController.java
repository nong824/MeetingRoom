package com.zhku.meeting.controller;

import com.zhku.meeting.Annotation.loginCharacter;
import com.zhku.meeting.entities.ApplicationRecord;
import com.zhku.meeting.entities.MeetingRecord;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.service.ApplicationRecordService;
import com.zhku.meeting.service.DepartmentService;
import com.zhku.meeting.service.MeetingRecordService;
import com.zhku.meeting.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ApplicationRecordController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ApplicationRecordService applicationRecordService;

    @Autowired
    private MeetingRecordService meetingRecordService;


    //查看申请
    @GetMapping("/apply/history")
    public Msg history(@RequestParam Map<String,Object> para,@RequestHeader Map<String,Object> header){
        String token = (String) header.get("authorization");
        String deptNo = TokenUtil.getNo(token);
        if (!deptNo.equals("admin")){   //部门账号/管理员不需要id
        //返回id
            int deptId = departmentService.seletByNo(deptNo);
            para.put("deptId",deptId);
        }
        return applicationRecordService.select(para);
    }


    //同意申请，审批通过
    @loginCharacter
    @PutMapping("/apply/accept")
    public Msg accept(@RequestBody Map<String,Object> para){
        Integer id = (Integer) para.get("applicationId");
        //获取记录
        ApplicationRecord applicationRecord = applicationRecordService.getById(id);
        //封装会议
        MeetingRecord meetingRecord = new MeetingRecord(applicationRecord.getMeetingTheme(),
                applicationRecord.getMeetingSize(),applicationRecord.getDeptId(),
                applicationRecord.getRoomId(),applicationRecord.getApplyDate(),applicationRecord.getApplySlot());
        meetingRecord.setApplyId(applicationRecord.getApplicationId());
        //插入会议
        meetingRecordService.addRecord(meetingRecord);
        //修改申请的状态
        return  applicationRecordService.updateStatus(id,1,"");
    }

    //拒绝申请
    @loginCharacter
    @PutMapping("/apply/reject")
    public Msg reject(@RequestBody Map<String,Object> para){
        Integer id = (Integer) para.get("applicationId");
        String reason = "";
        if(para.containsKey("rejectReason")){
            reason = (String)para.get("rejectReason");
        }
        //修改申请的状态
        return  applicationRecordService.updateStatus(id,2,reason);
    }




}
