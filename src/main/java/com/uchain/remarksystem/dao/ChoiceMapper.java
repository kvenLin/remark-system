package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Choice;
import java.util.List;

public interface ChoiceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Choice record);

    Choice selectByPrimaryKey(Long id);

    List<Choice> selectAll();

    int updateByPrimaryKey(Choice record);

    List<Choice> selectByProjectId(Long projectId);

    void deleteChoiceByProject(Long id);
}