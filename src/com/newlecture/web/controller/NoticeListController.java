package com.newlecture.web.controller;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.info.PrivateInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 게시글 목록 보기를 위한 컨트롤러
 */
@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Notice> list = new ArrayList<>();

        String sql = "SELECT * FROM NOTICE";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String title = rs.getString("TITLE");
                String writerId = rs.getString("WRITER_ID");
                Date regDate = rs.getDate("REGDATE");
                String hit = rs.getString("HIT");
                String files = rs.getString("FILES");
                String content = rs.getString("CONTENT");

                Notice notice = new Notice(id, title, writerId, regDate, hit, files, content);
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

        request.setAttribute("list", list);

        request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp").forward(request, response);

    }
}
