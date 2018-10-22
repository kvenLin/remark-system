package com.uchain.remarksystem.VO;

import lombok.Data;

@Data
public class RowStatusVO {
    private Integer rowNum;
    private Integer status;

    @Override
    public String toString() {
        return "{" +
                "rowNum:" + rowNum +
                ", status:" + status +
                '}';
    }
}
