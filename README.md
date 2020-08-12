# Microsoft-365-Admin
 
![https://img.shields.io/badge/license-MIT-blue.svg?style=flat-square](https://img.shields.io/badge/license-MIT-blue.svg?longCache=true&style=flat-square)
![https://img.shields.io/badge/springboot-2.0.6-orange.svg?style=flat-square](https://img.shields.io/badge/springboot-2.1.3-yellow.svg?longCache=true&style=popout-square)

此项目是一个基于API的 Microsoft 365 管理平台, 支持订阅查询，用户管理（查询，新增，删除，分配许可证等），支持批量创建用户，批量删除用户. 支持多账户管理.

后端基于SpringBoot，使用 msal4j 和 microsoft-graph sdk 开发完成

前端？**嗯，能力不足，恳请各位大佬帮忙支持一下**

<font color="#ff0000">【注意】该项目只完成了后端接口开发，暂无界面，需要直接调用接口</font>

## 系统功能

- 总览：订阅数，许可证数，可用许可证数，用户数，允许登陆用户数，禁止登陆用户数等信息展示
- 许可查询：查询每个订阅的许可证信息
- 用户管理：查询，新增，删除，分配许可证等
- 多账户切换

## API接口

[接口文档](https://github.com/6mb/Microsoft-365-Admin/blob/master/Microsoft%20365%20Admin.md)

- Microsoft 365 首页
    - 首页展示
    - 刷新缓存
- Microsoft 365 订阅管理
    - 许可统计
    - 许可证列表查询
- Microsoft 365 用户管理
    - 查询用户统计
    - 查询用户信息列表
    - 查询用户信息详情
    - 查询绑定域名
    - 添加账号
    - 删除账户
    - 添加订阅
    - 取消订阅
    - 启用账户
    - 禁用账户
    - 批量创建用户信息
## Postman 接口

 [点击下载](https://raw.githubusercontent.com/6mb/Microsoft-365-Admin/master/.github/Microsoft%20365%20Admin.postman_collection.json)

请自行设置 .evn  {host} 和 {port}

![image](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/接口.png)


## 原型文档

[原型截图](https://github.com/6mb/Microsoft-365-Admin/blob/master/Azure.md)

具体文稿，请下载 [Microsoft 365 Admin.rp](https://raw.githubusercontent.com/6mb/Microsoft-365-Admin/master/.github/Microsoft%20365%20Admin.rp) 文件

## 