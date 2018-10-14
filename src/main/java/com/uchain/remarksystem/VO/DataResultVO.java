package com.uchain.remarksystem.VO;

import com.uchain.remarksystem.model.Answer;
import com.uchain.remarksystem.model.Choice;
import com.uchain.remarksystem.model.Package;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataResultVO {
    //显示当前数据的信息
    private List<DataVO> dataVOS;
    private List choiceOrAnswerDataVOS;
    //若已经回答则显示结果,若没有回答则显示null
    private Answer answer;
    private Integer wordsNum;
    //0:没有,1:有
    private Integer hasText;
}
