var path = "http://127.0.0.1:8099/microsoft/365";
var appNameList;
$.ajax({
    type: "get",
    url: path + "/getAppName",
    data: {},
    dataType: "json",
    success: function (r) {
        if (r.status !== 200) {
            lightyear.notify(r.message, 'error', 1000);
        } else {
            appNameList = r.data;
        }
    }
});

function setAppName() {
    for (i in appNameList) {
        var option = "<option value=" + appNameList[i] + ">" + appNameList[i] + "</option>";
        $("#appNameSelect").append(option);
    }

}

function getAppName() {
    var options=$("#appNameSelect option:selected");
    return options.val();
}