package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.model.*;
import com.uchain.remarksystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-13
 * @Description:
 */
@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private FormService formService;
    @Autowired
    private DataService dataService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    AnswerDataService answerDataService;


    @Override
    public void createHeader(HSSFSheet sheet, Project project) {
        log.info("======================= 制定表头 ========================");
        //得到表头数据
        List<Header> headers = formService.selectHeaderByProjectId(project.getId());
        Integer headerLastColumn = headers.size();
        //在excel中添加表头
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers.get(i).getName());
            cell.setCellValue(text);
        }

        if (project.getHasText()==1){
            //添加回答的表头
            List<Header> answerHeaders = formService.selectAnswerHeaderByProjectId(project.getId());
            for (int i = 0; i < answerHeaders.size(); i++) {
                HSSFCell cell = row.createCell(headerLastColumn);
                cell.setCellValue(answerHeaders.get(i).getName());
                headerLastColumn++;
            }
        }else {
            //添加选择标注栏
            HSSFCell cell = row.createCell(headerLastColumn);
            HSSFRichTextString text = new HSSFRichTextString("选择内容");
            cell.setCellValue(text);
        }
        log.info("=======================制定表头========================");
    }

    @Override
    public void createDataCell(Integer startNum, Integer endNum, HSSFSheet sheet, Project project) {
        //选择类项目
        if (project.getHasText()!=1){
            //创建每一行的数据
            for (Integer i = startNum + 1; i <= endNum; i++) {
                HSSFRow row1 = sheet.createRow(i);
                //获取第i行的数据
                List<Data> dataList = dataService.selectByProjectRowNum(project.getId(),i);

                //遍历将数据放入表中
                for (int j = 0; j < dataList.size(); j++) {
                    row1.createCell(j).setCellValue(dataList.get(j).getContent());
                }
                //添加answer的choice
                Answer answer = answerService.selectByProjectAndRowNum(project.getId(),i);
                if (answer==null){
                    continue;
                }
                Choice choice = formService.selectByChoiceId(answer.getChoiceId());
                row1.createCell(dataList.size()).setCellValue(choice.getContent());
            }
        }else {//带有文本的类型的项目
            for (Integer i = startNum + 1; i <= endNum; i++) {
                HSSFRow row1 = sheet.createRow(i);
                //获取第i行的数据
                List<Data> dataList = dataService.selectByProjectRowNum(project.getId(),i);
                //遍历将数据放入表中
                int lastColumn = 0;
                for (lastColumn = 0; lastColumn < dataList.size(); lastColumn++) {
                    row1.createCell(lastColumn).setCellValue(dataList.get(lastColumn).getContent());
                }
                List<AnswerData> answerDataList = answerDataService.selectByProjectRowNum(project.getId(), i);
                for (int k = 0; k < answerDataList.size(); k++) {
                    row1.createCell(lastColumn++).setCellValue(answerDataList.get(k).getContent());
                }
            }
        }
    }


}
