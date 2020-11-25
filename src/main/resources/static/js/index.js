$(window).on("load", function () {
    $('#titleName').html(" 后台首页 ");
    lightyear.loading('show');
    // 设置组织类型
    var success = setAppName();
    if (success) {
        homePage();
    }
});

function homePage() {
    $.ajax({
        type: "get",
        url: path + "/365/homePage",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', 100);
            } else {
                $("#productSubs").text(r.data.statisticsVo.productSubs);
                $("#licenses").text(r.data.statisticsVo.licenses);
                $("#allocatedLicenses").text(r.data.statisticsVo.allocatedLicenses);
                $("#availableLicenses").text(r.data.statisticsVo.availableLicenses);
                $("#users").text(r.data.statisticsVo.users);
                $("#allowedUsers").text(r.data.statisticsVo.allowedUsers);
                $("#biddenUsers").text(r.data.statisticsVo.biddenUsers);
                $("#unauthorizedUsers").text(r.data.statisticsVo.unauthorizedUsers);

                // 表格
                var noLandingUsers = r.data.noLandingUsers;
                for (i in noLandingUsers) {
                    var skuName = "";
                    if (noLandingUsers[i].skuVos !== null) {
                        for (j in  noLandingUsers[i].skuVos) {
                            skuName = skuName + noLandingUsers[i].skuVos[j].skuName;
                        }
                    }
                    var tr = '<td>' + (parseInt(i) + 1) + '</td>'
                        + '<td hidden>' + noLandingUsers[i].userId + '</td>'
                        + '<td>' + noLandingUsers[i].userPrincipalName + '</td>'
                        + '<td>' + noLandingUsers[i].displayName + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + noLandingUsers[i].displayAccountEnable + '</td>';
                    $("#noLandingUsersTable").append('<tr>' + tr + '</tr>')
                }

                // 表格
                var unauthorizedUsers = r.data.unauthorizedUsers;
                for (i in unauthorizedUsers) {
                    var skuName = "";
                    if (unauthorizedUsers[i].skuVos !== null) {
                        for (j in  unauthorizedUsers[i].skuVos) {
                            skuName = skuName + unauthorizedUsers[i].skuVos[j].skuName;
                        }
                    } else {
                        skuName = "无";
                    }
                    var tr = '<td>' + (parseInt(i) + 1) + '</td>'
                        + '<td hidden>' + unauthorizedUsers[i].userId + '</td>'
                        + '<td>' + unauthorizedUsers[i].userPrincipalName + '</td>'
                        + '<td>' + unauthorizedUsers[i].displayName + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + unauthorizedUsers[i].displayAccountEnable + '</td>';
                    $("#unauthorizedUsersTable").append('<tr>' + tr + '</tr>')
                }
            }
            lightyear.loading('hide');
        },
        error: function () {
            lightyear.loading('hide');
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    });
}