let delay = 2000;
$(window).on("load", function () {
    $('#titleName').html(" 申请账号 ");
    lightyear.loading('show');

    listLicense();

});

function listLicense() {
    $.ajax({
        type: "get",
        url: "/front/listLicense",
        data: {},
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', delay);
            } else {
                licenseList = r.data;
                setLicense("g-sub-select");
            }
            lightyear.loading('hide');
        },
        error: function () {
            lightyear.loading('hide');
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
        }
    });
}


function setLicense(id) {
    // 先清空下数据
    $("#" + id).empty();
    $("#" + id).append("<option value=\"\" disabled selected hidden>请选择许可证</option>\n");
    for (i in licenseList) {
        let option = "<option value=" + (licenseList[i].appName + '_' + licenseList[i].skuId) + ">" + licenseList[i].skuName + "</option>";
        $("#" + id).append(option);
    }
}

function getSelect(id) {
    var options = $(id + " option:selected");
    return options.val();
}

function getInput(id) {
    return $(id).val();
}

function addUserClick() {
    lightyear.loading('show');
    // 参数获取
    let select = getSelect("#g-sub-select")
    let appName = select.split("_")[0];
    let skuId = select.split("_")[1];
    let displayName = getInput("#g_name");
    let mailNickname = getInput("#g_mail_sub");
    let password = getInput("#g-password");
    let mailbox = getInput("#g-send_mail");
    let code = getInput("#g-code");
    // 参数校验

    // 提交请求
    $.ajax({
        type: "post",
        url: "/front/create",
        data: {
            "appName": appName,
            "displayName": displayName,
            "skuId": skuId,
            "mailNickname": mailNickname,
            "password": password,
            "mailbox": mailbox,
            "code": code
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                let message = r.message;
                let password_error = "Error code: Request_BadRequestError message: The specified password does not comply with password complexity requirements. Please provide a different password.";
                let name_error = "Error code: Request_BadRequestError message: Another object with the same value for property userPrincipalName already exists.";
                if (password_error === message){
                    message = "密码太简单啦，";
                }
                if (name_error === message){
                    message = "该邮箱前缀已经被抢啦！";
                }
                lightyear.notify(message, 'danger', delay);
            } else {
                console.log(r);
                let userInfo = '名称：' + r.data.displayName + '<br>账号：' + r.data.userPrincipalName + '<br>密码：' + r.data.password;
                lightyear.loading('hide');
                $.alert({
                    title: '新增账号成功！',
                    content: '<br><strong>' + userInfo + '</strong><br>',
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