package com.zhku.meeting.entities;

public class TempStatus {
    private int roomId;
    private int meetingSlot;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMeetingSlot() {
        return meetingSlot;
    }

    public void setMeetingSlot(int meetingSlot) {
        this.meetingSlot = meetingSlot;
    }
}
