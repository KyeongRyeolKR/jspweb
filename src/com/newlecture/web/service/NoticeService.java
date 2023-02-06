package com.newlecture.web.service;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.info.PrivateInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 공지사항 서비스 로직을 담당하는 클래스
 */
public class NoticeService {
    public int removeNoticeAll(int[] ids) {

        return 0;
    }

    public int pubNoticeAll(int[] oids, int[] cids) {

        List<String> oidsList = new ArrayList<>();
        for(int i=0; i<oids.length; i++){
            oidsList.add(String.valueOf(oids[i]));
        }

        List<String> cidsList = new ArrayList<>();
        for(int i=0; i<cids.length; i++){
            cidsList.add(String.valueOf(cids[i]));
        }

        return pubNoticeAll(oidsList, cidsList);
    }

    public int pubNoticeAll(List<String> oids, List<String> cids) {

        String oidsCSV = String.join(",", oids);
        String cidsCSV = String.join(",", cids);

        return pubNoticeAll(oidsCSV, cidsCSV);
    }

    public int pubNoticeAll(String oidsCSV, String cidsCSV) {

        int result = 0;

        String sqlOpen = String.format("UPDATE NOTICE SET PUB=1 WHERE ID IN (%s)", oidsCSV);
        String sqlClose = String.format("UPDATE NOTICE SET PUB=0 WHERE ID IN (%s)", cidsCSV);

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);

            Statement stOpen = con.createStatement();
            result += stOpen.executeUpdate(sqlOpen);

            Statement stClose = con.createStatement();
            result += stClose.executeUpdate(sqlClose);

            stOpen.close();
            stClose.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int insertNotice(Notice notice) {
        int result = 0;

        String sql = "INSERT INTO NOTICE(ID, TITLE, CONTENT, WRITER_ID, PUB, FILES) VALUES (NOTICE_SEQ.NEXTVAL, ?,?,?,?,?)";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, notice.getTitle());
            st.setString(2, notice.getContent());
            st.setString(3, notice.getWriterId());
            st.setBoolean(4, notice.getPub());
            st.setString(5, notice.getFiles());

            result = st.executeUpdate();

            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int deleteNotice(int id) {
        return 0;
    }

    public int updateNotice(Notice notice) {
        return 0;
    }

    public List<Notice> getNoticeNewestList() {
        return null;
    }

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
                boolean pub = rs.getBoolean("PUB");

                NoticeView notice = new NoticeView(id, title, writerId, regDate, hit, files, pub, cmtCount);
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

    public List<NoticeView> getNoticePubList(String field, String query, int page) {
        List<NoticeView> list = new ArrayList<>();

        String sql = "SELECT * FROM (" +
                "    SELECT ROWNUM NUM, N.* " +
                "    FROM (SELECT * FROM NOTICE_VIEW WHERE " + field + " LIKE ? ORDER BY REGDATE DESC) N" +
                ") " +
                "WHERE PUB=1 AND NUM BETWEEN ? AND ?";

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
                boolean pub = rs.getBoolean("PUB");

                NoticeView notice = new NoticeView(id, title, writerId, regDate, hit, files, pub, cmtCount);
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

            if (rs.next()) {
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
                boolean pub = rs.getBoolean("PUB");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
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
                boolean pub = rs.getBoolean("PUB");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
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
                boolean pub = rs.getBoolean("PUB");

                notice = new Notice(nid, title, writerId, regDate, hit, files, content, pub);
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

    public int deleteNoticeAll(int[] ids) {

        int result = 0;

        String params = "";

        for (int i = 0; i < ids.length; i++) {
            params += ids[i];
            if (i < ids.length - 1) {
                params += ",";
            }
        }

        String sql = "DELETE NOTICE WHERE ID IN (" + params + ")";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            Statement st = con.createStatement();
            result = st.executeUpdate(sql);

            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
