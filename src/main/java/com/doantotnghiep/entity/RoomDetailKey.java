package com.doantotnghiep.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RoomDetailKey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "RoomId")
    private long roomId;
    @Column(name = "StudentId")
    private long studentId;
    @Column(name = "TestId")
    private long testId;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    @Override
    public int hashCode() {
        return (int)(studentId+roomId+testId);
    }

    @Override
    public boolean equals(Object obj) {
        RoomDetailKey roomDetailKey = (RoomDetailKey) obj;
        return this.roomId!=roomDetailKey.getRoomId()&&this.studentId!=roomDetailKey.getStudentId()&&
                this.testId!=roomDetailKey.getTestId();
    }
}
