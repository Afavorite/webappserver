package com.demo.service;

import com.alibaba.fastjson.JSONObject;
import com.demo.dao.OrderDAo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UnlockCheck extends HttpServlet {// 手机扫码解锁
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
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
