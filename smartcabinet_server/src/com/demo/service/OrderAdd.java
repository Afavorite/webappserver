package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        String jsonstring = request.getParameter("jsonstring");

        String result = "";

        OrderDAo orderDAo = new OrderDAo();

        Order o = (Order) JSON.parseObject(jsonstring, Order.class);

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
