var path = url + "/microsoft";
var appNameList;
$.ajax({
    type: "get",
    async: false,
    url: path + "/365/getAppName",
    data: {},
    dataType: "json",
    success: function (r) {
        if (r.status !== 200) {
            lightyear.notify(r.message, 'error', 1000);
        } else {
            appNameList = r.data;
        }
    },
    error: function (r) {
        console.log(r)
        if (r.status === 401) {
            lightyear.notify(r.message, 'error', 200);
            window.location.href = "login.html";
        } else {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    }
});

function refreshCache() {
    lightyear.loading('show');
    $.ajax({
        type: "get",
        url: path + "/365/refresh",
        data: {
            "appName": getAppName()
        },
        dataType: "json",
        success: function (r) {
            lightyear.loading('hide');
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', 200);
            } else {
                lightyear.notify("刷新缓存成功！", 'success', 200);
                window.location.reload();
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 200);
        }

    });
}

function reloadPage() {
    lightyear.loading('show');
    setCookie("appName", getAppName());
    location.reload();
    lightyear.loading('hide');
}


function setCookie(name, value) {
    var exp = new Date();
    exp.setTime(exp.getTime() + 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {
    var regExp = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    var arr = document.cookie.match(regExp);
    if (arr == null) {
        return null;
    }
    return unescape(arr[2]);
}

function setAppName() {
    if (!isNotNull(appNameList)) {
        return false;
    }
    var appName = getCookie("appName");
    for (i in appNameList) {
        if (appName !== null && appNameList[i] === appName) {
            var option = "<option value=" + appNameList[i] + " selected>" + appNameList[i] + "</option>";
        } else {
            var option = "<option value=" + appNameList[i] + ">" + appNameList[i] + "</option>";
        }
        $("#appNameSelect").append(option);
    }
    return true;
}

function getAppName() {
    var options = $("#appNameSelect option:selected");
    return options.val();
}

function getSelect(id) {
    var options = $(id + " option:selected");
    return options.val();
}

function getInput(id) {
    return $(id).val();
}

function isNotNull(ele) {
    if (typeof ele === 'undefined') {
        return false;
    } else if (ele == null) {
        return false;
    } else if (ele === '') {
        return false;
    }
    return true;
}

