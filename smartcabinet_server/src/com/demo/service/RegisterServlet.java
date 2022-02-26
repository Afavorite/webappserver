package com.demo.service;

import com.demo.bean.User;
import com.demo.dao.UserDAo;
import sun.util.resources.cldr.ar.CalendarData_ar_MA;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPut(request,response);
    }

    //    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//    }
    protected  void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setCharacterEncoding("UTF-8");
        //PrintWriter out = response.getWriter();
        response.setContentType("text/html;charset=UTF-8");
        String username = request.getParameter("name");
        String password = request.getParameter("password");

        User user = new User();
        user.setName(username);
        user.setPassword(password);
        UserDAo userDAo = new UserDAo();
        boolean flag = userDAo.addUser(user);
        if (flag && !user.getName().equals(null)){
            System.out.println("注册成功");
            request.getRequestDispatcher("Login.jsp").forward(request,response);
        }
        else{
            System.out.println("注册失败");
            request.getRequestDispatcher("defeat.jsp").forward(request,response);
        }


    }
}
