package com.uchain.remarksystem.VO;

import com.uchain.remarksystem.model.AnswerData;
import com.uchain.remarksystem.model.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDataVO {
    private Header header;
    private AnswerData answerData;
}
