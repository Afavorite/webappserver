package com.demo.service;

import com.demo.bean.Order;
import com.demo.bean.User;
import com.demo.dao.OrderDAo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;

public class OrderAdd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //获取订单新建者，订单箱柜，使用开始时间，使用结束时间，订单设定温度，订单设定是否杀菌
        String order_creator = request.getParameter("order_creator");
        String order_box_number = request.getParameter("order_box_number");
        String order_use_start_time = request.getParameter("order_use_start_time");
        String order_use_end_time = request.getParameter("order_use_end_time");
        String order_temp = request.getParameter("order_temp");
        String order_sterilization = request.getParameter("order_sterilization");

        String result = "";

        OrderDAo orderDAo = new OrderDAo();

        Order o = new Order();
        o.setOrder_creator(order_creator);
        o.setOrder_box_number(order_box_number);
        o.setOrder_use_start_time(order_use_start_time);
        o.setOrder_use_end_time(order_use_end_time);
        o.setOrder_temp(order_temp);
        o.setOrder_sterilization(order_sterilization);

        boolean flag = orderDAo.orderadd(o);
        PrintWriter out = response.getWriter();//回应请求
        if(flag){
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
