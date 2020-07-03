package com.zhku.meeting.controller;

import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.service.DepartmentService;
import com.zhku.meeting.service.MeetingRecordService;
import com.zhku.meeting.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class MeetingRecordController {

    @Autowired
    private MeetingRecordService meetingRecordService;

    @Autowired
    private  DepartmentService departmentService;

    //查看会议    带上分页和pass 0代表未进行，1代表已经进行
    @GetMapping("/meeting/history")
    public Msg history(@RequestParam Map<String,Object> para,@RequestHeader Map<String,Object> header){
        String token = (String) header.get("authorization");
        String deptNo = TokenUtil.getNo(token);
        if (!deptNo.equals("admin")){   //部门账号/管理员不需要id
            //返回id
            Integer deptId = departmentService.seletByNo(deptNo);
            para.put("deptId",String.valueOf(deptId));
        }
        return meetingRecordService.getHistory(para);
    }

}
