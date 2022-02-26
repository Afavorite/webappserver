package com.demo.dao;

import com.demo.bean.User;
import com.demo.utils.JdbcUtils;

import javax.xml.transform.Result;
import java.sql.*;

public class UserDAo {
    //数据库连接对象
    //此方法用于在数据库中查询信息并与Login.jsp表格中所填信息比较，若数据库中存在
    //与表格所填数据一一对应相等，则登陆成功，否则登录失败
    public User login(String username, String password){
        User u = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getconn();
            String sql = "select * from users where stu_id=? and password=?;";
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);//组装sql语句
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                u = new User();
                u.setName(resultSet.getString("stu_id"));
                u.setPassword(resultSet.getString("password"));
                System.out.println("登录成功");
            }
            else{
                System.out.println("用户名或者密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return u;
    }

    //此方法实现注册功能，向数据库中写入新用户的信息
    public boolean addUser(User user){
        boolean flag = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getconn();//连接数据库

            String sql = "select * from users where stu_id=?;"; //判断注册的账号是否重复
            preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getName());
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()){
                flag = true;
                sql = "insert into users(stu_id,password) values(?,?);";
                preparedStatement = (PreparedStatement)connection.prepareStatement(sql);
                preparedStatement.setString(1,user.getName());
                preparedStatement.setString(2,user.getPassword());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            JdbcUtils.close(preparedStatement,connection);
        }
        return flag;
    }

    //android_login
//    public boolean isuserlogin(String id,String password){
//        boolean isValid = false;
//        String drv = "com.mysql.jdbc.Driver";
//        String url = "jdbc:mysql://localhost:3306/smartcabinet";
//        String usr = "root";
//        String pwd = "Lkw121731";
//        String sql="select * from users where stu_id='"+id+"' and password='"+password+"'";
//        try{
//            Class.forName(drv).newInstance();
//            Connection conn = DriverManager.getConnection(url,usr,pwd);
//            Statement stm = conn.createStatement();
//            ResultSet rs = stm.executeQuery(sql);
//
//            if(rs.next()){
//                isValid = true;
//            }
//
//            rs.close();
//            stm.close();
//            conn.close();
//        }catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
//        }
//        if(isValid){//判断用户名以及密码是否与设定相符
//            return true;
//        }
//        else return false;
//    }

    //android_register
    public boolean userregister(String id,String password){

        boolean b = false;
        String drv = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/smartcabinet";
        String usr = "root";
        String pwd = "Lkw121731";
        String sql = "select * from users where stu_id='"+id+"'";

        try{
            Class.forName(drv).newInstance();
            Connection conn = DriverManager.getConnection(url,usr,pwd);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            if(!rs.next()){

                sql = "insert into users(stu_id,password) values('"+id+"','"+password+"')";
                stm.execute(sql);
                b = true;
            }

            rs.close();
            stm.close();
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        if(b)
        {
            return true;
        }
        else return false;
    }


}
