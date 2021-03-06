# FlappyMall
## A shopping mall backend development </br>

# Requirement for project:
Operating System: CentOS 6.8 64 bits、Windows7 64 bits </br>
jdk version 7u80 64 bits </br>
git version 2.8.0 </br>
Maven version 3.0.5 </br>
MySQL version mysql-server-5.1.73 </br>
Nginx version nginx-1.10.2.tar.gz </br>
vsftpd version vsftpd-2.2.2-21.el6.x86_64 </br>
Developing techs and version idea 15.0.6 </br>
Skils: SSM/Guava/Jackson/Joda/comment </br>
Languages: Java </br>

# Design Pattern:
## User Pattern
    -user login
    -user vertification</br>
    -user reigstration</br>
    -forget password</br>
    -turn in answer for question</br>
    -reset password</br>
    -obtain user information</br>
    -update user information log out</br>
    -Avoid user to obtain higher level information, such as admin and other order information</br>
    -use MD5 encryption to make the password private for other people and use guava as Cache</br>
    3)高复用服务响应对象的设计思想和封装</br>

## 分类模块
    递归算法</br>
    复杂对象排重</br>
    无限层级树结构设计</br>

## 商品模块
    POJO、BO、VO抽象模型</br>
    高效分页及动态排序</br>
    FTP服务对接、富文本上传</br>

## 购物车模块
    商品总价计算复用封装</br>
    高复用的逻辑方法封装思想</br>
    解决商业运算丢失精度的坑</br>

## 订单模块
    安全漏洞解决方案</br>
    订单号生成规则</br>
    强大的常量、枚举设计</br>

## 收货地址
    同步获取自增主键</br>
    数据绑定的对象绑定</br>
    越权问题升级巩固</br>

## 支付模块
    支付宝SDK源码解析</br>
    支付宝支付流程与集成</br>
    二维码生成，扫码支付</br>

## 线上部署
    云服务器vsftpd、nginx等配置</br>
    云服务器的配置与域名解析</br>
    发布上线注意事项</br>



