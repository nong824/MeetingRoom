package com.zhku.meeting.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhku.meeting.entities.Msg;
import com.zhku.meeting.entities.Room;
import com.zhku.meeting.entities.ShowStatus;
import com.zhku.meeting.mapper.MeetingRecordMapper;
import com.zhku.meeting.mapper.RoomMapper;
import com.zhku.meeting.mapper.ShowStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ShowStatusMapper showStatusMapper;

    @Autowired
    private MeetingRecordMapper meetingRecordMapper;

    private Room room ;
    private List<Room> rooms;

    public Msg getRoom(Map<String,Object> para){
        Integer id = Integer.parseInt((String)para.get("roomId"));
        Room room = roomMapper.selectByPrimaryKey(id);
        Map<String ,Object> map = new HashMap<>();
        map.put("result",room);
        return Msg.success().add(map);
    }

    public Msg getAll(Map<String,Object> para) {
        Map<String ,Object> map = new HashMap<>();
        if(para.containsKey("currentPage")){
            Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
            Integer pageSize = Integer.parseInt((String)para.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            rooms = roomMapper.selectAll();
            PageInfo pageInfo = new PageInfo(rooms);
            map.put("result",pageInfo);
        }else{
            rooms = roomMapper.selectAll();
            map.put("result",rooms);
        }
        return Msg.success().add(map);
    }

    public Msg updateRoom(Map<String,Object> para) {
        Integer id = (Integer)para.get("roomId");
        String no=null;
        Integer size=null;
        Boolean status = null;
        if(!para.containsKey("roomStatus")){
            no = (String)para.get("roomNo");
            size = (Integer)para.get("roomSize");
        }else{
            status = (Boolean)para.get("roomStatus");
        }
        room = new Room(no,size,status);
        room.setRoomId(id);
        int num = roomMapper.updateByPrimaryKey(room);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg addRoom(Map<String,Object> para) {
        String no = (String)para.get("roomNo");
        Integer size =(Integer)para.get("roomSize");
        room =new Room(no,size,null);
        //连同状态表也插入
        showStatusMapper.insert(room);
        int num = roomMapper.insert(room);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg deleteRoom(Map<String,Object> para) {
        Integer id = (Integer)para.get("roomId");
        int num = roomMapper.deleteByPrimaryKey(id);
        showStatusMapper.deleteByPrimaryKey(id);
        meetingRecordMapper.deleteByCondition(id);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg selectByNo(Map<String, Object> para) {
        //取出信息
        String no=null;
        Integer size=null;
        Boolean status=null;
        if (para.containsKey("roomNo")){
            no ="%"+para.get("roomNo")+"%";
        }
        if(para.containsKey("roomSize")){
            size = Integer.parseInt((String)para.get("roomSize"));
        }
        if(para.containsKey("roomStatus")){
            status = (Boolean)para.get("roomStatus");
        }
        Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
        Integer pageSize = Integer.parseInt((String)para.get("pageSize"));

        //封装
        room = new Room(no,size,status);

        //分页
        PageHelper.startPage(currentPage,pageSize);
        List<Room> rooms = roomMapper.selectByNo(room);
        PageInfo pageInfo = new PageInfo(rooms);

        Map<String ,Object> map = new HashMap<>();
        map.put("result",pageInfo);
        return Msg.success().add(map);
    }

    //查重
    public Msg duplicateCheck(Map<String, Object> para) {
        String roomNo = (String) para.get("roomNo");
        Integer roomId = null;
        if(para.containsKey("roomId")){
            roomId = (Integer) para.get("roomId");
        }
        Room room = new Room(roomNo,null,null);
        room.setRoomId(roomId);
        Room room1 = roomMapper.duplicateCheck(room);
        if(room1!=null){
            return Msg.fault().setError("该会议室编号已经存在");
        }
        return  Msg.success();
    }


    //根据占用的会议室id查询空闲的会议室
    public Msg getFree(Map<String, Object> para) {
        Map<String,Object> map = new HashMap<>();
        List<ShowStatus> statuses;
        if(para.containsKey("currentPage")){
            Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
            Integer pageSize = Integer.parseInt((String)para.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            //查出所有
            statuses = showStatusMapper.selectAll();
            PageInfo pageInfo = new PageInfo(statuses);
            map.put("result",pageInfo);
        }else{
            statuses = showStatusMapper.selectAll();
            map.put("result",statuses);
        }
        return Msg.success().add(map);
    }

    public List<Room> getRooms() {
        return roomMapper.selectAll();
    }

    public Msg updateEquipment(Map<String, Object> para) {
        Integer id = (Integer)para.get("roomId");
        Boolean air = null;
        Boolean projector = null;
        if(para.containsKey("air")){
            air = (Boolean)para.get("air");
            showStatusMapper.updateEquipment(id,air,projector);
            roomMapper.updateEquipment(id,air,projector);
            return Msg.success();
        }
        projector = (Boolean)para.get("projector");
        showStatusMapper.updateEquipment(id,air,projector);
        roomMapper.updateEquipment(id,air,projector);
        return Msg.success();
    }
}
