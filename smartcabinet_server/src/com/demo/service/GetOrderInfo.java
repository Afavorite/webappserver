package com.demo.service;

import com.demo.bean.Order;
import com.demo.bean.User;
import com.demo.dao.OrderDAo;
import com.demo.dao.UserDAo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GetOrderInfo extends HttpServlet { //手机获取订单详情
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String result;
        String order_creator = req.getParameter("order_creator");
        String order_range = req.getParameter("order_range");

        OrderDAo orderDAo = new OrderDAo();

        result = orderDAo.getorderinfo(order_creator, order_range);
        PrintWriter out = resp.getWriter();//回应请求

        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
