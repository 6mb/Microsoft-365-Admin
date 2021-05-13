let delay = 2000;
function loginClick() {
    // lightyear.loading('show');
    let username = $("#username").val();
    let password = $("#password").val();
    console.log(username + "   "+password)
    // 对密码加密

    // 提交请求
    $.ajax({
        type: "post",
        url: url + "/login",
        data: {
            "userName": username,
            "password": md5(password)
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('登陆成功！', 'success', delay);
                window.location.href = "index.html";
            }
        },
        error: function (r) {
            /*错误信息处理*/
            lightyear.notify(r.responseJSON.message, 'danger', delay);
            lightyear.loading('hide');
        }
    });

}

$(document).on('keypress', function (e) {
    if (e.which === 13) {
        loginClick();
    }
});

$('#invitePage').click(
    function () {
        window.location.href = '/invite.html'
    }
)