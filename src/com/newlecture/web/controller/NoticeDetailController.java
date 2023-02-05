package com.newlecture.web.controller;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.info.PrivateInfo;
import com.newlecture.web.service.NoticeService;

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

        NoticeService service = new NoticeService();

        Notice notice = service.getNotice(id);

        request.setAttribute("n", notice);

        // redirect -> 단순히 페이지를 옮기고 싶을 때 사용
        // forward -> 하고 있던 작업을 그대로 서블릿으로 옮기고 싶을 때 사용
        // 심어준 값을 그대로 detail.jsp로 넘겨준다. (forward)
        request.getRequestDispatcher("/WEB-INF/view/notice/detail.jsp").forward(request, response);

    }
}
