package com.doantotnghiep.dto;

import java.util.ArrayList;
import java.util.List;

public class ListQuestionDTO {
    private List<QuestionAnswerDTO> questionAnswerDTOs = new ArrayList<>();

    public void add(QuestionAnswerDTO questionAnswerDTO){
        this.questionAnswerDTOs.add(questionAnswerDTO);
    }

    public List<QuestionAnswerDTO> getQuestionAnswerDTOs() {
        return questionAnswerDTOs;
    }

    public void setQuestionAnswerDTOs(List<QuestionAnswerDTO> questionAnswerDTOs) {
        this.questionAnswerDTOs = questionAnswerDTOs;
    }
}
