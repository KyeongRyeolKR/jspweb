package com.newlecture.web.service;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.info.PrivateInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 서비스 로직을 담당하는 클래스
 */
public class NoticeService {

    public List<NoticeView> getNoticeList() {
        return getNoticeList("title", "", 1);
    }

    public List<NoticeView> getNoticeList(int page) {
        return getNoticeList("title", "", page);
    }

    public List<NoticeView> getNoticeList(String field/*TITLE, WRITER_ID*/, String query/*A*/, int page) {

        List<NoticeView> list = new ArrayList<>();

        String sql = "SELECT * FROM (" +
                "    SELECT ROWNUM NUM, N.* " +
                "    FROM (SELECT * FROM NOTICE_VIEW WHERE " + field + " LIKE ? ORDER BY REGDATE DESC) N" +
                ") " +
                "WHERE NUM BETWEEN ? AND ?";

        // 1, 11, 21, 31 -> 1+(page-1)*10
        // 10, 20, 30, 40 -> page*10

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, "%" + query + "%");
            st.setInt(2, 1 + (page - 1) * 10);
            st.setInt(3, page * 10);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String writerId = rs.getString("WRITER_ID");
                Date regDate = rs.getDate("REGDATE");
                String hit = rs.getString("HIT");
                String files = rs.getString("FILES");
                //String content = rs.getString("CONTENT");
                int cmtCount = rs.getInt("CMT_COUNT");

                NoticeView notice = new NoticeView(id, title, writerId, regDate, hit, files, cmtCount);
                list.add(notice);
            }

            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public int getNoticeCount() {
        return getNoticeCount("title", "");
    }

    public int getNoticeCount(String field, String query) {

        int count = 0;

        String sql = "SELECT COUNT(ID) COUNT FROM (" +
                "    SELECT ROWNUM NUM, N.* " +
                "    FROM (SELECT * FROM NOTICE WHERE " + field + " LIKE ? ORDER BY REGDATE DESC) N" +
                ")";
        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, "%" + query + "%");
            ResultSet rs = st.executeQuery();

            if(rs.next()) {
                count = rs.getInt("count");
            }
            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public Notice getNotice(int id) {

        Notice notice = null;

        String sql = "SELECT * FROM NOTICE WHERE ID=?";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int nid = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String writerId = rs.getString("WRITER_ID");
                Date regDate = rs.getDate("REGDATE");
                String hit = rs.getString("HIT");
                String files = rs.getString("FILES");
                String content = rs.getString("CONTENT");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content);
            }

            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notice;
    }

    public Notice getNextNotice(int id) {

        Notice notice = null;

        String sql = "SELECT * FROM NOTICE " +
                "WHERE ID = ( " +
                "    SELECT ID FROM (SELECT * FROM NOTICE ORDER BY REGDATE) " +
                "    WHERE REGDATE > (SELECT REGDATE FROM NOTICE WHERE ID = ?) " +
                "    AND ROWNUM = 1 " +
                ")";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int nid = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String writerId = rs.getString("WRITER_ID");
                Date regDate = rs.getDate("REGDATE");
                String hit = rs.getString("HIT");
                String files = rs.getString("FILES");
                String content = rs.getString("CONTENT");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content);
            }

            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notice;
    }

    public Notice getPrevNotice(int id) {

        Notice notice = null;

        String sql = "SELECT * FROM NOTICE " +
                "WHERE ID = ( " +
                "    SELECT ID FROM (SELECT * FROM NOTICE ORDER BY REGDATE DESC) " +
                "    WHERE REGDATE < (SELECT REGDATE FROM NOTICE WHERE ID = ?) " +
                "    AND ROWNUM = 1 " +
                ")";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                int nid = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String writerId = rs.getString("WRITER_ID");
                Date regDate = rs.getDate("REGDATE");
                String hit = rs.getString("HIT");
                String files = rs.getString("FILES");
                String content = rs.getString("CONTENT");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content);
            }

            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return notice;
    }

}
