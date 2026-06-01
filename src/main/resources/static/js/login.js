let delay = 2000;

$.ajaxSetup({
    beforeSend: function (xhr) {
        attachCsrfToken(xhr);
    }
});

function loginClick() {
    // lightyear.loading('show');
    let username = $("#username").val();
    let password = $("#password").val();

    // 提交请求
    $.ajax({
        type: "post",
        url: url + "/login",
        data: {
            "userName": username,
            "password": password
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
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
