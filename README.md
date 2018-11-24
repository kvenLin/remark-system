# remark-system

## 功能简介
* 制定规则:
    * 包括表格的格式
    * 标注形式:选择或文字回答
    * 数据标注要求: 数据包大小,参与的成员
* 用户管理:
    * 用户的增删查改
    * 用户和项目的分配
* 数据包的标注,质检,验收,打回
* 根据规则动态的导入导出表格数据

## 使用说明
* clone该项目
* 在本地数据库导入sql文件: [sql文件路径](https://github.com/kvenLin/remark-system/blob/master/src/main/resources/sql/remarksystem.sql)
* 修改application.properties中的配置信息:
    * 数据库用户和密码
    * 根据需要修改jwt.secret和jwt.expiration
* 启动项目
* 打开前端界面: resources/build/index.html
## 界面展示
* 登录

![登录界面](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_003.png)

* 用户管理

![用户管理](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_004.png)

* 项目管理

![项目管理](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_005.png)

* 数据包管理

![数据包管理](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_006.png)

* 用户查看参与的项目

![查看参与项目](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_007.png)