var licenseList;

$(window).on("load", function () {
    // 设置组织类型
    setAppName();
    lightyear.loading('show');
    // 统计信息
    getUsersStatistics();
    // 列表查询
    listUsers();
    // 许可证
    listLicense();
    lightyear.loading('hide');
});

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
                lightyear.notify(r.message, 'error', 1000);
            } else {
                $("#users").text(r.data.users);
                $("#allowedUsers").text(r.data.allowedUsers);
                $("#biddenUsers").text(r.data.biddenUsers);
                $("#unauthorizedUsers").text(r.data.unauthorizedUsers);
            }
        }
    });
}

function listUsers() {
    $.ajax({
        type: "get",
        url: path + "/listUsers",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'error', 1000);
            } else {
                // 表格
                var usersTable = r.data.list;
                for (i in usersTable) {
                    var skuName = "";
                    if (usersTable[i].skuVos !== null) {
                        for (j in  usersTable[i].skuVos) {
                            skuName = skuName + usersTable[i].skuVos[j].skuName;
                        }
                    }
                    var tr = '<td>' + i + '</td>'
                        + '<td>' + usersTable[i].userPrincipalName + '</td>'
                        + '<td>' + usersTable[i].displayName + '</td>'
                        + '<td>' + usersTable[i].displayAccountEnable + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + usersTable[i].usageLocation + '</td>'
                        + '<td>' + usersTable[i].createdDateTime + '</td>';
                    $("#usersTable").append('<tr>' + tr + '</tr>')
                }

            }
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
                lightyear.notify(r.message, 'error', 1000);
            } else {
                licenseList = r.data;
                setLicense("usersLicenseSelect");
            }
        }
    });
}

function setLicense(id) {
    console.log(licenseList);
    for (i in licenseList) {
        console.log(licenseList[i].skuId);
        var option = "<option value=" + licenseList[i].skuId + ">" + licenseList[i].skuName + "</option>";
        $("#" + id).append(option);
    }
}