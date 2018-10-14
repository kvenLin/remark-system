package com.uchain.remarksystem.model;

import lombok.Data;

@Data
public class AnswerData {
    private Long id;
    private Integer rowNum;
    private String content;
    private Long packageId;
    private Long answerHeaderId;
    private Long projectId;

}
