$(window).on("load", function () {
    lightyear.loading('show');
    // 设置组织类型
    setAppName();
    homePage();
    lightyear.loading('hide');
});

function homePage() {
    $.ajax({
        type: "get",
        url: path + "/homePage",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'error', 1000);
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
                    var tr = '<td>' + i + '</td>'
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
                    var tr = '<td>' + i + '</td>'
                        + '<td>' + unauthorizedUsers[i].userPrincipalName + '</td>'
                        + '<td>' + unauthorizedUsers[i].displayName + '</td>'
                        + '<td>' + skuName + '</td>'
                        + '<td>' + unauthorizedUsers[i].displayAccountEnable + '</td>';
                    $("#unauthorizedUsersTable").append('<tr>' + tr + '</tr>')
                }
            }
        }
    });
}