package com.demo.dao;

import com.alibaba.fastjson.JSON;
import com.demo.bean.Box;
import com.demo.bean.Order;
import com.demo.service.GetBoxInfo;
import com.demo.utils.JdbcUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class OrderDAo {
    public Boolean orderadd(Order order){
        boolean flag = false;
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
                flag = true;
                sql = "update box set box_status = ?, box_owner = ? where box_number = ?;";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                preparedStatement.setString(1,"booked");
                preparedStatement.setString(2,order.getOrder_creator());
                preparedStatement.setString(3,order.getOrder_box_number());
                preparedStatement.executeUpdate();
                sql = "insert into orders(order_number,order_creator,order_box_number,order_status,order_use_start_time,order_use_end_time,order_temp,order_sterilization) values (?,?,?,?,?,?,?,?);";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                preparedStatement.setString(1,String.valueOf(System.currentTimeMillis())+String.valueOf(order.getOrder_creator())+String.valueOf(order.getOrder_box_number()));
                preparedStatement.setString(2,order.getOrder_creator());
                preparedStatement.setString(3,order.getOrder_box_number());
                preparedStatement.setString(4,"booking");
                preparedStatement.setString(5,order.getOrder_use_start_time());
                preparedStatement.setString(6,order.getOrder_use_end_time());
                preparedStatement.setString(7,order.getOrder_temp());
                preparedStatement.setString(8,order.getOrder_sterilization());
                preparedStatement.executeUpdate();
                System.out.println("新建订单成功");
            }
            else{
                System.out.println("创建订单失败，箱柜已被使用");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return flag;
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

    public void RPiBoxStatusUpdate(Box box){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "update box set box_temp = ? where box_number = ?;";//获取空闲状态箱柜的列表
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,box.getBox_temp());
            preparedStatement.setString(2,"1010");
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
    }

    public JSONArray RPiGetOrder(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        // json数组
        JSONArray array = new JSONArray();

        try {
            connection = JdbcUtils.getconn();
            String sql = "select order_creator, order_status, order_temp, order_sterilization from orders where order_box_number = ? and order_status <> ?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,"1010");
            preparedStatement.setString(2,"finish");
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
        return array;
    }
}
