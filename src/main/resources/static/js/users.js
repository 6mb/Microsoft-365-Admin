let licenseList;
let domainList;
let pageITotal;
let pageIndex = 1;
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
        listUsers(obj);
        // 许可证
        listLicense();
        listDomain();

    }
    setCookie("pageIndex", 1);
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
        url: path + "/getUsersStatistics",
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

function listUsers(obj) {
    // 需要设置选中值
    if (isNotNull(obj)) {
        if (isNotNull(obj.accountEnabled)) {
            $("#accountEnabled").val(obj.accountEnabled)
        }
        if (isNotNull(obj.assignLicense)) {
            $("#assignLicense").val(obj.assignLicense)
        }
    }
    $.ajax({
        type: "get",
        url: path + "/listUsers",
        data: {
            "pageIndex": pageIndex,
            "pageSize": 10,
            "appName": getAppName(),
            "skuId": getSelect("#license"),
            "accountEnabled": getSelect("#accountEnabled"),
            "assignLicense": getSelect("#assignLicense"),
            "displayName": getInput("#displayName"),
            "userPrincipalName": getInput("#userPrincipalName"),
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                // 表格
                let usersTable = r.data.list;
                let num = (pageIndex - 1) * 10;
                for (i in usersTable) {
                    let skuName = "";
                    if (usersTable[i].skuVos !== null) {
                        for (j in usersTable[i].skuVos) {
                            skuName = skuName + usersTable[i].skuVos[j].skuName;
                        }
                    } else {
                        skuName = "无";
                    }
                    let tr =
                        '<td> <label class="lyear-checkbox checkbox-primary"> <input type="checkbox" name="userIds" value="' + usersTable[i].userId + '" onclick="checkBoxClick($(this))"><span></span> </label> </td>'
                        + '<td>' + (parseInt(num) + parseInt(i) + 1) + '</td>'
                        + '<td hidden name="userId">' + usersTable[i].userId + '</td>'
                        + '<td name="userPrincipalName">' + usersTable[i].userPrincipalName + '</td>'
                        + '<td>' + usersTable[i].displayName + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + usersTable[i].displayAccountEnable + '</td>'
                        + '<td>' + usersTable[i].usageLocation + '</td>'
                        + '<td>' + usersTable[i].createdDateTime + '</td>';
                    $("#usersTable").append('<tr>' + tr + '</tr>')
                }
                // 分页处理，计算总页数
                pageITotal = parseInt(r.data.total / 10) + 1;
                $("#usersTotal").text("总页数：" + pageITotal);
                $("#usersIndex").text("当前页：" + pageIndex);
                lightyear.loading('hide');
            }
        },
        error: function () {
            lightyear.loading('hide');
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });

}


function listLicense() {
    $.ajax({
        type: "get",
        url: path + "/listLicense",
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
        url: path + "/getDomains",
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

function pageOnclick(data) {
    $("#usersTable tr:not(:first)").empty();
    pageIndex = isNotNull(getCookie("pageIndex")) ? getCookie("pageIndex") : 1;
    if (data === 0 && pageIndex > 1) {
        pageIndex = parseInt(pageIndex) - 1;
    }
    if (data === 1 && pageIndex < pageITotal) {
        pageIndex = parseInt(pageIndex) + 1;
    }
    setCookie("pageIndex", pageIndex);
    listUsers();
}

function addUserClick() {
    lightyear.loading('show');
    // 参数获取
    let displayName = getInput("#addDisplayName");
    let skuId = getSelect("#addLicenseSelect");
    let mailNickname = getInput("#addMailNickname");
    let domain = getSelect("#addDomainSelect");
    let password = getInput("#addPassword");
    // 参数校验


    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/addUser",
        data: {
            "appName": getAppName(),
            "displayName": displayName,
            "skuId": skuId,
            "mailNickname": mailNickname,
            "domain": domain,
            "password": password
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                let userInfo = '名称：' + r.data.displayName + '<br>账号：' + r.data.userPrincipalName + '<br>密码：' + r.data.password;
                lightyear.loading('hide');
                $.alert({
                    title: '添加成功',
                    content: '新增账号成功：<br><br><strong>' + userInfo + '</strong><br><br>',
                    buttons: {
                        confirm: {
                            text: '确认',
                            btnClass: 'btn-primary',
                            action: function () {
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

function addLicenseClick() {
    lightyear.loading('show');
    let userId = $('#licenseSelectModalUserId').html();
    let skuId = getSelect("#licenseSelectModal");
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/addLicense",
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

function enableUserClick(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/enableDisableUser",
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

function disableUserClick(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/enableDisableUser",
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

function deletedUserClick(userId) {
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/deletedUser",
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
    let num = $("#addUserBatchNum").val();
    let skuId = getSelect("#licenseSelectModalBatch");
    let domain = getSelect("#domainSelectModalBatch");
    // 提交请求
    $.ajax({
        type: "get",
        url: path + "/createUserBatch",
        data: {
            "appName": getAppName(),
            "num": num,
            "skuId": skuId,
            "domain": domain
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('正在创建账户中，请等待！', 'success', delay);
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
        url: path + "/deletedUserBatch",
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
        let option = "<option value=" + domainList[i].id + ">" + domainList[i].id + "</option>";
        $("#" + id).append(option);
    }
}

function searchUsers() {
    lightyear.loading('show');
    $("#usersTable tr:not(:first)").empty();
    listUsers();
}


function checkBoxClick(data) {
    let checkbox = $("[name='userIds']");
    let checkBoxClickCount = 0;
    for (i in checkbox) {
        if (checkbox[i].checked) {
            checkBoxClickCount++;
        }
    }
    if (checkBoxClickCount === 1) {
        $('#enableAccount').removeClass("disabled");
        $('#disableAccount').removeClass("disabled");
        $('#deletedAccount').removeClass("disabled");
        $('#addLicenseAccount').removeClass("disabled");
    } else {
        $('#enableAccount').addClass("disabled");
        $('#disableAccount').addClass("disabled");
        $('#deletedAccount').addClass("disabled");
        $('#addLicenseAccount').addClass("disabled");
    }

}