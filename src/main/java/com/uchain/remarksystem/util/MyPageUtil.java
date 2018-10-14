package com.uchain.remarksystem.util;

import com.uchain.remarksystem.VO.PageInfoVO;
import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;

import java.util.List;

public class MyPageUtil {
    public final static Integer userNum = 8;
    public final static Integer projectNum = 10;

    public final static int packageNum = 8;
    public final static int dataVoNum = 9;
    public final static int selectAddUserNum = 3;


    public static PageInfoVO getMyPageInfo(List list, Integer pageNum){
        int total = list.size();

        PageInfoVO<RowStatusVO> pageInfo = new PageInfoVO<>();
        int re = total/9;
        if (total%9>0){
            re++;
        }

        if (pageNum>re){
            throw new GlobalException(CodeMsg.PAGE_NO_DATA);
        }

        int startNum = 9*(pageNum-1);
        int endNum = 9*pageNum;
        if (pageNum==re){
            endNum = total;
        }

        List<RowStatusVO> rowStatusVOS = list.subList(startNum, endNum);
        pageInfo.setTotal(total);
        pageInfo.setList(rowStatusVOS);
        pageInfo.setPageSize(MyPageUtil.dataVoNum);
        return pageInfo;
    }
}
