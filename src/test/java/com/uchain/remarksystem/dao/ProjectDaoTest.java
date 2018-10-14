package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.service.PackageService;
import com.uchain.remarksystem.service.ProjectService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectDaoTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PackageService packageService;
    @Test
    public void createMock(){
//        for (int i = 0; i < 30; i++) {
//            Project project = new Project();
//            project.setName("hello world");
//            project.setCreatedBy(1L);
//            project.setUpdatedBy(1L);
//            projectService.insert(project);
//        }
    }

    @Test
    public void createPackage(){
//        for (int i = 0; i < 10; i++) {
//            Package aPackage = new Package();
//            aPackage.setProjectId(17L);
//            aPackage.setUpdateTime(new Date());
//            aPackage.setStartTime(new Date());
//            aPackage.setUserId(15L);
//            packageService.insert(aPackage);
//        }
    }

}
