package com.zhku.meeting.controller;

import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Msg login(@RequestBody Map<String , Object> para){
        return loginService.login(para);
    }

}
