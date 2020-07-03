package com.zhku.meeting.service;

import com.zhku.meeting.entities.Manager;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.mapper.ManagerMapper;
import com.zhku.meeting.util.TokenUtil;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ManagerService {

    @Autowired
    private ManagerMapper managerMapper;

    public Msg login(Map<String, Object> para) {
        String username = (String)para.get("username");
        String password = (String)para.get("password");
        Manager manager = new Manager();
        manager.setUsername(username);
        manager.setPassword(password);

        Manager result = managerMapper.loginMessage(manager);
        if(result!=null){
            String token = TokenUtil.sign(manager);
            Map<String , Object> map = new HashMap<>();
            map.put("token",token);
            return Msg.success().add(map);
        }
        return Msg.fault();
    }
}
