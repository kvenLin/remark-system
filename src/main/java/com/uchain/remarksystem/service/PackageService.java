package com.uchain.remarksystem.service;

import com.uchain.remarksystem.VO.PackageInfoVO;
import com.uchain.remarksystem.VO.UserPackageVO;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface PackageService {
    boolean insert(Package aPackage);
    boolean update(Package aPackage);
    void delete(Long packageId);
    Package selectById(Long packageId);
    List<Package> selectByUserAndProject(Long userId,Long projectId);
    Package selectByAnswerId(Long answerId);

    List<Package> projectAllPackage(Long projectId);
    List<UserPackageVO> userAllPackage(Long userId);
    List<UserPackageVO> checkAllPackage(Long userId);
    List<PackageInfoVO> showPackageInfoVO(String info);

    List<Package> selectFinishByProject(Long projectId);

    Result grabPackage(Long projectId);
    List<Package> selectUnFinishPackage(Long id, Long projectId);
    Result changeStatus(Long packageId, Integer status);
    Result commitToCheck(Long packageId);
    Result commitToExam(Long packageId);

    Result passExam(Long packageId);

    Result repulseToRemark(Long packageId);

    Result repulseToCheck(Long packageId);

    Result releasePackage(Long packageId);
}
