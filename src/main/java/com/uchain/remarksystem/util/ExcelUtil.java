package com.uchain.remarksystem.util;

import com.uchain.remarksystem.DTO.DataGroup;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: clf
 * @Date: 19-1-13
 * @Description:
 */
public class ExcelUtil {
    /**
     * 拆分数据条数
     * @param total
     * @param splitNum
     * @return
     */
    public static List getDataGroup(Integer total, Integer splitNum){
        List<DataGroup> groups = new ArrayList<>();
        int startNum = 0;
        while (total - splitNum > 0){
            groups.add(new DataGroup(startNum, startNum = startNum + splitNum));
            total -= splitNum;
        }
        //将拆分剩下的包装成一个group
        if (total != 0){
            groups.add(new DataGroup(startNum, startNum + total));
        }
        return groups;
    }

}
