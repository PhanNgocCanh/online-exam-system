package com.doantotnghiep.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomDTO {
    private long roomId;
    private long teacherId;
    private long examId;
    private int subjectId;
    private String roomName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date finish;
    private String timeStart;
    private String timeFinish;
    private String formatTimeStart;
    private String formatTimeFinish;
    private int answerView;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getStart() {
        return this.start;
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

    public String getTimeStart() {
       return this.timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeFinish() {
        return this.timeFinish;
    }

    public void setTimeFinish(String timeFinish) {
        this.timeFinish = timeFinish;
    }

    public String getFormatTimeStart() {

        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.start);
    }

    public void setFormatTimeStart(String formatTimeStart) {
        this.formatTimeStart = formatTimeStart;
    }

    public String getFormatTimeFinish() {
        return  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(this.finish);
    }

    public void setFormatTimeFinish(String formatTimeFinish) {
        this.formatTimeFinish = formatTimeFinish;
    }

    public int getAnswerView() {
        return answerView;
    }

    public void setAnswerView(int answerView) {
        this.answerView = answerView;
    }
}
