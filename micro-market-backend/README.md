# micro-market

#### 介绍
微信商城的后端服务。基于Spring Boot

#### 软件架构

#### 安装教程

#### 使用说明

1.  将该项目下载到本地后，使用IDEA打开。
2.  项目打开后，使用Maven下载相关依赖
3.  依赖下载完成后，将项目的image压缩包。解压到路径。 E:\images\micro_mart\images
4.  解压文件后确保bigTypeImages，productImages，productIntroImages，productParaImages，productSwiperImages，swiperImages这几个文件夹都处于该目录下。
5.  用户也可以到项目的WebAppConfigurer类下，进行模拟静态资源服务器的自定义配置。路径为：src/main/java/com/micromarket/config/WebAppConfigurer.java
6.  本项目使用MySql作为数据库。Sql文件也在image目录下。
7.  若想要使用手机访问微信小程序，需要将将产品表的productIntroImgs和productParaImgs的ip更改为自己的ip，可参考下面的两句sql。
8.  UPDATE t_product SET productIntroImgs = REPLACE(productIntroImgs,'192.168.43.246','YOUR IP ADDRESS') WHERE productIntroImgs LIKE '%192.168.43.246%';
9.  UPDATE t_product SET productParaImgs = REPLACE(productParaImgs,'192.168.43.246','YOUR IP ADDRESS') WHERE productIntroImgs LIKE '%192.168.43.246%';  

#### 参与贡献
