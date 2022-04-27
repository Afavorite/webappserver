package com.demo.service;

import com.demo.dao.OrderDAo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "DeleteOrder")
public class DeleteOrder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //获取订单新建者，订单箱柜，使用开始时间，使用结束时间，订单设定温度，订单设定是否杀菌
        String order_number = request.getParameter("order_number");

        String result = "";

        OrderDAo orderDAo = new OrderDAo();

        boolean flag = orderDAo.DeleteOrder(order_number);
        PrintWriter out = response.getWriter();//回应请求
        if(flag){
            result = "delete_success";
        }
        else {
            result = "delete_fail";
        }
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
