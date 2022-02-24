package com.demo.service;

import com.demo.dao.UserDAo;
import com.demo.bean.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AndroidLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");

        boolean b=false;
        UserDAo userDAo = new UserDAo();

        //根据标示名获取JSP文件中表单所包含的参数
        String id=request.getParameter("id");
        String password=request.getParameter("password");
        String result = "";

        b=userDAo.isuserlogin(id,password);//使用模型对账号和密码进行验证，返回一个boolean类型的对象
        PrintWriter out = response.getWriter();//回应请求
        if(b){  //如果验证结果为真，跳转至登录成功页面
            result = "success";
        }
        else {  //如果验证结果为假，跳转至登录失败页面
            result = "fail";
        }
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
