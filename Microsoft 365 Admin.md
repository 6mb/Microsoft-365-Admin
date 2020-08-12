 <h1 class="curproject-name"> Microsoft 365 Admin </h1> 


# Microsoft 365 首页

## GET 首页展示
<a id=GET 首页展示> </a>

### 基本信息

**Path：** /microsoft/365/homePage

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |

### 返回数据

```javascript
{
    "status": 200,
    "message": "操作成功",
    "data": {
        "statisticsVo": {
            "productSubs": 1, // 产品订阅数
            "licenses": 5000, // 许可证数
            "allocatedLicenses": 4969, //已分配许可证 
            "availableLicenses": 31, //可用许可证
            "users": 4992, // 总用户数
            "allowedUsers": 4988, //允许登陆用户数
            "biddenUsers": 4, //禁止登陆用户数
            "unauthorizedUsers": 0 //未授权用户数
        },
        "noLandingUsers": [ // 禁止登陆用户
            {
                "userId": "b28945cc-8f8f-4dcf-a300-11dbea59e434", //用户ID
                "skuId": null,
                "skuType": null,
                "domain": null,
                "surname": null,
                "givenName": null,
                "displayName": "Info", // 显示名称
                "mailNickname": "info", 
                "userPrincipalName": "xxx@xxx.com", //邮箱
                "password": null,
                "passwordPolicies": "DisablePasswordExpiration, DisableStrongPassword",
                "mobilePhone": null,
                "mail": null,
                "companyName": null,
                "usageLocation": "CN", //地区
                "streetAddress": null,
                "city": null,
                "state": null,
                "country": null,
                "accountEnabled": false,
                "createdDateTime": "2020-05-28T14:34:04Z", //创建时间
                "appName": null,
                "needDeleted": null,
                "displayAccountEnable": "禁用", //状态
                "top": 999,
                "skuVos": null //订阅 
            },
            
        ],
        "unauthorizedUsers": null
    }
}
```
## GET 刷新缓存
<a id=GET 刷新缓存> </a>
### 基本信息

**Path：** /microsoft/365/refresh

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织名称 |
| type | 否  |  0 |  刷新类型：【1：用户；2：订阅；3：域名】 |

### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```
# Microsoft 365 订阅管理

## GET 许可统计
<a id=GET 许可统计> </a>
### 基本信息

**Path：** /microsoft/365/getLicenseStatistics

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |

### 返回数据

```javascript
{
  "status": 200,
  "message": "操作成功",
  "data": {
    "productSubs": 1, // 产品订阅数
    "licenses": 5000, // 许可证数
    "allocatedLicenses": 4969, //已分配许可证 
    "availableLicenses": 31, //可用许可证
    "users": 0,
    "allowedUsers": 0,
    "biddenUsers": 0,
    "unauthorizedUsers": 0
  }
}
```
## GET 许可证列表查询
<a id=GET 许可证列表查询> </a>
### 基本信息

**Path：** /microsoft/365/listLicense

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |
| skuId | 否  |  94763226-9b3c-4e75-a931-5c89701abe66 |  订阅ID |

### 返回数据

```javascript
{
    "status": 200,
    "message": "操作成功",
    "data": [
        {
            "appName": "LAZY_EDU", // 组织类型
            "id": "afa95aab-2f8d-4f57-a568-834179d069af_94763226-9b3c-4e75-a931-5c89701abe66",
            "skuId": "94763226-9b3c-4e75-a931-5c89701abe66", // skuId
            "capabilityStatus": "Enabled",
            "displayStatus": "启用", // 状态
            "consumedUnits": 4969, // 已分配许可，【可用许可= 启用- 已分配】
            "skuPartNumber": "STANDARDWOFFPACK_FACULTY", // 唯一 SKU 显示名称
            "skuName": "A1 教师版", // 自定义订阅名称
            "enabled": 5000, // 启用数量
            "suspended": 0, // 已挂起数量
            "warning": 0 // 警告数量
        }
    ]
}
```
# Microsoft 365 用户管理

## GET 用户统计
<a id=GET 用户统计> </a>
### 基本信息

**Path：** /microsoft/365/getUsersStatistics

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |

