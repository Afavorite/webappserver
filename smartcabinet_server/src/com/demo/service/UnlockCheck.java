package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.dao.OrderDAo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnlockCheck extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        //获取订单新建者，订单箱柜，使用开始时间，使用结束时间，订单设定温度，订单设定是否杀菌
        String unlockcheck = req.getParameter("unlockcheck");

        String result = "";
        OrderDAo orderDAo = new OrderDAo();

        result = orderDAo.unlockcheck(JSONObject.parseObject(unlockcheck));

        PrintWriter out = resp.getWriter();//回应请求
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }
}
