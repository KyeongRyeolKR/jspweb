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

/**
 * 공지사항 게시글 상세보기를 위한 컨트롤러
 */
@WebServlet("/notice/detail")
public class NoticeDetailController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        String sql = "SELECT * FROM NOTICE WHERE ID=?";

        try {
            Class.forName(PrivateInfo.driver);
            Connection con = DriverManager.getConnection(PrivateInfo.url, PrivateInfo.uid, PrivateInfo.pwd);
            PreparedStatement st = con.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            rs.next();
            String title = rs.getString("TITLE");
            String writerId = rs.getString("WRITER_ID");
            Date regDate = rs.getDate("REGDATE");
            String hit = rs.getString("HIT");
            String files = rs.getString("FILES");
            String content = rs.getString("CONTENT");

            // Notice 엔티티 클래스로 객체를 request 저장소에 심는다.
            Notice notice = new Notice(id, title, writerId, regDate, hit, files, content);
            request.setAttribute("n", notice);

            /*
            // request 저장소에 DB에서 얻어온 값을 심는다.
            request.setAttribute("title", title);
            request.setAttribute("writerId", writerId);
            request.setAttribute("regDate", regDate);
            request.setAttribute("hit", hit);
            request.setAttribute("files", files);
            request.setAttribute("content", content);
            */

            rs.close();
            st.close();
            con.close();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // redirect -> 단순히 페이지를 옮기고 싶을 때 사용
        // forward -> 하고 있던 작업을 그대로 서블릿으로 옮기고 싶을 때 사용
        // 심어준 값을 그대로 detail.jsp로 넘겨준다. (forward)
        request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp").forward(request, response);

    }
}