### 返回数据

```javascript
{
  "status": 200,
  "message": "操作成功",
  "data": {
    "productSubs": 0,
    "licenses": 0,
    "allocatedLicenses": 0,
    "availableLicenses": 0,
    "users": 4992, // 总用户数
    "allowedUsers": 4988, //允许登陆用户数
    "biddenUsers": 4, //禁止登陆用户数
    "unauthorizedUsers": 0 //未授权用户数
  }
}
```
## GET 查询用户信息列表
<a id=GET 查询用户信息列表> </a>
### 基本信息

**Path：** /microsoft/365/listUsers

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |
| skuType | 否  |  @string |  订阅类型 |
| domain | 否  |  @string |  域名 |
| displayName | 否  |  @string |  显示名称 |
| userPrincipalName | 否  |  @string |  用户名 |
| accountEnabled | 否  |  false |  是否启用 |
| pageIndex | 是  |  0 |  当前页码 |
| pageSize | 是  |  0 |  每页数量 |

### 返回数据

```javascript


{
    "status": 200,
    "message": "操作成功",
    "data": {
        "total": 1,
        "list": [
            {
                "userId": "1994e87b-3e13-434a-ad09-71bbe472a478", //用户ID
                "skuId": null,
                "skuType": null,
                "domain": null,
                "surname": "仁",
                "givenName": "吴",
                "displayName": "吴仁", //显示名称
                "mailNickname": "wuren",
                "userPrincipalName": "wuren@xxx.com", // 邮箱账户
                "password": null,
                "passwordPolicies": "DisablePasswordExpiration, DisableStrongPassword",
                "mobilePhone": null,
                "mail": null,
                "companyName": null,
                "usageLocation": "HK", //地区
                "streetAddress": "乐清市 智仁乡",
                "city": "温州市",
                "state": "温州市",
                "country": "中国",
                "accountEnabled": true,
                "createdDateTime": "2020-05-30T07:09:57Z", //创建时间
                "appName": null,
                "needDeleted": null,
                "displayAccountEnable": "启用", //状态
                "top": 999,
                "skuVos": [ //订阅信息
                    {
                        "skuId": "94763226-9b3c-4e75-a931-5c89701abe66",
                        "skuType": "STANDARDWOFFPACK_FACULTY", // 唯一 SKU 显示名称
                        "skuName": "A1 教师版" // 自定义订阅名称
                    }
                ]
            }
        ]
    }
}
```
## GET 查询用户信息：详情
<a id=GET 查询用户信息：详情> </a>
### 基本信息

**Path：** /microsoft/365/getUser

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织名称 |
| userId | 否  |  wuren@xxx.com |  账户/用户ID |

### 返回数据

```javascript
{
    "status": 200,
    "message": "操作成功",
    "data": {
        "userId": "1994e87b-3e13-434a-ad09-71bbe472a478", //用户ID
        "skuId": null,
        "skuType": null,
        "domain": null,
        "surname": "仁",
        "givenName": "吴",
        "displayName": "吴仁", //显示名称
        "mailNickname": "wuren",
        "userPrincipalName": "wuren@xxx.com", // 邮箱账户
        "password": null,
        "passwordPolicies": "DisablePasswordExpiration, DisableStrongPassword",
        "mobilePhone": null,
        "mail": null,
        "companyName": null,
        "usageLocation": "HK", //地区
        "streetAddress": "乐清市 智仁乡",
        "city": "温州市",
        "state": "温州市",
        "country": "中国",
        "accountEnabled": true,
        "createdDateTime": "2020-05-30T07:09:57Z", //创建时间
        "appName": null,
        "needDeleted": null,
        "displayAccountEnable": "启用", //状态
        "top": 999,
        "skuVos": [ //订阅信息
            {
                "skuId": "94763226-9b3c-4e75-a931-5c89701abe66",
                "skuType": "STANDARDWOFFPACK_FACULTY", // 唯一 SKU 显示名称
                "skuName": "A1 教师版" // 自定义订阅名称
            }
        ]
    }
}
```
## GET 查询绑定域名
<a id=GET 查询绑定域名> </a>
### 基本信息

