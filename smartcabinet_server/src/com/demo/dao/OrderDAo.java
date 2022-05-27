package com.demo.dao;

import com.alibaba.fastjson.JSON;
import com.demo.bean.Box;
import com.demo.bean.Order;
import com.demo.bean.RPi_Control;
import com.demo.service.GetBoxInfo;
import com.demo.utils.JdbcUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class OrderDAo {
    public String orderadd(Order order){
        String number = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select box_status from box where box_number=?;";//查看计划预约的箱柜状态
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,order.getOrder_box_number());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next() && resultSet.getString("box_status").equals("free")){
                sql = "update box set box_status = ?, box_owner = ? where box_number = ?;";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                preparedStatement.setString(1,"used");
                preparedStatement.setString(2,order.getOrder_creator());
                preparedStatement.setString(3,order.getOrder_box_number());
                preparedStatement.executeUpdate();
                sql = "insert into orders(order_number,order_creator,order_box_number,order_status,order_use_start_time,order_use_end_time,order_temp,order_sterilization,order_temp_switch) values (?,?,?,?,?,?,?,?,?);";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                number = System.currentTimeMillis() +String.valueOf(order.getOrder_creator())+ order.getOrder_box_number();
                preparedStatement.setString(1,number);
                preparedStatement.setString(2,order.getOrder_creator());
                preparedStatement.setString(3,order.getOrder_box_number());
                preparedStatement.setString(4,"booking");
                preparedStatement.setString(5,order.getOrder_use_start_time());
                preparedStatement.setString(6,order.getOrder_use_end_time());
                preparedStatement.setString(7,order.getOrder_temp());
                preparedStatement.setString(8,order.getOrder_sterilization());
                preparedStatement.setString(9,order.getOrder_temp_switch());
                preparedStatement.executeUpdate();
                System.out.println("新建订单成功");
                
            }
            else{
                System.out.println("创建订单失败，箱柜已被使用");
                number = "fail";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return number;
    }

    public String getboxinfo(String string){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // json数组
        JSONArray array = new JSONArray();

        try {
            connection = JdbcUtils.getconn();
            String sql = "select box_number from box where box_status=?;";//获取空闲状态箱柜的列表
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,"free");
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.equals("")){

                // 获取列数
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // 遍历ResultSet中的每条数据
                while (resultSet.next()) {
                    JSONObject jsonObj = new JSONObject();

                    // 遍历每一列
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName =metaData.getColumnLabel(i);
                        String value = resultSet.getString(columnName);
                        jsonObj.put(columnName, value);
                    }
                    array.put(jsonObj);
                }
            }

        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return array.toString();
    }

    public String getorderinfo(String order_creator, String order_range){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // json数组
        JSONArray array = new JSONArray();

        try {
            connection = JdbcUtils.getconn();
            if (order_range.equals("all")){
                String sql = "select * from orders where order_creator = ?;";//获取空闲状态箱柜的列表
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,order_creator);
                resultSet = preparedStatement.executeQuery();
            }
            else if (order_range.equals("booking")){
                String sql = "select * from orders where order_creator = ? and order_status = ?;";//获取空闲状态箱柜的列表
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,order_creator);
                preparedStatement.setString(2,"booking");
                resultSet = preparedStatement.executeQuery();
            }
            else if (order_range.equals("using")){
                String sql = "select * from orders where order_creator = ? and order_status in (?, ?);";//获取空闲状态箱柜的列表
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,order_creator);
                preparedStatement.setString(2,"unlock");
                preparedStatement.setString(3,"using");
                resultSet = preparedStatement.executeQuery();
            }
            else if (order_range.equals("finish")){
                String sql = "select * from orders where order_creator = ? and order_status = ?;";//获取空闲状态箱柜的列表
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,order_creator);
                preparedStatement.setString(2,"finish");
                resultSet = preparedStatement.executeQuery();
            }

            if(!resultSet.equals("")){
                // 获取列数
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();

                // 遍历ResultSet中的每条数据
                while (resultSet.next()) {
                    JSONObject jsonObj = new JSONObject();

                    // 遍历每一列
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName =metaData.getColumnLabel(i);
                        String value = resultSet.getString(columnName);
                        jsonObj.put(columnName, value);
                    }
                    array.put(jsonObj);
                }
            }

        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return array.toString();
    }

    public void RPiBoxStatusUpdate(Box box){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "update box set box_temp = ?, box_lock = ?, box_sterilization = ?, box_qrcode = ? where box_number = ?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,box.getBox_temp());
            preparedStatement.setString(2,box.getBox_lock());
            preparedStatement.setString(3,box.getBox_sterilization());
            preparedStatement.setString(4,box.getBox_qrcode());
            preparedStatement.setString(5,"1010");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
    }

    public String RPiGetOrder(Box box){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        String json = new String();//关键

        try {
            connection = JdbcUtils.getconn();
            String sql = "select order_creator, order_status, order_temp, order_sterilization, order_temp_switch from orders where order_box_number = ? and order_status <> ?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,"1010");
            preparedStatement.setString(2,"finish");
            resultSet = preparedStatement.executeQuery();

            RPi_Control rPiControl = new RPi_Control();
            if(resultSet.next()){// 查询到状态（order_status）为booking，unlock，using
                // rPiControl.setControl_flag(resultSet.getString("order_status"));
                String order_status = resultSet.getString("order_status");
                String order_creator = resultSet.getString("order_creator");
                String order_temp = resultSet.getString("order_temp");
                String order_sterilization = resultSet.getString("order_sterilization");
                String order_temp_switch = resultSet.getString("order_temp_switch");

                rPiControl.setControl_flag(order_status);//指令写入flag标志
                rPiControl.setControl_user(order_creator);

                String sql1 = "select box_lock from box where box_number = ?;";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql1);//组装sql语句
                preparedStatement.setString(1,"1010");
                ResultSet resultSet1 = preparedStatement.executeQuery();

                if (resultSet1.next()) {
                    if (resultSet1.getString("box_lock").equals("unlock") && box.getBox_lock().equals("lock")){
                        String sql2 = "update orders set order_status = ? where order_box_number = ? and order_status = ?;";
                        preparedStatement = (PreparedStatement)connection.prepareStatement(sql2);//组装sql语句
                        preparedStatement.setString(1,"using");
                        preparedStatement.setString(2,"1010");
                        preparedStatement.setString(3,"unlock");
                        preparedStatement.executeUpdate();
                        rPiControl.setControl_flag("using");
                    }
                }

                if (rPiControl.getControl_flag().equals("using")){
                    rPiControl.setControl_ster(order_sterilization);//根据订单下发命令
                    if (order_temp_switch.equals("on")) {
                        rPiControl.setControl_temp(order_temp);
                    }
                    else{
                        rPiControl.setControl_temp("stop");
                    }

                }
            }
            else {
                rPiControl.setControl_flag("finish");
            }

            json = JSON.toJSONString(rPiControl);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return json;
    }

    public String unlockcheck (com.alibaba.fastjson.JSONObject unlockcheck){
        String qrcode = unlockcheck.getString("box");
        String user = unlockcheck.getString("user");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String result = "";

        try {
            connection = JdbcUtils.getconn();
            String box;
            if (user.equals("1000000001")){ // 管理员
                box = qrcode.substring(0, 4);
                String sql = "select order_number, order_status from orders where order_box_number = ? and order_creator = ? and order_status <> ?;";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,box);
                preparedStatement.setString(2,user);
                preparedStatement.setString(3,"finish");
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    String order_number = resultSet.getString("order_number");
                    String order_status = resultSet.getString("order_status");
                    if (order_status.equals("unlock")) {
                        result = "already unlock";//查询结果为unlock
                    }
                    else {//查询结果为booking，using
                        sql = "update orders set order_status = ? where order_number = ?;";
                        preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                        preparedStatement.setString(1,"unlock");
                        preparedStatement.setString(2,order_number);
                        preparedStatement.executeUpdate();
                        result = "unlock";
                    }
                }
                else{//查询没有结果，说明为finish或者没有
                    result = "not exist";
                }
            }
            else {
                box = qrcode.substring(0, 4);
                String sql = "select box_qrcode from box where box_number = ?;";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                preparedStatement.setString(1,box);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    if (qrcode.equals(resultSet.getString("box_qrcode"))){
                        sql = "select order_number, order_status from orders where order_box_number = ? and order_creator = ? and order_status <> ?;";
                        preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                        preparedStatement.setString(1,box);
                        preparedStatement.setString(2,user);
                        preparedStatement.setString(3,"finish");
                        resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()){
                            String order_number = resultSet.getString("order_number");
                            String order_status = resultSet.getString("order_status");
                            if (order_status.equals("unlock")) {
                                result = "already unlock";//查询结果为unlock
                            }
                            else {//查询结果为booking，using
                                sql = "update orders set order_status = ? where order_number = ?;";
                                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                                preparedStatement.setString(1,"unlock");
                                preparedStatement.setString(2,order_number);
                                preparedStatement.executeUpdate();
                                result = "unlock";
                            }
                        }
                        else{//查询没有结果，说明为finish或者没有
                            result = "not exist";
                        }
                    }
                    else { result = "not exist"; }
                }
                else { result = "not exist"; }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return result;
    }

    public Boolean FinishOrder(String order_number){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean flag = false;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select order_status, order_box_number from orders where order_number = ?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,order_number);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String order_status = resultSet.getString("order_status");
                String order_box_number = resultSet.getString("order_box_number");
                if (order_status.equals("booking") || order_status.equals("using")) {
                    sql = "update orders set order_status = ?, order_end_time = ? where order_number = ?;";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                    preparedStatement.setString(1,"finish");
                    java.util.Date date = new java.util.Date();
                    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    preparedStatement.setString(2,dateFormat.format(date));
                    preparedStatement.setString(3,order_number);
                    preparedStatement.executeUpdate();

                    sql = "update box set box_status = ?, box_owner = ? where box_number = ?;";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                    preparedStatement.setString(1,"free");
                    preparedStatement.setString(2,null);
                    preparedStatement.setString(3,order_box_number);
                    preparedStatement.executeUpdate();
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return flag;
    }

    public Boolean DeleteOrder(String order_number){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Boolean flag = false;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select order_status from orders where order_number = ?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,order_number);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                String order_status = resultSet.getString("order_status");
                if (order_status.equals("finish")) {
                    sql = "delete from orders where order_number = ?";
                    preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
                    preparedStatement.setString(1,order_number);
                    preparedStatement.executeUpdate();
                    flag = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return flag;
    }
}
