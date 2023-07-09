package com.doantotnghiep.dto;

import java.util.ArrayList;
import java.util.List;

public class TestAutomaticDTO {

    private String testName;
    private int numOfTest;
    private int maxScore;
    private int maxTime;
    private List<RowTestAutomatic> rowTestAutomatic = new ArrayList<>();

    public void add(RowTestAutomatic testAutomaticDTO){
        rowTestAutomatic.add(testAutomaticDTO);
    }


    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getNumOfTest() {
        return numOfTest;
    }

    public void setNumOfTest(int numOfTest) {
        this.numOfTest = numOfTest;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public List<RowTestAutomatic> getRowTestAutomatic() {
        return rowTestAutomatic;
    }

    public void setRowTestAutomatic(List<RowTestAutomatic> rowTestAutomatic) {
        this.rowTestAutomatic = rowTestAutomatic;
    }
}
