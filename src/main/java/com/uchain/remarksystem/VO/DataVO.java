package com.uchain.remarksystem.VO;

import com.uchain.remarksystem.model.Header;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataVO {
    private Header header;
    private com.uchain.remarksystem.model.Data data;
}
