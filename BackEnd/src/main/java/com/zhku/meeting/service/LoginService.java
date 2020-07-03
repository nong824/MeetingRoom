package com.zhku.meeting.service;

import com.zhku.meeting.entities.Department;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.mapper.DepartmentMapper;
import com.zhku.meeting.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class LoginService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public Msg login(Map<String , Object> para){
        String name = (String)para.get("username");
        String pwd = (String)para.get("password");
        Department department=new Department();
        department.setDeptNo(name);
        department.setDeptPassword(pwd);
        Department b = departmentMapper.loginMessage(department);
        if (b!=null){
            String token = TokenUtil.sign(b);
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            return Msg.success().add(map);
        }
        return Msg.fault();
    }
}
