<%--
  Created by IntelliJ IDEA.
  User: AFavo
  Date: 2022/2/20
  Time: 下午 04:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>注册界面</title>
    <style type="text/css">
        body{
            background-repeat: no-repeat;
            background-position: center;
        }

    </style>
</head>
<body>
<div style="text-align:center;margin-top: 120px">
    <form action="${pageContext.request.contextPath}/RegisterServlet" method="post">
        <table style="margin-left:40%">
            <caption>用户注册</caption>
            <tr>
                <td>登录名：</td>
                <td><input id="name" name="name" type="text" size="20"
                           placeholder="输入10位数字学号"
                           onkeyup="check()"></td>
                <td><span id="tishi1" size="10"></span></td>
            </tr>
            <tr>
                <td>输入密码:</td>
                <td><input id="password" name="password" type="password" size="20"
                           placeholder="输入8到20位密码"
                           onkeyup="check()"></td>
                <td><span id="tishi2" size="10"></span></td>
            </tr>
            <tr>
                <td>确认密码:</td>
                <td><input id="conf_password" name="conf_password" type="password" size="20"
                           placeholder="确认密码"
                           onkeyup="check()"></td>
                <td><span id="tishi3" size="10"></span></td>
            </td>
            </tr>
            </tr>
        </table>
        <input id="submit" type="submit" value="注册">
        <input type="reset" value="重置">
    </form>
    <br>
    <a href="Login.jsp">登录</a>
    </form>
</div>
</body>
</html>

<script type="text/javascript">
    var flag1, flag2, flag3, flag = 0;
    function check() {
        checkname();
        checkpassword();
        confirmpassword();
        flag = flag1 + flag2 + flag3;
        if (flag === 3){
            document.getElementById("submit").disabled = false;
        }
        else{
            document.getElementById("submit").disabled = true;
        }
    }
    function checkname() {
        const stu_id = document.getElementById("name").value;
        let result = /^([-+])?\d+$/.test(stu_id);
        if (result && stu_id.length === 10){
            document.getElementById("tishi1").innerHTML="<br><font color='green'>输入学号符合规范</font>";
            flag1 = 1;
        }
        else {
            document.getElementById("tishi1").innerHTML="<br><font color='red'>输入学号不符合规范</font>";
            flag1 = 0;
        }
    }
    function checkpassword() {
        const password = document.getElementById("password").value;
        if (password.length >= 8 && password.length <= 20){
            document.getElementById("tishi2").innerHTML="<br><font color='green'>输入密码符合规范</font>";
            flag2 = 1;
        }
        else {
            document.getElementById("tishi2").innerHTML="<br><font color='red'>输入密码不符合规范</font>";
            flag2 = 0;
        }

    }
    function confirmpassword() {
        const password = document.getElementById("password").value;
        const repassword = document.getElementById("conf_password").value;

        if(password === repassword) {
            document.getElementById("tishi3").innerHTML="<br><font color='green'>两次密码输入一致</font>";
            flag3 = 1;

        }else {
            document.getElementById("tishi3").innerHTML="<br><font color='red'>两次输入密码不一致!</font>";
            flag3 = 0;
        }
    }
</script>

