app.service("loginService", function ($http) {
    // 获取登陆用户名
    this.getUsername = function () {
        return $http.get("../login/getUsername.do");
    };
});