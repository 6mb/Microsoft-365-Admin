# Microsoft-365-Admin

![License](https://img.shields.io/badge/license-GPLv2-blue?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.4.5-brightgreen?style=flat-square)

[中文 README](./README.md) | [English Config Guide](./Config.en.md) | [English API Reference](./Microsoft%20365%20Admin.en.md) | [English Prototype Notes](./Azure.en.md)

Microsoft-365-Admin is an API-based Microsoft 365 administration panel. It supports subscription statistics, user lifecycle management, bulk user creation and deletion, invitation codes, and switching between multiple Microsoft 365 organizations.

The backend is built with Spring Boot and integrates with Microsoft Graph.

## Features

- Dashboard summary for subscriptions, licenses, available licenses, total users, enabled users, and disabled users
- Subscription lookup and license usage statistics
- User management: query, create, delete, enable, disable, and assign or remove licenses
- Multi-tenant or multi-account switching
- Login protection through configurable admin credentials
- Invitation-code-based self-service registration
- Bulk user creation and bulk deletion
- User role elevation support

Note:
After operations such as creating users, deleting users, enabling or disabling accounts, or changing licenses, the page data may need a cache refresh before the latest results are shown.

## Required Microsoft Graph Permissions

### Create users

Permission type | Permissions
---|---
Delegated (work or school account) | `User.ReadWrite.All`, `Directory.ReadWrite.All`, `Directory.AccessAsUser.All`
Delegated (personal Microsoft account) | Not supported
Application | `User.ReadWrite.All`, `Directory.ReadWrite.All`

### Delete users

Permission type | Permissions
---|---
Delegated (work or school account) | `Directory.AccessAsUser.All`
Delegated (personal Microsoft account) | Not supported
Application | `User.ReadWrite.All`

### Assign or remove licenses

Permission type | Permissions
---|---
Delegated (work or school account) | `User.ReadWrite.All`, `Directory.ReadWrite.All`
Delegated (personal Microsoft account) | Not supported
Application | `User.ReadWrite.All`, `Directory.ReadWrite.All`

### Elevate user roles

Permission type | Permissions
---|---
Delegated (work or school account) | Depends on your tenant policy
Delegated (personal Microsoft account) | Not supported
Application | `RoleManagement.ReadWrite.Directory`

## Getting Started

### 1. Configuration

See the configuration guide:
[Config Guide (Chinese)](./Config.md)
[Config Guide (English)](./Config.en.md)

### 2. Run with Java

Download the latest jar package, then create a `config` directory beside it. Copy `src/main/resources/config/application-dev.yml` into that directory and update the values for your environment.

Start the application with:

```bash
java -jar microsoft-365-admin-1.6.2-RELEASE.jar --spring.profiles.active=dev
```

Default port: `8099`

### 3. Run with Docker

Copy `application-dev.yml` into your mounted `config` directory and update the values before starting the container.

```bash
docker pull logr/microsoft:latest

docker run -d --name=microsoft-admin \
    -p 8099:8099 \
    -v /home/microsoft/config:/config \
    -v /home/microsoft/db:/root/.graph/db \
    logr/microsoft:latest
```

If the frontend is deployed separately, update `src/main/resources/static/js/config.js` to point to the backend base URL.

## Screenshots

![Home](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/首页.png)
![Subscriptions](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/订阅管理.png)
![Users](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/用户管理.png)
![Invitations](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/邀请.png)
![Registration](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/申请.png)
![About](https://github.com/6mb/Microsoft-365-Admin/blob/master/.github/关于.png)

## Documentation

- [API Reference (Chinese)](./Microsoft%20365%20Admin.md)
- [API Reference (English)](./Microsoft%20365%20Admin.en.md)
- [Postman Collection](https://raw.githubusercontent.com/6mb/Microsoft-365-Admin/master/.github/Microsoft%20365%20Admin.postman_collection.json)
- [Prototype Notes (Chinese)](./Azure.md)
- [Prototype Notes (English)](./Azure.en.md)

Set the Postman environment values for `{host}` and `{port}` before sending requests.
