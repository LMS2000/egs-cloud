# SQL-SDK使用 

首先我们需要下载系统提供的SDK的jar包，我们可以去[`gihub`地址](https://github.com/LMS2000/sql-generator) 下载我们的SDK项目，或者直接点击[下载链接](	https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/sql-generator/sql-generator-v1.0.jar)



得到`Jar`包之后，我们需要使用`maven`命令将`jar`包放到自己的本地`Maven`仓库里。

```shell
mvn install:install-file -Dfile=sql-generator-v1.0.jar -Dmaven.repo.local=本地仓库路径 -DgroupId=com.lms -DartifactId=sql-generator -Dversion=v1.0 -Dpackaging=jar
```



通过YML 使用

在YML配置中使用：

```yaml
sqltool:
 client: 
  accessKey: xxxxx
  secretKey: xxxxx
```



自己创建：

```java
  SqlToolApiClient client=new SqlToolApiClient(accessKey, secretKey)
```



使用接口

```java
  SqlToolApiClient client=new SqlToolApiClient();
        client.setAccessKey("47c0d9485f5fa50aaab162bef5fae201");
        client.setSecretKey("c516ab4c5f843d82da3c7c896b46ad7a");
        LmsRequest lmsRequest=new LmsRequest();
        lmsRequest.setSql("CREATE TABLE `report` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',\n" +
                "  `content` text NOT NULL COMMENT '内容',\n" +
                "  `type` int(11) NOT NULL COMMENT '举报实体类型（0-词库）',\n" +
                "  `reported_id` bigint(20) NOT NULL COMMENT '被举报对象 id',\n" +
                "  `reported_user_id` bigint(20) NOT NULL COMMENT '被举报用户 id',\n" +
                "  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态（0-未处理, 1-已处理）',\n" +
                "  `user_id` bigint(20) NOT NULL COMMENT '创建用户 id',\n" +
                "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='举报';");
        System.out.println(client.randomData(lmsRequest));
```





# 代码生成器生成功能

每个用户都可以制作代码生成器和使用代码生成器。



### 制作代码生成器



#### 用户首先进入创建生成器页面



**填写基本信息**

![image-20240309135606634](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309135606634.png)



- 名称(必填)

名字为生成器的名称只能用英文，不可以包含特殊符号空格(){}[] / 等。

- 描述(选填)

代码生成器的描述，说明生成器的主要功能

- 基本包(必填)

格式为xxx.xxx不可以包含中文或者特殊符号，如com.lms  错误示例：com_.l/m s

- 版本(必填)

格式为xxx.xxx  必须是数字或者字符  如1.0.0  v1.0.0 

- 作者(必填)

项目的作者

- 标签(必填)

指定项目的标签方便在项目列表中查询

- 图片

项目的封面

**填写模型信息**

![image-20240309140507602](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309140507602.png)

项目的模型信息是生成器需要的一些开启选项。

比如说后端项目常见的跨域配置，数据库配置，swagger文档配置，es/redis配置等等

我们都可以使用一些命令来开启或者说关闭，如 generate --nc true 来开启跨域 --nm jdbc:mysql://localhost:3306/my_db  配置数据库地址，这些工作不需要我们去修改，而是使用生成器快速帮助我们把这个项目改成我所需要的项目。



- 字段名称(必填)

开启配置的是否生成或者填写指定值

- 描述

这个配置的描述，是用来干什么的

- 类型(必填)

目前只支持boolean和String 类型

- 默认值(必填)

这个配置的默认值，如果是boolean就是true或者false。

- 缩写(选填)

用来使用短命令 如-- needCors 太长了可以使用-nc 

**文件配置**



![image-20240309144641569](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309144641569.png)

文件配置用来设置需要导出的文件。



- 输入路径(必填)

需要生成的问题路径

- 输出路径(必填)

生成文件的输出路径

- 类型(必填)

文件的类型，分为文件或者文件夹

- 生成类型(必填)

生成类型分为静态和动态，静态=只会生成或者不生成，动态则会根据模型配置将指定的代码挖掘出来可以让用户填写

- 条件(必填)

指定模型配置的字段，满足条件的时候才会进行生成操作



**生成器的制作**

![image-20240309151502191](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309151502191.png)



![image-20240309151601809](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309151601809.png)

用户可以上传制作这次生成器的项目模板(注意不要打包外层的文件夹)

如选择项目里面的这一层目录进行压缩为**ZIP格式**的压缩包：

![image-20240309151714854](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309151714854.png)

然后点击上传，系统会根据前面填写的模型配置和文件配置和上传的项目模板生成生成器项目代码，用户下载得到生成器代码后，可以选择上传生成器代码。然后点击提交就完成了创建。



### 使用代码生成器



![image-20240309152211067](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309152211067.png)



从生成器列表进入到指定的生成器详细页，我们可以选择点赞收藏，使用，下载编辑，删除

**注意**：这些功能只有登录之后才可以使用。并且编辑和删除只有管理员或者创建的用户才可以操作

用户可以点击**立即使用**来在线使用也可以点击**下载** 来下载生成器代码



**立即使用**



![image-20240309153240462](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240309153240462.png)

点击立即使用之后，跳转到在线使用的页面，用户需要填写生成的选项然后点击**生成代码** 系统会根据用户的选项生成代码给用户下载

 

**下载**

系统直接将代码生成器给用户下载来使用



# 生成器制作工具的使用

在线的代码生成器制作功能可能不是很好用，为了更好的制作生成器系统提供了一个生成器的代码包。我们可以下载使用，制作完将制作完成的`meta.json`中关于模型配置还有文件配置的`json`文件可以在线导入。具体使用在[工具地址](https://github.com/LMS2000/lmszi-generator-maker)内有使用说明

![image-20240320172518665](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areapic_go_areaimage-20240320172518665.png)





我们可以使用在线制作工具的导入功能将我们本地制作的生成器的模型配置的`JSON` 导入

![image-20240320171639768](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240320171639768.png)







![image-20240320171703708](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240320171703708.png)



同样也可以将文件配置的`JSON` 导入

![image-20240320171724192](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240320171724192.png)



![image-20240320171745504](https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/pic_go_areaimage-20240320171745504.png)





