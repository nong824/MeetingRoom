package com.zhku.meeting.controller;


import com.zhku.meeting.Annotation.loginCharacter;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    //根据ID查单个部门 （deptID）
    @GetMapping("/dept")
    public Msg getDept(@RequestParam Map<String,Object> para){
        return departmentService.getDept(para);
    }
    //分页查询所有部门信息  （currentPage,pageSize）
    @loginCharacter
    @GetMapping("/depts")
    public Msg getAllDept(@RequestParam Map<String,Object> para){
        return departmentService.getAll(para);
    }
    //更新部门信息，带什么改什么  --未查重
    @loginCharacter
    @PutMapping("/dept")
    public Msg update(@RequestBody Map<String,Object> para){
        Msg msg = departmentService.duplicateCheck(para);
        return msg.getCode()==200?msg:departmentService.update(para);
    }
    //添加部门 （deptName,deptPhone,deptNo,deptPassword）
    @loginCharacter
    @PostMapping("/dept")
    public Msg add(@RequestBody Map<String,Object> para){
        Msg msg = departmentService.duplicateCheck(para);
        if (msg.getCode()==200){
            return msg;
        }
        return departmentService.addDept(para);
    }
    //id删除部门  --未连带删除
    @loginCharacter
    @DeleteMapping("/dept")
    public Msg delete(@RequestBody Map<String,Object> para){
        return departmentService.deleteById(para);
    }
    //模糊条件查询，带什么查什么
    @loginCharacter
    @GetMapping("/deptCondition")
    public Msg selectCondition(@RequestParam Map<String,Object> para){
        return departmentService.selectCondition(para);
    }


}
