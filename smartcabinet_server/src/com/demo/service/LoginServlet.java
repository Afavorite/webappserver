package com.demo.service;

import com.demo.bean.User;
import com.demo.dao.UserDAo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UserDAo userDAo = new UserDAo();
        User u = new User();//新建user，填入信息
        u.setName(username);
        u.setPassword(password);
        User user = userDAo.login(u);
        if(user != null){
            request.getRequestDispatcher("success.jsp").forward(request,response);
        }
        else {
            request.getRequestDispatcher("defeat.jsp").forward(request,response);
        }
    }
}
