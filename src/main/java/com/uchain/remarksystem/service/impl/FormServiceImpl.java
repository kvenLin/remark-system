package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.dao.AnswerHeaderMapper;
import com.uchain.remarksystem.dao.ChoiceMapper;
import com.uchain.remarksystem.dao.HeaderMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;
import com.uchain.remarksystem.form.UploadForm;
import com.uchain.remarksystem.form.choice.ChoiceForm;
import com.uchain.remarksystem.form.choice.ChoiceUpdateForm;
import com.uchain.remarksystem.form.header.HeaderForm;
import com.uchain.remarksystem.form.header.HeaderUpdateForm;
import com.uchain.remarksystem.model.*;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.redis.DownloadPathKey;
import com.uchain.remarksystem.redis.RedisService;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.*;
import com.uchain.remarksystem.util.ClientUtil;
import com.uchain.remarksystem.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FormServiceImpl implements FormService {

    @Autowired
    private HeaderMapper headerMapper;
    @Autowired
    private AnswerHeaderMapper answerHeaderMapper;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private DataService dataService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AnswerDataService answerDataService;

    @Override
    public boolean insertHeader(Header header) {
        if (headerMapper.insert(header)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean insertAnswerHeader(Header header) {
        if (answerHeaderMapper.insert(header)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean insertChoice(Choice choice) {
        if (choiceMapper.insert(choice)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateHeader(Header header) {
        if (headerMapper.updateByPrimaryKey(header)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAnswerHeader(Header header) {
        if (answerHeaderMapper.updateByPrimaryKey(header)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean updateChoice(Choice choice) {
        if (choiceMapper.updateByPrimaryKey(choice)==1){
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void deleteHeader(Long id) {
        //判断项目是否启动
        Project project = projectService.selectProjectByHeader(id);
        if (project==null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getStatus()!=0){
            throw new GlobalException(CodeMsg.PROJECT_PROCESSING);
        }
        Header header = selectByHeaderId(id);
        if (header==null){
            return;
        }
        headerMapper.deleteByPrimaryKey(id);
        List<Header> headers = headerMapper.selectLaterHeader(project.getId(),header.getColumnNum());
        if (headers.size()==0){
            return;
        }
        headers.forEach(header1 -> {
            header1.setColumnNum(header1.getColumnNum()-1);
            if (!updateHeader(header1)) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        });
    }

    @Transactional
    @Override
    public void deleteAnswerHeader(Long id) {
        Project project = projectService.selectProjectByAnswerHeader(id);
        if (project==null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getStatus()!=0){
            throw new GlobalException(CodeMsg.PROJECT_PROCESSING);
        }
        if (project.getHasText()!=1){
            throw new GlobalException(CodeMsg.PROJECT_NO_TEXT);
        }
        Header header = selectByAnswerHeaderId(id);
        if (header==null){
            return;
        }
        answerHeaderMapper.deleteByPrimaryKey(id);
        List<Header> headers = answerHeaderMapper.selectLaterAnswerHeader(project.getId(),header.getColumnNum());
        if (headers.size()==0){
            return;
        }
        headers.forEach(header1 -> {
            header1.setColumnNum(header1.getColumnNum()-1);
            if (!updateAnswerHeader(header1)) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        });
    }

    @Override
    public void deleteChoice(Long id) {
        //判断项目是否启动
        Project project = projectService.selectProjectByChoice(id);
        if (project==null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getStatus()!=0){
            throw new GlobalException(CodeMsg.PROJECT_PROCESSING);
        }
        choiceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Header selectHeaderByProjectIdAndColumnNum(Long projectId, Integer columnNum) {
        return headerMapper.selectByProjectIdColumnNum(projectId,columnNum);
    }

    @Override
    public Header selectAnswerHeaderByProjectIdAndColumnNum(Long projectId, Integer columnNum) {
        return answerHeaderMapper.selectByProjectIdColumnNum(projectId,columnNum);
    }

    @Override
    public Header selectByHeaderId(Long headerId) {
        return headerMapper.selectByPrimaryKey(headerId);
    }

    @Override
    public Header selectByAnswerHeaderId(Long answerHeaderId) {
        return answerHeaderMapper.selectByPrimaryKey(answerHeaderId);
    }


    @Override
    public Choice selectByChoiceId(Long choiceId) {
        return choiceMapper.selectByPrimaryKey(choiceId);
    }

    @Override
    public List<Header> selectHeaderByProjectId(Long projectId) {
        return headerMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Header> selectAnswerHeaderByProjectId(Long projectId) {
        return answerHeaderMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Choice> selectChoiceByProjectId(Long projectId) {
        return choiceMapper.selectByProjectId(projectId);
    }

    /**
     *
     * @param headerForm
     * @param headerType
     * 1:添加导表的header
     * 2:添加回答的header
     * @return
     */
    @Override
    public CodeMsg addHeader(HeaderForm headerForm,Integer headerType) {
        Project project = projectService.selectById(headerForm.getProjectId());
        if (project==null){
            return CodeMsg.PROJECT_NOT_EXIST;
        }
        if (project.getStatus()!=0){
            return CodeMsg.PROJECT_PROCESSING;
        }
        Header header;
        if (headerType==0){
            header = selectHeaderByProjectIdAndColumnNum(headerForm.getProjectId(),headerForm.getColumnNum());
        }else {
            if (project.getHasText()!=1){
                return CodeMsg.PROJECT_NO_TEXT;
            }
            header = selectAnswerHeaderByProjectIdAndColumnNum(headerForm.getProjectId(),headerForm.getColumnNum());
        }
        if (header!=null){
            log.error(header.toString());
            return CodeMsg.HEADER_COLUMN_EXIST;
        }
        header = new Header();
        BeanUtils.copyProperties(headerForm,header);
        Long id = userService.getCurrentUser().getId();
        header.setCreatedBy(id);
        header.setUpdatedBy(id);
        //根据添加headerType的不同类型,来添加1:header和2:answerHeader
        if (headerType==0){
            if (insertHeader(header)) {
                return null;
            }
        }else {
            if (insertAnswerHeader(header)){
                return null;
            }
        }
        return CodeMsg.ADD_ERROR;
    }

    /**
     *
     * @param headerForms
     * @param headerType
     * 1:添加导表的headers
     * 2:添加回答的headers
     * @return
     */
    @Transactional
    @Override
    public Result addHeaders(HeaderForm[] headerForms,Integer headerType) {
        for (HeaderForm headerForm : headerForms) {
            CodeMsg codeMsg = addHeader(headerForm,headerType);
            if (codeMsg!=null&&codeMsg.getCode()!=0){
                log.error(codeMsg.getMsg());
                throw new GlobalException(codeMsg);
            }
        }
        return Result.success();
    }

    @Override
    public CodeMsg addChoice(ChoiceForm choiceForm) {
        Choice choice  = new Choice();
        Project project = projectService.selectById(choiceForm.getProjectId());
        if (project.getHasText()==1){
            return CodeMsg.PROJECT_HAS_TEXT;
        }
        if (project==null){
            return CodeMsg.PROJECT_NOT_EXIST;
        }
        if (project.getStatus()!=0){
            return CodeMsg.PROJECT_PROCESSING;
        }
        BeanUtils.copyProperties(choiceForm,choice);
        Long id = userService.getCurrentUser().getId();
        choice.setCreatedBy(id);
        choice.setUpdatedBy(id);
        if (insertChoice(choice)){
            return null;
        }
        return CodeMsg.ADD_ERROR;
    }

    @Transactional
    @Override
    public Result addChoices(ChoiceForm[] choiceForms) {
        for (ChoiceForm choiceForm : choiceForms) {
            CodeMsg codeMsg = addChoice(choiceForm);
            if (codeMsg!=null&&codeMsg.getCode()!=0){
                log.error(codeMsg.getMsg());
                throw new GlobalException(codeMsg);
            }
        }
        return Result.success();
    }

    @Override
    public CodeMsg changeChoice(ChoiceUpdateForm choiceUpdateForm) {
        Choice choice = selectByChoiceId(choiceUpdateForm.getId());
        if (choice==null){
            Result.error(CodeMsg.CHOICE_NOT_EXIST);
        }
        Project project = projectService.selectById(choice.getProjectId());
        if (project==null){
            return CodeMsg.PROJECT_NOT_EXIST;
        }
        if (project.getStatus()!=0){
            return CodeMsg.PROJECT_PROCESSING;
        }
        if (project.getHasText()==1){
            return CodeMsg.PROJECT_HAS_TEXT;
        }
        BeanUtils.copyProperties(choiceUpdateForm,choice);
        choice.setUpdatedBy(userService.getCurrentUser().getId());
        if (updateChoice(choice)){
            return null;
        }
        return CodeMsg.UPDATE_ERROR;
    }

    @Transactional
    @Override
    public Result changeChoices(ChoiceUpdateForm[] choiceUpdateForms) {
        for (ChoiceUpdateForm choiceUpdateForm : choiceUpdateForms) {
            CodeMsg codeMsg = changeChoice(choiceUpdateForm);
            if (codeMsg.getCode()!=0){
                throw new GlobalException(codeMsg);
            }
        }
        return Result.success();
    }

    /**
     *
     * @param headerUpdateForm
     * @param headerType
     * 1:更改导表的header
     * 2:更改回答的header
     * @return
     */
    @Override
    public CodeMsg changeHeader(HeaderUpdateForm headerUpdateForm,Integer headerType) {
        Project project = projectService.selectById(headerUpdateForm.getProjectId());
        if (project==null){
            return CodeMsg.PROJECT_NOT_EXIST;
        }
        if (project.getStatus()!=0){
            return CodeMsg.PROJECT_PROCESSING;
        }
        Header header;
        if (headerType==0){
            header = selectByHeaderId(headerUpdateForm.getId());
        }else {
            if (project.getHasText()!=1){
                return CodeMsg.PROJECT_NO_TEXT;
            }
            header = selectByAnswerHeaderId(headerUpdateForm.getId());
        }
        if (header==null){
            return CodeMsg.HEADER_NOT_EXIST;
        }
        BeanUtils.copyProperties(headerUpdateForm,header);
        header.setUpdatedBy(userService.getCurrentUser().getId());
        if (headerType==0){
            if (updateHeader(header)){
                return null;
            }
        }else {
            if (updateAnswerHeader(header)){
                return null;
            }
        }
        return CodeMsg.UPDATE_ERROR;
    }

    /**
     *
     * @param headerUpdateForms
     * @param headerType
     * 1:更改导表的headers
     * 2:更改回答的headers
     * @return
     */
    @Transactional
    @Override
    public Result changeHeaders(HeaderUpdateForm[] headerUpdateForms,Integer headerType) {
        for (HeaderUpdateForm headerUpdateForm : headerUpdateForms) {
            CodeMsg codeMsg = changeHeader(headerUpdateForm,headerType);
            if (codeMsg.getCode()!=0){
                throw new GlobalException(codeMsg);
            }
        }
        return Result.success();
    }

    @Override
    @Transactional
    public Result uploadFile(UploadForm uploadForm) {
        Project project = projectService.selectById(uploadForm.getProjectId());
        if (project==null){
            return Result.error(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getDataNum()!=0){
            return Result.error(CodeMsg.PROJECT_DATA_EXIST);
        }
        //得到当前项目的所有header
        List<Header> headers = selectHeaderByProjectId(uploadForm.getProjectId());
        if (headers.size()==0){
            return Result.error(CodeMsg.HEADER_NOT_EXIST);
        }
        Map<Integer,Header> headerMap = new HashMap<>();
        for (Header header : headers) {
            headerMap.put(header.getColumnNum(),header);
        }
        Workbook wb = FileUtil.getWb(uploadForm.getFile());
        if (wb==null){
            return Result.error(CodeMsg.FILE_FORMAT_ERROR);
        }
        Sheet sheet = wb.getSheetAt(0);
        //得到总行数,注:sheet得到的最后行数是索引坐标不是真正的行数
        int rowNum = sheet.getLastRowNum()+1;
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        //正文内容从第0行起
        for (int i = 0; i <rowNum; i++) {
            row = sheet.getRow(i);
            //如果当前行为空行则跳过该行
            if (row==null){
                continue;
            }
            int j = 0;
            log.info("遍历第{}行",i+1);
            while (j<colNum){
                //判断当前单元格是否存在header,如果不存在说明制定的格式不匹配
                Header header = headerMap.get(j+1);
                if (header==null){
                     throw new GlobalException(CodeMsg.HEADER_NOT_MATCH);
                }
                //读取到当前单元的数据信息
                Object obj = FileUtil.getCellFormatValue(row.getCell(j));
                //如果匹配则进行添加数据
                Data data = new Data();
                data.setHeaderId(header.getId());
                data.setContent(obj.toString());
                data.setProjectId(project.getId());
                //存入当前实际的所在的行数
                data.setRowNum(i+1);
                if (!dataService.insert(data)) {
                    throw new GlobalException(CodeMsg.ADD_ERROR);
                }

                log.info("当前元素信息的信息:{}",data);
                log.info("cellValue 第{}个 , 当前值:{}",j+1,obj);
                j++;
            }

        }
        //启动项目
        project.setStatus(1);
        project.setDataNum(rowNum);
        if (!projectService.update(project)) {
            return Result.error(CodeMsg.UPDATE_ERROR);
        }
        return Result.success("数据导入已完成!");
    }

    @Override
    public void download(Long projectId, HttpServletResponse response) throws IOException {
        Project project = projectService.selectById(projectId);
        if (project==null){
            throw new GlobalException(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getStatus()==0) {
            throw new GlobalException(CodeMsg.PROJECT_NOT_START);
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(project.getName());
        String fileName = project.getName();//设置导出的文件的名字

        //得到表头数据
        List<Header> headers = selectHeaderByProjectId(projectId);
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
            List<Header> answerHeaders = selectAnswerHeaderByProjectId(projectId);
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

        //项目还在进行中
        if (project.getStatus()==1){
            //得到所有完成的数据包
            List<Package> packages = packageService.selectFinishByProject(projectId);

            //判断项目是否带有文本
            if (project.getHasText()==0){//不带文本的形式

                //遍历数据包
                for (Package aPackage : packages) {
                    Integer k = 1;
                    List<Data> dataList = dataService.allPackageData(aPackage.getId());
                    //获取第一个元素的行数
                    Integer tag = dataList.get(0).getRowNum();
                    Integer headerNum = 0;
                    //TODO,遍历单元格数据创建每一行的数据
                    HSSFRow row1 = sheet.createRow(k);
                    for (int i = 0; i < dataList.size(); i++) {
                        Data data = dataList.get(i);
                        //获取当前行数
                        Integer rowNum = data.getRowNum();
                        //判断是否是遍历到了下一行的数据
                        if (tag!=rowNum){
                            //添加上一行的最后的标注信息和描述信息
                            Answer answer = answerService.selectByPackageAndRowNum(aPackage.getId(),tag);
                            Choice choice = selectByChoiceId(answer.getChoiceId());
                            row1.createCell(headerNum++).setCellValue(choice.getContent());
                            //进行换行操作
                            k++;
                            //重新从第一个列元素进行添加
                            headerNum = 0;
                            //创建新的行
                            row1 = sheet.createRow(k);
                        }
                        //标识当前行
                        tag = rowNum;
                        //添加原始信息
                        row1.createCell(headerNum++).setCellValue(data.getContent());
                        //遍历到最后一行的最后一个元素
                        if (i+1==dataList.size()){
                            //添加上一行的最后的标注信息和描述信息
                            Answer answer = answerService.selectByPackageAndRowNum(aPackage.getId(), data.getRowNum());
                            Choice choice = selectByChoiceId(answer.getChoiceId());
                            row1.createCell(headerNum++).setCellValue(choice.getContent());
                        }
                    }
                }
            }else {
                //记录行数
                Integer k = 1;
                //带文本的形式进行
                for (Package aPackage : packages) {
                    List<Data> dataList = dataService.allPackageData(aPackage.getId());
                    //获取第一个元素的行数
                    Integer tag = dataList.get(0).getRowNum();
                    Integer headerNum = 0;
                    HSSFRow row1 = sheet.createRow(k);
                    for (int i = 0; i < dataList.size(); i++) {
                        Data data = dataList.get(i);
                        //获取当前行数
                        Integer rowNum = data.getRowNum();
                        //判断是否是遍历到了下一行的数据
                        if (tag!=rowNum){
                            //添加上一行的最后的answerData
                            List<AnswerData> answerDataList = answerDataService.selectByProjectRowNum(aPackage.getProjectId(), tag);
                            for (AnswerData answerData : answerDataList) {
                                row1.createCell(headerNum++).setCellValue(answerData.getContent());
                            }
                            //进行换行操作
                            k++;
                            //重新从第一个列元素进行添加
                            headerNum = 0;
                            //创建新的行
                            row1 = sheet.createRow(k);
                        }
                        //标识当前行
                        tag = rowNum;
                        //添加原始信息
                        row1.createCell(headerNum++).setCellValue(data.getContent());
                        //遍历到最后一行的最后一个元素
                        if (i+1==dataList.size()){
                            //添加上一行的最后的标注信息和描述信息
                            List<AnswerData> answerDataList = answerDataService.selectByProjectRowNum(projectId, data.getRowNum());
                            for (AnswerData answerData : answerDataList) {
                                row1.createCell(headerNum++).setCellValue(answerData.getContent());
                            }
                        }
                    }
                }
            }

        }
        //项目已经全部完成
        if (project.getStatus()==2){
            //选择类项目
            if (project.getHasText()!=1){
                //创建每一行的数据
                for (Integer i = 0; i < project.getDataNum(); i++) {
                    HSSFRow row1 = sheet.createRow(i+1);
                    //获取第i+1行的数据
                    List<Data> dataList = dataService.selectByProjectRowNum(projectId,i+1);

                    //遍历将数据放入表中
                    for (int j = 0; j < dataList.size(); j++) {
                        row1.createCell(j).setCellValue(dataList.get(j).getContent());
                    }
                    //添加answer的choice
                    Answer answer = answerService.selectByProjectAndRowNum(projectId,i+1);
                    if (answer==null){
                        continue;
                    }
                    Choice choice = selectByChoiceId(answer.getChoiceId());
                    row1.createCell(dataList.size()).setCellValue(choice.getContent());
                }
            }else {//带有文本的类型的项目
                for (Integer i = 0; i < project.getDataNum(); i++) {
                    HSSFRow row1 = sheet.createRow(i+1);
                    //获取第i+1行的数据
                    List<Data> dataList = dataService.selectByProjectRowNum(projectId,i+1);
                    //遍历将数据放入表中
                    int lastColumn = 0;
                    for (lastColumn = 0; lastColumn < dataList.size(); lastColumn++) {
                        row1.createCell(lastColumn).setCellValue(dataList.get(lastColumn).getContent());
                    }
                    List<AnswerData> answerDataList = answerDataService.selectByProjectRowNum(projectId, i + 1);
                    for (int k = 0; k < answerDataList.size(); k++) {
                        row1.createCell(lastColumn++).setCellValue(answerDataList.get(k).getContent());
                    }
                }
            }
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.addHeader("Content-Disposition", "attachment; filename="
                + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xlsx");
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public Result getDownloadPath(HttpServletRequest request) {
        String clientIpAddress = ClientUtil.getClientIpAddress(request);
        String path = String.valueOf(System.currentTimeMillis()).substring(6);
        redisService.set(DownloadPathKey.getByClientIp,""+clientIpAddress,path);
        return Result.success(path);
    }

    @Override
    public boolean checkPath(HttpServletRequest request, String path) {
        String clientIpAddress = ClientUtil.getClientIpAddress(request);
        String s = redisService.get(DownloadPathKey.getByClientIp, clientIpAddress, String.class);
        if (path.equals(s)){
            return true;
        }
        return false;
    }


}
