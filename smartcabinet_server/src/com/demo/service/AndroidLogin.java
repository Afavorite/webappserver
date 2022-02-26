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

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("id");
        String password = request.getParameter("password");
        String result = "";
        UserDAo userDAo = new UserDAo();

        User u = new User();//新建user，填入信息
        u.setName(username);
        u.setPassword(password);

        User user = userDAo.login(u);
        PrintWriter out = response.getWriter();//回应请求
        if(user != null){
            result = "success";
        }
        else {
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
