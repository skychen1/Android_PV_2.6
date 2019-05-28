package com.ruihua.face.recognition.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具
 */
public class DBUtil {
    //执行查询
    public static List<List<String>> executeQuery(Connection conn, String sql){
        if(conn == null){
            return null;
        }
        List<List<String>> resultList = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();
            while(rs.next()){
                List<String> lineList = new ArrayList<>();
                for(int i = 0; i < colNum; i++){
                    String colData = rs.getString(i);
                    lineList.add(colData);
                }
                resultList.add(lineList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }
    //执行插入、更新、删除等
    public static void executeUpdate(Connection conn, String sql){
        if(conn == null){
            return;
        }
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close(Connection conn){
        if(conn != null){
            try{
                conn.close();
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
}
