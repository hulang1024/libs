/*
为方便开发或测试环境，提供一种自动（快速）登录操作的方式。生产环境下要删除。
第一步：
在登录页面中插入JS代码：设置登录动作回调，该回调的输入参数为：username和password
setupAutoLogin(function(username, password) {
    doSomething();

    $("[name=username]").val(username);    // 将username和password设置到表单域
    $("[name=password]").val(password);
    submitLogin(); // 调用提交方法
});

第二步：
在HTTP URL中提供输入参数，回车，参数说明：
    参数名		参数义     类型			备注
    -----------------------------------------
    autoLogin 	自动登录   0或1
    username	用户名     string
    password	密码       string		如果省略,则默认值为 123456
    -----------------------------------------
例如 http://locahost:8080/xyz/login.do?autoLogin=1&username=admin&password=123456
*/

function setupAutoLogin(loginAction) {
    function getParamMap(url) {
        // 解析url中的参数，结果为map
        var param = url.substr(url.lastIndexOf("?") + 1);
        param = param.split("&");
        var map = {};
        param.map(function(p){
            var pairs = p.split("=");
            map[pairs[0]] = pairs[1];
        });
        return map;
    }
    //是否有自动登录参数且值为真
    var paramMap = getParamMap(location.href);
    if(!!+paramMap["autoLogin"]) {
        loginAction(paramMap["username"], paramMap["password"] || "123456");
    }
}

