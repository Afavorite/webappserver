package com.demo.service;

import com.demo.bean.Order;
import com.demo.dao.OrderDAo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderAdd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        //获取订单新建者，订单箱柜，订单设定温度，订单设定是否杀菌
        String order_creator = request.getParameter("order_creator");
        String order_box_number = request.getParameter("order_box_number");
        String order_temp = request.getParameter("order_temp");
        String order_sterilization = request.getParameter("order_sterilization");

        String result = "";

        OrderDAo orderDAo = new OrderDAo();

        Order o = new Order();
    }
}
