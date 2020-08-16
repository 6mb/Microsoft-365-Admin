let path = "http://127.0.0.1:8099";

$(window).on("load", function () {
    $('#titleName').html(" 关于平台 ");
    $("#appNameSelect").hide();
    $("#clearCache").hide();
    getSystemMonitor();
});

setInterval(function () {
    getSystemMonitor();
}, 1000);

function getSystemMonitor() {
    $.ajax({
        type: "get",
        url: path + "/system/monitor",
        data: {},
        dataType: "json",
        success: function (r) {
            if (r.status !== 200) {
                lightyear.notify(r.message, 'danger', 1000);
            } else {
                $("#systemTable").empty();
                $("#jvmTable").empty();
                sys = r.data.sys;
                jvm = r.data.jvm;
                $("#systemTable").append('<thead>\n' +
                    '<tr>\n' +
                    '<th>系统属性</th>\n' +
                    '<th></th>\n' +
                    '</thead>'
                );
                $("#systemTable").append('<tr>' + '<td> 操作系统 </td>' + '<td>' + sys.osName + '</td>' + '</tr>');
                $("#systemTable").append('<tr>' + '<td> 系统版本 </td>' + '<td>' + sys.osVersion + '</td>' + '</tr>');
                $("#systemTable").append('<tr>' + '<td> 系统架构 </td>' + '<td>' + sys.osArch + '</td>' + '</tr>');
                $("#systemTable").append('<tr>' + '<td> 系统启动时间 </td>' + '<td>' + sys.upTime + '</td>' + '</tr>');
                $("#jvmTable").append('<thead>\n' +
                    '<tr>\n' +
                    '<th>JVM属性</th>\n' +
                    '<th></th>\n' +
                    '</tr>\n' +
                    '</thead>'
                );
                $("#jvmTable").append('<tr>' + '<td> JVM 版本' + '</td>' + '<td>' + jvm.version + '</td>' + '</tr>');
                $("#jvmTable").append('<tr>' + '<td> JVM 可占用内存' + '</td>' + '<td>' + jvm.max.toFixed(2) + ' MB</td>' + '</tr>');
                $("#jvmTable").append('<tr>' + '<td> JVM 已占用内存' + '</td>' + '<td>' + jvm.free.toFixed(2) + ' MB</td>' + '</tr>');
                $("#jvmTable").append('<tr>' + '<td> JVM 空闲内存' + '</td>' + '<td>' + jvm.total.toFixed(2) + ' MB</td>' + '</tr>');
            }
        },
        error: function () {
            /*错误信息处理*/
            lightyear.notify("服务器错误，请稍后再试~", 'danger', 100);
        }
    });
}
