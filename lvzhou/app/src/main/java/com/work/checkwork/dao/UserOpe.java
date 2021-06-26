package com.work.checkwork.dao;

import android.util.Log;

import com.work.checkwork.model.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class UserOpe {

    private Connection conn = null; //打开数据库对象
    private PreparedStatement ps = null;//操作整合sql语句的对象
    private ResultSet rs = null;//查询结果的集合

    //DBService 对象
    public static UserOpe userOpe = null;

    /**
     * 构造方法 私有化
     */

    private UserOpe() {

    }

    /**
     * 获取MySQL数据库单例类对象
     */

    public static UserOpe getUserOpe() {
        if (userOpe == null) {
            userOpe = new UserOpe();
        }
        return userOpe;
    }

    /**
     * 用户注册  增
     */
    public int insertUserData(UserBean user) {
        int result = -1;
        if (user != null) {
            //获取链接数据库对象
            conn = DBOpenHelper.getConn();
            //MySQL 语句
            String sql = "INSERT INTO user (userName,userPhone,userPwd,userPic) VALUES (?,?,?,?)";
            try {
                if (conn != null && !conn.isClosed()) {
                    ps = (PreparedStatement) conn.prepareStatement(sql);
                    ps.setString(1, user.userName);//第一个参数 name 规则同上
                    ps.setString(2, user.userPhone);//第二个参数 phone 规则同上
                    ps.setString(3, user.userPwd);//第三个参数 content 规则同上
                    ps.setString(4, user.userPic);//第四个参数 state 规则同上
                    result = ps.executeUpdate();//返回1 执行成功
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }


    /**
     * 获取用户信息 查
     */
    public UserBean getUserData(String userPhone, String userPwd) {
        //结果存放集合
        UserBean userBean = null;
        //MySQL 语句
        String sql = " SELECT * FROM `user`WHERE `userPhone` = ? AND `userPwd` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, userPhone);
                    ps.setString(2, userPwd);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            userBean = new UserBean();
                            userBean.id = rs.getInt("id");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = (rs.getString("userPic"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return userBean;
    }

    /**
     * 获取用户信息 查
     */
    public UserBean getUserData(int userId) {
        //结果存放集合
        UserBean userBean = null;
        //MySQL 语句
        String sql = " SELECT * FROM `user` WHERE `id` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userId);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            userBean = new UserBean();
                            userBean.id = rs.getInt("id");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = (rs.getString("userPic"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return userBean;
    }

    /**
     * 获取用户信息 查
     */
    public boolean isGuanZhu(int userId, int guanzhuId) {
        Log.e("Lance", "isGuanZhu: userId -->" + userId);
        Log.e("Lance", "isGuanZhu: guanzhuId -->" + guanzhuId);
        //结果存放集合
        boolean isGuanzhu = false;
        //MySQL 语句
        String sql = "SELECT * FROM `guanzhu` WHERE `userId` = ? AND `guanzhuId` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userId);
                    ps.setInt(2, guanzhuId);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            isGuanzhu = id != 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return isGuanzhu;
    }

    /**
     * 修改数据库中某个对象的状态  改
     */
    public int updateUserData(String userName, String userPwd, String userPic, int id) {
        int result = -1;
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        //MySQL 语句
        String sql = "UPDATE `user` set userName = ?, userPwd = ?,userPic = ? WHERE `id` = ?";
        try {
            boolean closed = conn.isClosed();
            if (conn != null && (!closed)) {
                ps = conn.prepareStatement(sql);
                ps.setString(1, userName);//第一个参数username 一定要和上面SQL语句字段顺序一致
                ps.setString(2, userPwd);//第二个参数 password 一定要和上面SQL语句字段顺序一致
                ps.setString(3, userPic);
                ps.setInt(4, id);
                result = ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }


    /**
     * 用户注册  增
     */
    public int addGuanZhu(int userId, int guanZhuId) {
        int result = -1;
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        //MySQL 语句
        String sql = "INSERT INTO guanzhu (userId,guanzhuId) VALUES (?,?)";
        try {
            if (conn != null && !conn.isClosed()) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, guanZhuId);
                result = ps.executeUpdate();//返回1 执行成功
                Log.e("Lance", "addGuanZhu: result -->" + result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }

    /**
     * 删除关注 删
     */
    public int delGuanZhu(int userId, int guanZhuId) {
        int result = -1;
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        //MySQL 语句
        String sql = "DELETE FROM `guanzhu` WHERE userId = ? AND guanzhuId = ?";
        try {
            boolean closed = conn.isClosed();
            if ((conn != null) && (!closed)) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, userId);
                ps.setInt(2, guanZhuId);
                result = ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }

    /**
     * 获取用户信息 查
     */
    public ArrayList<UserBean> getGuanZhu(int userId) {
        //结果存放集合
        ArrayList<UserBean> userList = new ArrayList<>();
        //MySQL 语句
        String sql = "SELECT u.* FROM `guanzhu` g LEFT JOIN `user` u on g.guanzhuId = u.id WHERE g.userId = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userId);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            UserBean userBean = new UserBean();
                            userBean.id = rs.getInt("id");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = (rs.getString("userPic"));
                            userList.add(userBean);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return userList;
    }

    /**
     * 获取用户信息 查
     */
    public UserBean getUserPwd(int userId, String userPwd) {
        //结果存放集合
        UserBean userBean = null;
        //MySQL 语句
        String sql = "SELECT * FROM `user` WHERE `id` = ? AND `userPwd` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userId);
                    ps.setString(2, userPwd);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            userBean = new UserBean();
                            userBean.id = rs.getInt("id");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = (rs.getString("userPic"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return userBean;
    }

} 