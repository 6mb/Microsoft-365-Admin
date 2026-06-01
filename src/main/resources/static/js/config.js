// 如果前端文件单独部署，则需要将后端地址填写上，否则为空
let url = "";
// let url = "http://127.0.0.1:8099";

function getCookieValue(name) {
    let cookieValue = null;
    let cookies = document.cookie ? document.cookie.split(';') : [];
    for (let i = 0; i < cookies.length; i++) {
        let cookie = cookies[i].trim();
        if (cookie.substring(0, name.length + 1) === (name + '=')) {
            cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
            break;
        }
    }
    return cookieValue;
}

function attachCsrfToken(xhr) {
    let token = getCookieValue("XSRF-TOKEN");
    if (token) {
        xhr.setRequestHeader("X-XSRF-TOKEN", token);
    }
}
