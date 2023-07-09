package com.doantotnghiep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoomId")
    private long roomId;
    @ManyToOne
    @JoinColumn(name = "TeacherId")
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "ExamId")
    private Exam exam;
    @ManyToOne
    @JoinColumn(name = "SubjectId")
    private  Subject subject;
    @Column(name = "RoomName")
    private String roomName;
    @Column(name = "Start")
    private Date start;
    @Column(name = "Finish")
    private Date finish;
    @Column(name = "AnswerView")
    private int answerView;
    @OneToMany(mappedBy = "room")
    private List<RoomDetail> roomDetails = new ArrayList<>();
    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public int getAnswerView() {
        return answerView;
    }

    public void setAnswerView(int answerView) {
        this.answerView = answerView;
    }

    public List<RoomDetail> getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(List<RoomDetail> roomDetails) {
        this.roomDetails = roomDetails;
    }
}
