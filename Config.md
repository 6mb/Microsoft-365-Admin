### 邮件配置

该配置用于发送邮件功能，配置后新增账号时填写邮箱，即可将账号密码发送至该邮箱
```yaml
spring:
  mail: # 邮件配置，自行修改
    host: smtp.qq.com # smtp.qq.com
    username: 123@qq.com # 发送邮箱地址
    password:  123 # 发送邮箱密码
```

### 程序自定义配置(重点)

1、缓存超时时间配置，单位分钟：用户过期用户账号，许可证，绑定域名，默认即可
```yaml
graph:
  cache:
    token: default # token 缓存方式默认内存，redis 方式 需要配置 redis
    timeout: # 缓存超时时间  单位分钟
      user: 60 # 默认一小时
      license: 1440 # 默认一天
      domain: 1440 # 默认一天
```

2、后台登陆账号密码配置
```yaml
graph:
  userName: root # 登陆账户,自行修改
  password: 123456 # 登陆密码，自行修改
```

3、可以被用户注册的账号类型，为下面 configs.appName 的配置（必填，','分隔）
```yaml
graph:
  invite: mjj,mjj2
```

4、Microsoft API 配置（重点）
```yaml
graph:
  configs:
  - appName: mjj # 自定义该账号的类型（建议使用英文：默认域名前缀，必填）
    appId: mjj # 应用程序(客户端) ID client（必填）
    appTenant: mjj # 目录(租户) tenant（必填）
    appSecret: mjj # secrets（必填）
    admin: mjj # 全局管理员账号（可以为空）
  - appName: mjj2 # 账号2配置，如果不需要请删除
    appId: mjj2
    appTenant: mjj2
    appSecret: mjj2
    domain: mjj2
    admin: mjj2
```

5、订阅配置，用于转换订阅显示的名称，可自行按照订阅添加
```yaml
graph:
  subscribed: # 订阅类型，自行添加其他
  - skuName: STANDARDWOFFPACK_STUDENT
    displayName: A1 学生版
    skuId: 314c4481-f395-4525-be8b-2ec4bb1e9d91
  - skuName: STANDARDWOFFPACK_FACULTY
    displayName: A1 教师版
    skuId: 94763226-9b3c-4e75-a931-5c89701abe66
  - skuName: OFFICE_365_A1_PLUS_FOR_STUDENT
    displayName: A1P 学生版
    skuId: e82ae690-a2d5-4d76-8d30-7c6e01e6022e
  - skuName: OFFICE_365_A1_PLUS_FOR_FACULTY
    displayName: A1P 教师版
    skuId: 78e66a63-337a-4a9a-8959-41c6654dfb56
  - skuName: M365EDU_A3_STUUSEBNFT_RPA1
    displayName: A3 无人值守版
    skuId: 1aa94593-ca12-4254-a738-81a5972958e8
  - skuName: Office_365_E3Y
    displayName: E3Y
    skuId: 6fd2c87f-b296-42f0-b197-1e91e994b900
  - skuName: DEVELOPERPACK_E5
    displayName: E5 开发者订阅
    skuId: c42b9cae-ea4f-4ab7-9717-81576235ccac

```
