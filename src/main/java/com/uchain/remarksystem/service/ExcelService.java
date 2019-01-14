package com.uchain.remarksystem.service;

import com.uchain.remarksystem.model.Project;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @Author: clf
 * @Date: 19-1-13
 * @Description:
 * Excel文件服务
 */
public interface ExcelService {
    void createHeader(HSSFSheet sheet, Project project);
    void createDataCell(Integer startNum, Integer endNum, HSSFSheet sheet, Project project);

}
