# Microsoft 365 Admin API Reference

This document is the English API companion for the original Chinese reference in [Microsoft 365 Admin.md](./Microsoft%20365%20Admin.md).

## Overview

Base path groups:

- `/microsoft/365`: authenticated admin APIs
- `/microsoft/code`: invitation code management
- `/front`: self-service registration APIs
- `/system`: system monitor APIs

Common response format:

```json
{
  "status": 200,
  "message": "操作成功",
  "data": {}
}
```

## Dashboard

### GET `/microsoft/365/homePage`

Returns dashboard statistics and key user lists.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |

### GET `/microsoft/365/refresh`

Refreshes cached Graph data.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `type` | No | Cache type: `1=user`, `2=license`, `3=domain` |

## License Management

### GET `/microsoft/365/getLicenseStatistics`

Returns license statistics for one tenant.

### GET `/microsoft/365/listLicense`

Returns license or subscription entries for one tenant.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `skuId` | No | Filter by a specific SKU ID |

## User Management

### GET `/microsoft/365/getUsersStatistics`

Returns user totals, enabled and disabled counts, and unlicensed user counts.

### GET `/microsoft/365/listUsers`

Returns a paginated list of users.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `skuType` | No | Filter by SKU type |
| `domain` | No | Filter by domain |
| `displayName` | No | Filter by display name |
| `userPrincipalName` | No | Filter by sign-in name |
| `accountEnabled` | No | Filter by account status |
| `pageIndex` | Yes | Current page index |
| `pageSize` | Yes | Page size |

### GET `/microsoft/365/getUser`

Returns details for one user.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID or email address |

### GET `/microsoft/365/getDomains`

Returns verified domains for one tenant.

### POST `/microsoft/365/addUser`

Creates a new user.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `needDeleted` | No | Whether another licensed user should be removed first |
| `skuType` | Yes | Subscription type |
| `domain` | Yes | Email domain |
| `displayName` | Yes | Display name |
| `mailNickname` | Yes | Mail prefix |
| `password` | Yes | Initial password |
| `mail` | No | Notification email |
| `companyName` | No | Company name |
| `usageLocation` | No | Usage location |
| `streetAddress` | No | Street address |
| `city` | No | City |
| `state` | No | State or province |
| `country` | No | Country |

### POST `/microsoft/365/deletedUser`

Deletes a user.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID or email address |

### POST `/microsoft/365/addLicense`

Assigns a license to a user.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID or email address |
| `skuId` | Yes | License SKU ID |

### POST `/microsoft/365/cancelLicense`

Removes a license from a user.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID or email address |
| `skuId` | Yes | License SKU ID |

### POST `/microsoft/365/enableDisableUser`

Enables or disables a user account.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID or email address |
| `accountEnabled` | Yes | `true` to enable, `false` to disable |

### GET `/microsoft/365/createUserBatch`

Starts a bulk user creation task.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `num` | Yes | Number of users to create |
| `appName` | Yes | Tenant or organization name |
| `skuName` | Yes | Subscription or license name |

### POST `/microsoft/365/deletedUserBatch`

Deletes users in batch.

### GET `/microsoft/365/listRoles`

Returns available directory roles.

### POST `/microsoft/365/addDirectoryRoleMember`

Adds a user to a directory role.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `userId` | Yes | User ID |
| `roleId` | Yes | Role template ID |

## Invitation Code Management

### GET `/microsoft/code/getCodeStatistics`

Returns invitation code statistics.

### GET `/microsoft/code/list`

Returns a paginated invitation code list.

### POST `/microsoft/code/generate`

Generates invitation codes.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `num` | Yes | Number of codes to generate |

## Self-Service Registration

### GET `/front/listLicense`

Returns the list of invitation-enabled licenses.

### GET `/front/listUsageLocation`

Returns available usage locations for one tenant.

Query parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |

### POST `/front/create`

Creates a self-service registration account with an invitation code.

Body parameters:

| Name | Required | Description |
| --- | --- | --- |
| `appName` | Yes | Tenant or organization name |
| `displayName` | Yes | Display name |
| `skuId` | Yes | License SKU ID |
| `mailNickname` | Yes | Mail prefix |
| `password` | Yes | Initial password |
| `mailbox` | No | Notification email |
| `code` | Yes | Invitation code |
| `usageLocation` | No | Usage location |

## System Monitor

### GET `/system/monitor`

Returns basic JVM and operating system information for the current deployment.

## Postman

You can also use the bundled Postman collection:

- [Microsoft 365 Admin.postman_collection.json](https://raw.githubusercontent.com/6mb/Microsoft-365-Admin/master/.github/Microsoft%20365%20Admin.postman_collection.json)

Before using the collection, set `{host}` and `{port}` in your Postman environment.
