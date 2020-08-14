$(window).load(function () {
    lightyear.loading('show');
    // 设置组织类型
    setAppName();
    getLicenseStatistics();
    listLicense();
    lightyear.loading('hide');
});

function getLicenseStatistics() {
    $.ajax({
        type: "get",
        url: path + "/getLicenseStatistics",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'error', 1000);
            } else {
                $("#productSubs").text(r.data.productSubs);
                $("#licenses").text(r.data.licenses);
                $("#allocatedLicenses").text(r.data.allocatedLicenses);
                $("#availableLicenses").text(r.data.availableLicenses);
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
                // 表格
                var licenseTable = r.data;
                for (i in licenseTable) {
                    var tr = '<td>' + i + '</td>'
                        + '<td>' + licenseTable[i].skuName + '</td>'
                        + '<td>' + licenseTable[i].displayStatus + '</td>'
                        + '<td>' + (licenseTable[i].enabled + licenseTable[i].suspended + licenseTable[i].warning) + '</td>'
                        + '<td>' + licenseTable[i].consumedUnits + '</td>'
                        + '<td>' + (licenseTable[i].enabled - licenseTable[i].consumedUnits) + '</td>'
                        + '<td>' + licenseTable[i].enabled + '</td>'
                        + '<td>' + licenseTable[i].suspended + '</td>'
                        + '<td>' + licenseTable[i].warning + '</td>';
                    $("#licenseTable").append('<tr>' + tr + '</tr>')
                }
            }
        }
    });
}