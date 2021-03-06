package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.demo.bean.Box;
import com.demo.bean.Order;
import com.demo.bean.User;
import com.demo.dao.OrderDAo;
import com.demo.dao.UserDAo;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RPiServlet extends HttpServlet { //树莓派控制
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // String signal = request.getParameter("signal");
        String temp = request.getParameter("temp");
        String ster = request.getParameter("ster");
        String lock = request.getParameter("lock");
        String qrnumber = request.getParameter("qrnumber");
        Box box = new Box();
        box.setBox_temp(temp);
        box.setBox_sterilization(ster);
        box.setBox_lock(lock);
        box.setBox_qrcode(qrnumber);

        OrderDAo orderDAo = new OrderDAo();

        String result = orderDAo.RPiGetOrder(box);
        orderDAo.RPiBoxStatusUpdate(box);// 更新箱柜状态

        PrintWriter out = response.getWriter();//回应请求
        out.write(result);
        out.flush();
        out.close();
        System.out.println(result);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
