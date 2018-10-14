package com.uchain.remarksystem.exception;

import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.result.Result;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException{
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg) {
        super();
        this.codeMsg = codeMsg;
    }

}