**Path：** /microsoft/365/getDomains

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| appName | 是  |  @string |  组织类型 |

### 返回数据

```javascript
{
    "status": 200,
    "message": "操作成功",
    "data": [
        {
            "id": "lazyedu.onmicrosoft.com", //域名
            "isDefault": false,
            "displayIsDefault": "否", //是否是默认域名
            "isRoot": true,
            "displayIsRoot": "是" //是否是根域名
        }
    ]
}
```
## POST 添加账号
<a id=POST 添加账号> </a>
### 基本信息

**Path：** /microsoft/365/addUser

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| appName | text  |  是 |  @string  |  组织类型 |
| needDeleted | text  |  否 |  false  |  是否需要删除一个许可证 |
| skuType | text  |  是 |  @string  |  订阅类型 |
| domain | text  |  是 |  @string  |  域名 |
| displayName | text  |  是 |  @string  |  显示名称 |
| mailNickname | text  |  是 |  @string  |  邮箱前缀 |
| password | text  |  是 |  @string  |  密码 |
| mail | text  |  否 |  @string  |  邮箱 |
| companyName | text  |  否 |  @string  |  公司名 |
| usageLocation | text  |  否 |  @string  |  位置 |
| streetAddress | text  |  否 |  @string  |  街道地址 |
| city | text  |  否 |  @string  |  城市 |
| state | text  |  否 |  @string  |  省 |
| country | text  |  否 |  @string  |  国家 |



### 返回数据

```javascript
{
    "status": 200,
    "message": "操作成功",
    "data": {
        "userId": "b41570a4-5f81-46da-a3f6-d67672573ce2",
        "skuId": "94763226-9b3c-4e75-a931-5c89701abe66",
        "skuType": null,
        "domain": null,
        "surname": null,
        "givenName": null,
        "displayName": "app122", //显示名称
        "mailNickname": "app122",
        "userPrincipalName": "app122@xxx.com", //邮箱账户
        "password": "xxxx", //密码
        "passwordPolicies": "DisablePasswordExpiration, DisableStrongPassword",
        "mobilePhone": null,
        "mail": null,
        "companyName": null,
        "usageLocation": "CN",
        "streetAddress": null,
        "city": null,
        "state": null,
        "country": "中国",
        "accountEnabled": true,
        "createdDateTime": null
    }
}
```
## POST 删除用户信息
<a id=POST 删除用户信息> </a>
### 基本信息

**Path：** /microsoft/365/deletedUser

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| appName | text  |  是 |  @string  |  组织名称 |
| userId | text  |  否 |  @string  |  用户ID/账户邮箱 |



### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```
## POST 添加订阅
<a id=POST 添加订阅> </a>
### 基本信息

**Path：** /microsoft/365/addLicense

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| appName | text  |  是 |  @string  |  组织类型 |
| userId | text  |  是 |  @string  |  用户ID/账户邮箱 |
| skuId | text  |  是 |  @string  |  许可Id |



### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```
## POST 取消订阅
<a id=POST 取消订阅> </a>
### 基本信息

**Path：** /microsoft/365/cancelLicense

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| appName | text  |  是 |  @string  |  组织类型 |
| userId | text  |  是 |  @string  |  用户ID/账户邮箱 |
| skuId | text  |  是 |  @string  |  许可ID |



### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```
## POST 启用、禁用账户
<a id=POST 启用、禁用账户> </a>
### 基本信息

**Path：** /microsoft/365/enableDisableUser

**Method：** POST

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| appName | text  |  是 |  @string  |  组织类型 |
| userId | text  |  是 |  @string  |  用户ID/账户邮箱 |
| accountEnabled | text  |  是 |  false  |  启用/禁用 |



### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```
## GET 批量创建用户信息
<a id=GET 批量创建用户信息> </a>
### 基本信息

**Path：** /microsoft/365/createUserBatch

**Method：** GET

**接口描述：**


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/json | 是  |   |   |
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| num | 是  |  0 |  创建数量 |
| appName | 是  |  @string |  组织名称 |
| skuName | 是  |  @string |  订阅 |

### 返回数据

```javascript
{
   "status": 200,
   "message": "操作成功",
   "data": null
}
```