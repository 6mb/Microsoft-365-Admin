let codeList;
let pageITotal;
let pageIndex = 1;

$(window).load(function () {
    $('#titleName').html(" 邀请管理 ");
    lightyear.loading('show');
    // 设置组织类型
    var success = setAppName();
    if (success) {
        getCodeStatistics();
        listCode();
    }
    lightyear.loading('hide');
});

function getCodeStatistics() {
    $.ajax({
        type: "get",
        async: false,
        url: path + "/code/getCodeStatistics",
        data: {},
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                $("#codes").text(r.data.codes);
                $("#codeValid").text(r.data.valid);
                $("#codeInvalid").text(r.data.invalid);
                $("#codeInvalidUser").text(r.data.users);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    });
}

function listCode() {
    $.ajax({
        type: "get",
        async: false,
        url: path + "/code/list",
        data: {
            "code": getInput("#codeInput"),
            "valid": getSelect("#codeValidSelect"),
            "pageIndex": pageIndex,
            "pageSize": 10
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                // 表格
                let codeTable = r.data.content;
                let num = (pageIndex - 1) * 10;
                for (i in codeTable) {
                    var tr = '<td>' + (parseInt(num) + parseInt(i) + 1) + '</td>'
                        + '<td>' + codeTable[i].code + '</td>'
                        + '<td>' + (isNotNull(codeTable[i].createTime) ? codeTable[i].createTime : "-") + '</td>'
                        + '<td>' + (Boolean(codeTable[i].valid) ? ('有效') : ('失效')) + '</td>'
                        + '<td>' + (isNotNull(codeTable[i].expirationTime) ? codeTable[i].expirationTime : "-") + '</td>'
                        + '<td>' + (isNotNull(codeTable[i].invitedUser) ? codeTable[i].invitedUser : '-') + '</td>'
                        + '<td>' + (isNotNull(codeTable[i].subscribe) ? codeTable[i].subscribe : '-') + '</td>';
                    $("#codeTable").append('<tr>' + tr + '</tr>')
                }
                // 分页处理，计算总页数
                pageITotal = r.data.totalPages;
                $("#codeTotal").text("总页数：" + pageITotal);
                $("#codeIndex").text("当前页：" + pageIndex);
                lightyear.loading('hide');
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    });
}

function pageOnclickCode(data) {
    $("#codeTable tr:not(:first)").empty();
    pageIndex = isNotNull(getCookie("pageIndexCode")) ? getCookie("pageIndexCode") : 1;
    if (data === 0 && pageIndex > 1) {
        pageIndex = parseInt(pageIndex) - 1;
    }
    if (data === 1 && pageIndex < pageITotal) {
        pageIndex = parseInt(pageIndex) + 1;
    }
    setCookie("pageIndexCode", pageIndex);
    listCode();
}

function searchCodes() {
    lightyear.loading('show');
    $("#codeTable tr:not(:first)").empty();
    listCode();
}

function addCodeClick() {
    lightyear.loading('show');
    let num = $("#numCode").val();
    // 提交请求
    $.ajax({
        type: "post",
        url: path + "/code/generate",
        data: {
            "num": num
        },
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.loading('hide');
                lightyear.notify(r.message, 'danger', delay);
            } else {
                console.log(r);
                lightyear.loading('hide');
                lightyear.notify('创建邀请码成功数量：' + num, 'success', delay);
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', delay);
            lightyear.loading('hide');
        }
    });
}
