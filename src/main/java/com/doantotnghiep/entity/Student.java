package com.doantotnghiep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StudentId")
    private long studentId;
    @ManyToOne
    @JoinColumn(name = "ClassId")
    private Clazz clazz;
    @OneToMany(mappedBy = "student")
    private List<TestResult> testResults = new ArrayList<>();
    @OneToMany(mappedBy = "student")
    private List<TestHistory> testHistories = new ArrayList<>();
    @Column(name = "FullName")
    private String fullName;
    @Column(name = "Gender")
    private int gender;
    @Column(name = "Birthday")
    private Date birthday;
    @Column(name = "Email")
    private String email;
    @OneToMany(mappedBy = "student")
    private List<RoomDetail> roomDetails = new ArrayList<>();
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public List<TestHistory> getTestHistories() {
        return testHistories;
    }

    public void setTestHistories(List<TestHistory> testHistories) {
        this.testHistories = testHistories;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RoomDetail> getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(List<RoomDetail> roomDetails) {
        this.roomDetails = roomDetails;
    }
}
