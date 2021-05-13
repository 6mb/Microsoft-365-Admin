let licenseList;
let domainList;
let usageLocationList;
let rolesList;
let delay = 2000;

$(window).on("load", function () {
    $('#titleName').html(" 用户管理 ");
    lightyear.loading('show');
    const obj = getParam();
    // 设置组织类型
    const success = setAppName();
    if (success) {
        // 统计信息
        getUsersStatistics();
        // 列表查询
        initUsersTable();
        // 许可证
        listLicense();
        listDomain();
        listUsageLocation();
        listRoles();
        lightyear.loading('hide');
    }
});


var getParam = function () {
    try {
        var url = window.location.href;
        var result = url.split("?")[1];
        var keyValue = result.split("&");
        var obj = {};
        for (let i = 0; i < keyValue.length; i++) {
            let item = keyValue[i].split("=");
            obj[item[0]] = item[1];
        }
        return obj;
    } catch (e) {

    }
};

function getUsersStatistics() {
    $.ajax({
        type: "get",
        url: path + "/365/getUsersStatistics",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                $("#users").text(r.data.users);
                $("#allowedUsers").text(r.data.allowedUsers);
                $("#biddenUsers").text(r.data.biddenUsers);
                $("#unauthorizedUsers").text(r.data.unauthorizedUsers);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}

function initUsersTable() {
    let usersTable = $('#usersTable');
    usersTable.GM({
        gridManagerName: 'usersTable',
        height: '60vh',
        skinClassName: 'gm-microsoft-365-skin',
        useWordBreak: true,
        ajaxData: (settings, params) => {
            // console.log(settings);
            let url = new URL(window.location.origin + "/microsoft/365/listUsers");
            url.search = new URLSearchParams(params).toString();
            return fetch(url).then(res => res.json());
        },
        query: {
            "appName": getAppName(),
            "skuId": getSelect("#license"),
            "accountEnabled": getSelect("#accountEnabled"),
            "assignLicense": getSelect("#assignLicense"),
            "displayName": getInput("#displayName"),
            "userPrincipalName": getInput("#userPrincipalName"),
        },
        ajaxType: 'GET',
        supportAjaxPage: true,
        currentPageKey: 'pageIndex',
        pageSizeKey: 'pageSize',
        pageSize: 10,
        responseHandler: function (response) {
            let list = response.data.list;
            let i = 0;
            for (let userI in list) {
                const user = list[userI];
                let skuNames = [];
                let roles = [];
                if (user.skuVos == null) {
                    continue;
                }
                for (let skuVo of user.skuVos) {
                    skuNames.push(skuVo.skuName);
                }
                for (let role of user.directoryRoles) {
                    roles.push(role.displayName);
                }
                list[i].skuNames = skuNames.join(', ');
                list[i].roles = roles.join(', ')
                i++;
            }
            response.totals = response.data.total;
            response.data = list;
            return response;
        },
        supportDrag: false,
        columnData: [
            {
                key: 'userId',
                text: '用户Id',
                isShow: false
            }, {
                key: 'userPrincipalName',
                text: '邮箱',
                width: '350px'
            }, {
                key: 'displayName',
                text: '姓名'
            }, {
                key: 'skuNames',
                text: '订阅'
            }, {
                key: 'displayAccountEnable',
                text: '状态',
                width: '70px'
            }, {
                key: 'roles',
                text: '角色'
            }, {
                key: 'usageLocation',
                text: '地区',
                width: '70px'
            }, {
                key: 'createdDateTime',
                text: '创建时间'
            }
        ],
        checkedAfter: function (checkedList, isChecked, rowData) {
            checkBoxClick()
        },
        ajaxSuccess: () => {
            lightyear.loading('hide');
        },
        ajaxError: (e) => {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }

    })
}

function listUsers() {
    GridManager.setQuery('usersTable', {
        "appName": getAppName(),
        "skuId": getSelect("#license"),
        "accountEnabled": getSelect("#accountEnabled"),
        "assignLicense": getSelect("#assignLicense"),
        "displayName": getInput("#displayName"),
        "userPrincipalName": getInput("#userPrincipalName"),
    }, true);
}


function listLicense() {
    $.ajax({
        type: "get",
        url: path + "/365/listLicense",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                licenseList = r.data;
                setLicense("license");
                setLicense("addLicenseSelect");
                setLicense("licenseSelectModalBatch");
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}

function listDomain() {
    $.ajax({
        type: "get",
        url: path + "/365/getDomains",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                domainList = r.data;
                setDomain("addDomainSelect");
                setDomain("domainSelectModalBatch");
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}

function listUsageLocation() {
    $.ajax({
        type: "get",
        url: path + "/365/listUsageLocation",
        dataType: "json",
        data: {
            "appName": getAppName()
        },
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                usageLocationList = r.data;
                setUsageLocation('addUsageLocation');
                setUsageLocation('usageLocationSelectModalBatch')
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}

function listRoles() {
    $.ajax({
        type: "get",
        url: path + "/365/listRoles",
        dataType: "json",
        data: {
            "appName": getAppName()
        },
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                rolesList = r.data;
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}

function addUserClick() {
    lightyear.loading('show');
    // 参数获取
    let displayName = getInput("#addDisplayName");
    let skuId = getSelect("#addLicenseSelect");
    let mailNickname = getInput("#addMailNickname");
    let domain = getSelect("#addDomainSelect");
    let password = getInput("#addPassword");
    let mailbox = getInput("#addMail");
    let usageLocation = getSelect("#addUsageLocation")
    // 参数校验


    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/addUser",
        data: {
            "appName": getAppName(),
            "displayName": displayName,
            "skuId": skuId,
            "mailNickname": mailNickname,
            "domain": domain,
            "password": password,
            "mailbox": mailbox,
            "usageLocation": usageLocation
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                let message = r.message;
                let password_error = "Error code: Request_BadRequestError message: The specified password does not comply with password complexity requirements. Please provide a different password.";
                let name_error = "Error code: Request_BadRequestError message: Another object with the same value for property userPrincipalName already exists.";
                if (password_error === message) {
                    message = "密码太简单啦，";
                }
                if (name_error === message) {
                    message = "该邮箱前缀已经被使用啦！";
                }
                lightyear.notify(message, 'danger', delay);
            } else {
                console.log(r);
                let userInfo = '名称：' + r.data.displayName + '<br>账号：' + r.data.userPrincipalName + '<br>密码：' + r.data.password;
                lightyear.loading('hide');
                $.alert({
                    title: '添加成功；请刷新缓存！',
                    content: '新增账号成功：<br><br><strong>' + userInfo + '</strong><br><br>',
                    buttons: {
                        confirm: {
                            text: '确认',
                            btnClass: 'btn-primary',
                            action: function () {
                                window.location.reload();
                            }
                        }
                    }
                });
            }
        },
        error: function () {
            lightyear.loading('hide');
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });

}

function addLicenseToUserWithSku(userId, skuId) {
    lightyear.loading('show');
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/addLicense",
        data: {
            "appName": getAppName(),
            "userId": userId,
            "skuId": skuId
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify("添加许可证成功！", 'success', delay)
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function cancelLicenseForUser(userId) {
    lightyear.loading('show');
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/cancelLicense",
        data: {
            "appName": getAppName(),
            "userId": userId
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify("取消许可证成功！", 'success', delay)
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function enableUserWithUserId(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/enableDisableUser",
        data: {
            "appName": getAppName(),
            "userId": userId,
            "accountEnabled": true
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('账户启用成功！', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function disableUserWithUserId(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/enableDisableUser",
        data: {
            "appName": getAppName(),
            "userId": userId,
            "accountEnabled": false
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('账户禁用成功！', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function deletedUserWithUserId(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/deletedUser",
        data: {
            "appName": getAppName(),
            "userId": userId
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('账户删除成功！', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function addUserBatchClick() {
    lightyear.loading('show');
    let num = $("#addUserBatchNum").val();
    let password = getInput("#passwordSelectModalBatch");
    let skuId = getSelect("#licenseSelectModalBatch");
    let domain = getSelect("#domainSelectModalBatch");
    let usageLocation = getSelect("#usageLocationSelectModalBatch");

    // 提交请求
    $.ajax({
        type: "get",
        url: path + "/365/createUserBatch",
        data: {
            "appName": getAppName(),
            "num": num,
            "skuId": skuId,
            "domain": domain,
            "password": password,
            "usageLocation": usageLocation
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('正在创建账户中，请等待！请根据根据创建数量比例等待，刷新界面', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function deletedUserBatchClick() {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/deletedUserBatch",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('账户删除成功！', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}

function addDirectoryRoleMember(userId, roleId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/365/addDirectoryRoleMember",
        data: {
            "appName": getAppName(),
            "userId": userId,
            "roleId": roleId
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('提权成功！', 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}


function setLicense(id) {
    // 先清空下数据
    $("#" + id).empty();
    $("#" + id).append("<option value=\"\" disabled selected hidden>请选择许可证</option>\n" +
        "                            <option value=\"\">不限制</option>");
    for (i in licenseList) {
        let option = "<option value=" + licenseList[i].skuId + ">" + licenseList[i].skuName + "</option>";
        $("#" + id).append(option);
    }
}

function setDomain(id) {
    $("#" + id).empty();
    $("#" + id).append("<option value=\"\" disabled selected hidden>请选择域名后缀</option>");
    for (i in domainList) {
        console.log(domainList[i].isDefault)
        if (domainList[i].isDefault) {
            let option = "<option selected='selected' value=" + domainList[i].id + ">" + domainList[i].id + "</option>";
            $("#" + id).append(option);
        } else {
            let option = "<option value=" + domainList[i].id + ">" + domainList[i].id + "</option>";
            $("#" + id).append(option);
        }
    }
}

function setUsageLocation(id) {
    let usageLocationSelect = $("#" + id);
    usageLocationSelect.empty();
    for (let i in usageLocationList) {
        let option = "<option value=" + usageLocationList[i] + ">" + usageLocationList[i] + "</option>";
        usageLocationSelect.append(option);
    }
}

function setRoles(id) {
    let roleSelect = $("#" + id);
    roleSelect.empty();
    for (let i in rolesList) {
        let option = "<option value=" + rolesList[i].id + ">" + rolesList[i].displayName + ' - ' + rolesList[i].description + "</option>";
        roleSelect.append(option);
    }
}

function searchUsers() {
    lightyear.loading('show');
    listUsers();
}


function checkBoxClick() {
    let checkBoxClickCount = GridManager.getCheckedData('usersTable').length;
    if (checkBoxClickCount >= 1) {
        $('#enableAccount').removeClass("disabled");
        $('#disableAccount').removeClass("disabled");
        $('#deletedAccount').removeClass("disabled");
        $('#addLicenseAccount').removeClass("disabled");
        $('#disLicenseAccount').removeClass("disabled");
        $('#addDirectoryRoleMember').removeClass("disabled");
    } else {
        $('#enableAccount').addClass("disabled");
        $('#disableAccount').addClass("disabled");
        $('#deletedAccount').addClass("disabled");
        $('#addLicenseAccount').addClass("disabled");
        $('#disLicenseAccount').addClass("disabled");
        $('#addDirectoryRoleMember').addClass("disabled");
    }

}