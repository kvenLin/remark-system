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

## 项目所遇问题和解决方案
* 问题: 当每次项目完成进行验收时,验收员会将该项目的文件进行导出,这时会出现网站卡顿现象
* 原因检测:
> 检测发现网络正常,日志信息也正常,也没有达到并发量的瓶颈,
最后进行GC的监控发现是因为服务器进行的FullGC时间过长,导致网站服务卡顿.
* 为什么会发生长时间的FullGC?
> 因为在数据量很大的项目进行后生成的WorkBook文件对象会特别大,大对象会直接存放在老年代,
并且堆内存设置的非常大,所以进行FullGC的时间会很长.
* 解决思路:
> 减小堆内存
* 解决方案:
> 单机部署多个服务应用,每个web容器的堆内存指定为2G,虽然会造成一定资源的浪费,
但是相对是比较合理的解决方案.
同时为了每个应用服务都能被访问到,使用nginx进行ip_hash路由.

![参考图](https://raw.githubusercontent.com/kvenLin/remark-system/master/src/main/resources/images/选区_034.png)
