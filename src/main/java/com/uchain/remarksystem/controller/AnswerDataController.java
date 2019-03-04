package com.uchain.remarksystem.controller;

import com.uchain.remarksystem.DTO.AnswerDataDTO;
import com.uchain.remarksystem.DTO.AnswerDataUpdateDTO;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.service.AnswerDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/remark/answerData")
@RestController
@CrossOrigin
@Slf4j
public class AnswerDataController {

    @Autowired
    private AnswerDataService answerDataService;

    @PostMapping("/addAnswerData")
    @RequireRole(role = RoleEnum.REMARK)
    public Object addAnswerData(@Valid @RequestBody AnswerDataDTO answerDataDTO){
        return answerDataService.addAnswerData(answerDataDTO);
    }


    @PostMapping("/updateAnswerData")
    @RequireRole(role = RoleEnum.REMARK)
    public Object updateAnswerData(@Valid @RequestBody AnswerDataUpdateDTO answerDataUpdateDTO){
        return answerDataService.updateAnswerData(answerDataUpdateDTO);
    }
}
