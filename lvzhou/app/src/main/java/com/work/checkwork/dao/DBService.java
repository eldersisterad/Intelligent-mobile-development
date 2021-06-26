package com.work.checkwork.dao;

import android.util.Log;

import com.work.checkwork.model.DataBean;
import com.work.checkwork.model.LikeBean;
import com.work.checkwork.model.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Android Studio.
 */
public class DBService {

    private Connection conn = null; //打开数据库对象
    private PreparedStatement ps = null;//操作整合sql语句的对象
    private ResultSet rs = null;//查询结果的集合

    //DBService 对象
    public static DBService dbService = null;

    /**
     * 构造方法 私有化
     */

    private DBService() {

    }

    /**
     * 获取MySQL数据库单例类对象
     */

    public static DBService getDbService() {
        if (dbService == null) {
            dbService = new DBService();
        }
        return dbService;
    }

    /**
     * 向数据库插入数据  增（数据）
     */
    public int insertData(DataBean data) {
        int result = -1;
        if (data != null) {
            //获取链接数据库对象
            conn = DBOpenHelper.getConn();
            //MySQL 语句
            String sql = "INSERT INTO data (type,imgUrl,content,userId,likeNum) VALUES (?,?,?,?,?)";
            try {
                boolean closed = conn.isClosed();
                if ((conn != null) && (!closed)) {
                    ps = (PreparedStatement) conn.prepareStatement(sql);
                    ps.setInt(1, data.type);
                    ps.setString(2, data.imgUrl);
                    ps.setString(3, data.content);
                    ps.setInt(4, data.userId);
                    ps.setInt(5, data.likeNum);
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
     * 获取所有数据信息  查
     */
    public ArrayList<DataBean> getData(int type) {
        //结果存放集合
        ArrayList<DataBean> dataList = new ArrayList<>();
        //MySQL 语句
        String sql;
        sql = "SELECT d.*, u.userName, u.userPhone, u.userPwd, u.userPic, IFNULL(l.id,0) as likeId FROM `data` d LEFT JOIN `user` u on u.id = d.userId LEFT JOIN `like` l on l.dataId = d.id AND l.userId = d.userId WHERE d.`type` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, type);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            DataBean s = new DataBean();
                            s.id = rs.getInt("id");
                            s.type = rs.getInt("type");
                            s.userId = rs.getInt("userId");
                            s.imgUrl = rs.getString("imgUrl");
                            s.content = rs.getString("content");
                            s.likeNum = rs.getInt("likeNum");
                            UserBean userBean = new UserBean();
                            userBean.id = rs.getInt("userId");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = rs.getString("userPic");
                            s.userBean = userBean;
                            s.isLike = (rs.getInt("likeId") != 0);
                            dataList.add(s);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return dataList;
    }

    /**
     * 获取所有数据信息  查
     */
    public ArrayList<DataBean> getSouData(String souContent) {
        //结果存放集合
        ArrayList<DataBean> dataList = new ArrayList<>();
        //MySQL 语句
        String sql;
        sql = "SELECT d.*, u.userName, u.userPhone, u.userPwd, u.userPic, IFNULL(l.id,0) as likeId FROM `data` d LEFT JOIN `user` u on u.id = d.userId LEFT JOIN `like` l on l.dataId = d.id AND l.userId = d.userId WHERE d.`content` like ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setString(1, "%" + souContent + "%");
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            DataBean s = new DataBean();
                            s.id = rs.getInt("id");
                            s.type = rs.getInt("type");
                            s.userId = rs.getInt("userId");
                            s.imgUrl = rs.getString("imgUrl");
                            s.content = rs.getString("content");
                            s.likeNum = rs.getInt("likeNum");
                            UserBean userBean = new UserBean();
                            userBean.id = rs.getInt("userId");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = rs.getString("userPic");
                            s.userBean = userBean;
                            s.isLike = (rs.getInt("likeId") != 0);
                            dataList.add(s);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("Lance", "getSouData: e ----->" + e.getMessage());
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return dataList;
    }

    /**
     * 获取我的发布  查
     */
    public ArrayList<DataBean> getMyData(int userId) {
        //结果存放集合
        ArrayList<DataBean> dataList = new ArrayList<>();
        //MySQL 语句
        String sql;
        sql = "SELECT d.*, u.userName, u.userPhone, u.userPwd, u.userPic, IFNULL(l.id,0) as likeId FROM `data` d LEFT JOIN `user` u on u.id = d.userId LEFT JOIN `like` l on l.dataId = d.id AND l.userId = d.userId WHERE d.`userId` = ?";
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
                            DataBean s = new DataBean();
                            s.id = rs.getInt("id");
                            s.type = rs.getInt("type");
                            s.userId = rs.getInt("userId");
                            s.imgUrl = rs.getString("imgUrl");
                            s.content = rs.getString("content");
                            s.likeNum = rs.getInt("likeNum");
                            UserBean userBean = new UserBean();
                            userBean.id = rs.getInt("userId");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = rs.getString("userPic");
                            s.userBean = userBean;
                            s.isLike = (rs.getInt("likeId") != 0);
                            dataList.add(s);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return dataList;
    }

    /**
     * 获取我的收藏  查
     */
    public ArrayList<DataBean> getMyLikeData(int userId) {
        //结果存放集合
        ArrayList<DataBean> dataList = new ArrayList<>();
        //MySQL 语句
        String sql;
        sql = "SELECT d.*, u.userName, u.userPhone, u.userPwd, u.userPic, IFNULL(l.id, 0) as likeId FROM `like` l LEFT JOIN data d on l.dataId = d.id LEFT JOIN `user` u on d.userId = u.id WHERE l.userId = ?";
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
                            DataBean s = new DataBean();
                            s.id = rs.getInt("id");
                            s.type = rs.getInt("type");
                            s.userId = rs.getInt("userId");
                            s.imgUrl = rs.getString("imgUrl");
                            s.content = rs.getString("content");
                            s.likeNum = rs.getInt("likeNum");
                            UserBean userBean = new UserBean();
                            userBean.id = rs.getInt("userId");
                            userBean.userName = rs.getString("userName");
                            userBean.userPhone = rs.getString("userPhone");
                            userBean.userPwd = rs.getString("userPwd");
                            userBean.userPic = rs.getString("userPic");
                            s.userBean = userBean;
                            s.isLike = true;
                            dataList.add(s);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return dataList;
    }

    /**
     * 获取我是否收藏了该作品 查
     */
    public LikeBean getLikeData(int userId, int dataId) {
        //结果存放集合
        LikeBean like = null;
        //MySQL 语句
        String sql = "SELECT * FROM `like` WHERE `userId` = ? AND `dataId` = ?";
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        try {
            if (conn != null && (!conn.isClosed())) {
                ps = (PreparedStatement) conn.prepareStatement(sql);
                if (ps != null) {
                    ps.setInt(1, userId);
                    ps.setInt(2, dataId);
                    rs = ps.executeQuery();
                    if (rs != null) {
                        while (rs.next()) {
                            like = new LikeBean();
                            like.id = rs.getInt("id");
                            like.userId = rs.getInt("userId");
                            like.dataId = rs.getInt("dataId");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps, rs);//关闭相关操作
        return like;
    }

    /**
     * 向数据库插入数据  增（添加喜欢）
     */
    public int insertLike(LikeBean likeBean) {
        int result = -1;
        if (likeBean != null) {
            //获取链接数据库对象
            conn = DBOpenHelper.getConn();
            //MySQL 语句
            String sql = "INSERT INTO `like` (dataId,userId) VALUES (?,?)";
            try {
                if ((conn != null) && (!conn.isClosed())) {
                    ps = (PreparedStatement) conn.prepareStatement(sql);
                    ps.setInt(1, likeBean.dataId);
                    ps.setInt(2, likeBean.userId);
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
     * 修改数据库中某个对象的状态  改
     */
    public int updateData(int likeNum, int id) {
        int result = -1;
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        //MySQL 语句
        String sql = "UPDATE `data` set likeNum = ? WHERE `id` = ?";
        try {
            boolean closed = conn.isClosed();
            if (conn != null && (!closed)) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, likeNum);//第一个参数likeNum 一定要和上面SQL语句字段顺序一致
                ps.setInt(2, id);//第二个参数 id 一定要和上面SQL语句字段顺序一致
                result = ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }

    /**
     * 删除数据 删
     */
    public int delLikeData(int dataId, int userId) {
        int result = -1;
        //获取链接数据库对象
        conn = DBOpenHelper.getConn();
        //MySQL 语句
        String sql = "DELETE FROM `like` WHERE dataId = ? AND userId = ?";
        try {
            boolean closed = conn.isClosed();
            if ((conn != null) && (!closed)) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, dataId);
                ps.setInt(2, userId);
                result = ps.executeUpdate();//返回1 执行成功
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBOpenHelper.closeAll(conn, ps);//关闭相关操作
        return result;
    }

}