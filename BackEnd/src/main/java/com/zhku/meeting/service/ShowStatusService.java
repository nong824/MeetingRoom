package com.zhku.meeting.service;

import com.zhku.meeting.entities.Room;
import com.zhku.meeting.entities.ShowStatus;
import com.zhku.meeting.entities.TempStatus;
import com.zhku.meeting.mapper.RoomMapper;
import com.zhku.meeting.mapper.ShowStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowStatusService {

    @Autowired
    private ShowStatusMapper showStatusMapper;

    @Autowired
    private RoomMapper roomMapper;

    public void updateOccupy(TempStatus occupyRooms) {
        showStatusMapper.updateStatus(occupyRooms);
    }

    public void updateDefault() {
        List<Room> rooms = roomMapper.selectAll();
        for(Room room : rooms){
            showStatusMapper.updateDefault(room);
        }
    }

    public void updateMessage(ShowStatus showStatus) {
        showStatusMapper.updateByPrimaryKey(showStatus);
    }

    public void updateRepair(Room room) {
        showStatusMapper.updateRepair(room);
    }
}
