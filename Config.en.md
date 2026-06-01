# Configuration Guide

This document explains the main configuration items in `application-dev.yml`.

## Mail Settings

Update these values if you want the system to send account information by email after a user is created.

```yaml
spring:
  mail:
    host: smtp.qq.com
    username: 123@qq.com
    password: 123
```

## Application Settings

### 1. Cache timeout settings

Timeout values are in minutes. The defaults are acceptable for most deployments.

```yaml
graph:
  cache:
    token: default
    timeout:
      user: 60
      license: 1440
      domain: 1440
```

### 2. Admin login account

You should always change the default admin username and password.

```yaml
graph:
  userName: root
  password: 123456
```

### 3. Invitation-enabled account types

This field defines which `configs.appName` entries can be used for self-service registration.

```yaml
graph:
  invite: mjj,mjj2
```

### 4. Microsoft Graph application settings

These values are required.

```yaml
graph:
  configs:
  - appName: mjj
    appId: mjj
    appTenant: mjj
    appSecret: mjj
    admin: mjj
  - appName: mjj2
    appId: mjj2
    appTenant: mjj2
    appSecret: mjj2
    domain: mjj2
    admin: mjj2
```

Field notes:

- `appName`: internal tenant label shown in the UI
- `appId`: Azure application client ID
- `appTenant`: Azure tenant ID
- `appSecret`: Azure application client secret
- `admin`: optional global admin account

### 5. Subscription name mapping

This section is used to map Microsoft SKU identifiers to friendly names shown in the UI.

```yaml
graph:
  subscribed:
  - skuName: STANDARDWOFFPACK_STUDENT
    displayName: A1 Student
    skuId: 314c4481-f395-4525-be8b-2ec4bb1e9d91
  - skuName: STANDARDWOFFPACK_FACULTY
    displayName: A1 Faculty
    skuId: 94763226-9b3c-4e75-a931-5c89701abe66
  - skuName: OFFICE_365_A1_PLUS_FOR_STUDENT
    displayName: A1 Plus Student
    skuId: e82ae690-a2d5-4d76-8d30-7c6e01e6022e
  - skuName: OFFICE_365_A1_PLUS_FOR_FACULTY
    displayName: A1 Plus Faculty
    skuId: 78e66a63-337a-4a9a-8959-41c6654dfb56
  - skuName: M365EDU_A3_STUUSEBNFT_RPA1
    displayName: A3 Unattended
    skuId: 1aa94593-ca12-4254-a738-81a5972958e8
  - skuName: Office_365_E3Y
    displayName: E3Y
    skuId: 6fd2c87f-b296-42f0-b197-1e91e994b900
  - skuName: DEVELOPERPACK_E5
    displayName: E5 Developer Subscription
    skuId: c42b9cae-ea4f-4ab7-9717-81576235ccac
```

You can add or rename entries to match the subscriptions available in your tenant.
