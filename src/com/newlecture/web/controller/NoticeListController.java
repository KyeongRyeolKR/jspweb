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
import java.util.ArrayList;
import java.util.List;

/**
 * 공지사항 게시글 목록 보기를 위한 컨트롤러
 */
@WebServlet("/notice/list")
public class NoticeListController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // list?f=title&q=a
        String field_ = request.getParameter("f");
        String query_ = request.getParameter("q");
        String page_ = request.getParameter("p");

        String field = "title";     // 기본값
        if(field_ != null && !field_.equals("")){
            field = field_;
        }

        String query = "";          // 기본값
        if(query_ != null && !query_.equals("")){
            query = query_;
        }

        int page = 1;          // 기본값
        if(page_ != null && !page_.equals("")){
            page = Integer.parseInt(page_);
        }

        NoticeService service = new NoticeService();
        List<Notice> list = service.getNoticeList(field, query, page);
        int count = service.getNoticeCount(field, query);

        request.setAttribute("list", list);
        request.setAttribute("count", count);

        request.getRequestDispatcher("/WEB-INF/view/notice/list.jsp").forward(request, response);

    }
}
