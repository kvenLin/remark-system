package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.VO.PackageInfoVO;
import com.uchain.remarksystem.VO.UserPackageVO;
import com.uchain.remarksystem.model.Package;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PackageMapper {
    int deleteByPrimaryKey(Long id);

    void deleteByProject(Long id);

    int insert(Package record);

    Package selectByPrimaryKey(Long id);

    List<Package> selectAll();

    int updateByPrimaryKey(Package record);

    List<Package> selectByUserIdAndProjectId(Long userId, Long projectId);

    List<Package> selectByProjectId(Long projectId);

    List<UserPackageVO> selectByUserId(Long id);

    List<Package> selectByStatusAndProject(int i, Long projectId);

    List<PackageInfoVO> selectPackageVOByProjectId(@Param("info") String info);

    List<Package> selectUnFinishPackage(Long userId, Long projectId);

    Package selectByAnswerId(Long answerId);

    List<UserPackageVO> selectByUserJoin(Long userId);
}