var licenseList;
var domianList;
var pageITotal;
var pageIndex = 1;

$(window).on("load", function () {
    lightyear.loading('show');
    var obj = getParam();
    // 设置组织类型
    var success = setAppName();
    if (success) {
        // 统计信息
        getUsersStatistics();
        // 列表查询
        listUsers(obj);
        // 许可证
        listLicense();
        listDomain();
        setCookie("pageIndex", 1)
    }
    lightyear.loading('hide');
});

var getParam = function () {
    try {
        var url = window.location.href;
        var result = url.split("?")[1];
        var keyValue = result.split("&");
        var obj = {};
        for (var i = 0; i < keyValue.length; i++) {
            var item = keyValue[i].split("=");
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
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                $("#users").text(r.data.users);
                $("#allowedUsers").text(r.data.allowedUsers);
                $("#biddenUsers").text(r.data.biddenUsers);
                $("#unauthorizedUsers").text(r.data.unauthorizedUsers);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
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
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                // 表格
                var usersTable = r.data.list;

                var num = (pageIndex - 1) * 10;
                for (i in usersTable) {
                    var skuName = "";
                    if (usersTable[i].skuVos !== null) {
                        for (j in  usersTable[i].skuVos) {
                            skuName = skuName + usersTable[i].skuVos[j].skuName;
                        }
                    }
                    var tr =
                        '<td> <label class="lyear-checkbox checkbox-primary"> <input type="checkbox" name="ids[]" value="1"><span></span> </label> </td>'
                        + '<td>' + (parseInt(num) + parseInt(i)) + '</td>'
                        + '<td>' + usersTable[i].userPrincipalName + '</td>'
                        + '<td>' + usersTable[i].displayName + '</td>'
                        + '<td>' + usersTable[i].displayAccountEnable + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + usersTable[i].usageLocation + '</td>'
                        + '<td>' + usersTable[i].createdDateTime + '</td>';
                    $("#usersTable").append('<tr>' + tr + '</tr>')
                }
                // 分页处理，计算总页数
                pageITotal = parseInt(r.data.total / 10) + 1;
                $("#usersTotal").text("总页数：" + pageITotal);
                $("#usersIndex").text("当前页：" + pageIndex);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
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
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                licenseList = r.data;
                setLicense("license");
                setLicense("addLicenseSelect");
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
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
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                domianList = r.data;
                setDomain("addDomainSelect");
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    });
}

function addUserClick() {
    lightyear.loading('show');
    // 参数获取
    var displayName = getInput("#addDisplayName");
    var skuId = getSelect("#addLicenseSelect");
    var mailNickname = getInput("#addMailNickname");
    var domain = getSelect("#addDomainSelect");
    var password = getInput("#addPassword");
    // 参数校验


    // 提交请求
    // $.ajax({
    //     type: "post",
    //     url: path + "/addUser",
    //     data: {
    //         "appName": getAppName(),
    //         "displayName": displayName,
    //         "skuId": skuId,
    //         "mailNickname": mailNickname,
    //         "domain": domain,
    //         "password": password
    //     },
    //     dataType: "json",
    //     success: function (r) {
    //         if (r.status !== 200) {
    //             lightyear.notify(r.message, 'danger', 1000);
    //         } else {
    //             console.log(r)
    //         }
    //     },
    //     error: function () {
    //         /*错误信息处理*/
    //         lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
    //     }
    // });
    lightyear.loading('hide');
}

function setLicense(id) {
    for (i in licenseList) {
        var option = "<option value=" + licenseList[i].skuId + ">" + licenseList[i].skuName + "</option>";
        $("#" + id).append(option);
    }
}

function setDomain(id) {
    for (i in domianList) {
        var option = "<option value=" + domianList[i].id + ">" + domianList[i].id + "</option>";
        $("#" + id).append(option);
    }
}

function searchUsers() {
    $("#usersTable tr:not(:first)").empty();
    listUsers();
}
