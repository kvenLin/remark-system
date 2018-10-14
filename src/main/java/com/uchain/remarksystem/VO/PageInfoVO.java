package com.uchain.remarksystem.VO;

import lombok.Data;

import java.util.List;

@Data
public class PageInfoVO<T> {
    private Integer total;
    private Integer pageSize;
    private List<T> list;
}
