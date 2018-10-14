package com.uchain.remarksystem.VO;

import lombok.Data;

import java.util.Date;

@Data
public class PackageInfoVO {
    private Long id;

    private String projectName;

    private String username;

    //从抓取完成就进入开始标注状态.0:未完成,1:审核状态,2:验收状态,3:审核打回,4:验收打回,5:通过
    private Integer status;

    private Date startTime;

    private Date updateTime;

}
