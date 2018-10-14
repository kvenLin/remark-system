package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.model.Answer;
import com.uchain.remarksystem.model.Data;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataMapper {
    int deleteByPrimaryKey(Long id);

    void deleteByProject(Long id);

    int insert(Data record);

    Data selectByPrimaryKey(Long id);

    List<Data> selectAll();

    int updateByPrimaryKey(Data record);

    List<Data> selectByProjectId(Long projectId);

    List<Data> selectByPackageId(Long packageId);

    int grabProjectDataRandom(Long projectId, Long packageId, Integer dataNum);

    List<Data> selectPackageNoAnswerData(Long packageId);

    List<Data> selectByPackageAndRowNum(Long packageId, Integer rowNum);

    List<RowStatusVO> selectRowStatus(Long packageId,Integer dataNum);

    List<Data> selectProjectNoAnswerData(Long projectId);

    List<Data> selectByProjectAndRowNum(Long projectId, Integer rowNum);

    List<Data> selectPackageAnswerData(Long packageId);
}