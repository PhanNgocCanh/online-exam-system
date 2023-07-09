package com.doantotnghiep.entity;

import javax.persistence.*;

@Entity
@Table(name = "RoomDetail")
public class RoomDetail {
    @EmbeddedId
    private RoomDetailKey roomDetailKey;
    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "RoomId")
    private Room room;
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "StudentId")
    private Student student;
    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "TestId")
    private Test test;
    @Column(name = "Status")
    private int status;

    public RoomDetailKey getRoomDetailKey() {
        return roomDetailKey;
    }

    public void setRoomDetailKey(RoomDetailKey roomDetailKey) {
        this.roomDetailKey = roomDetailKey;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
