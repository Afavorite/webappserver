package com.demo.service;

import com.demo.dao.UserDAo;
import com.demo.bean.User;
import com.sun.org.apache.bcel.internal.generic.NEW;
import sun.rmi.runtime.NewThreadAction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AndroidRegister extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);

        //设置客户端的解码方式为utf-8
        response.setContentType("text/html;charset=utf-8");
        //
        response.setCharacterEncoding("UTF-8");

        boolean b = false;
        UserDAo userDAo = new UserDAo();

        String id=request.getParameter("id");
        String password=request.getParameter("password");

        User user = new User();
        user.setName(id);
        user.setPassword(password);

        String result = "";

        b=userDAo.register(user);//连接数据库，插入该用户信息

        PrintWriter out = response.getWriter();//回应请求
        if(b){
            result = "success";
        }
        else{
            result = "fail";
        }
        out.write(result);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}