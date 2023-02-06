package com.newlecture.web.controller.admin.notice;

import com.newlecture.web.entity.NoticeView;
import com.newlecture.web.service.NoticeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 관리자 공지사항 게시글 목록 보기를 위한 컨트롤러
 */
@WebServlet("/admin/board/notice/list")
public class ListController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String[] openIds = request.getParameterValues("open-id");
        String[] delIds = request.getParameterValues("del-id");
        String cmd = request.getParameter("cmd");
        String ids_ = request.getParameter("ids");
        String[] ids = ids_.trim().split(" ");

        NoticeService service = new NoticeService();
        switch (cmd){
            case "일괄공개":
                for(String openId : openIds){
                    System.out.printf("open id = %s\n", openId);
                }

                List<String> oids = Arrays.asList(openIds);
                List<String> cids = new ArrayList<>(Arrays.asList(ids));
                cids.removeAll(oids);

                for(int i=0; i<ids.length; i++){
                    // 1. 현재 id가 open된 상태냐
                    if(oids.contains(ids[i])){
                        // pub 필드 -> 1 변경
                    } else {
                        // pub 필드 -> 0 변경
                    }
                }

                // Transaction 처리 ( pub을 1과 0으로 변경하는것을 한번에 동작하는 것처럼 )
                service.pubNoticeAll(oids, cids);

                break;
            case "일괄삭제":

                int[] ids1 = new int[delIds.length];
                for(int i=0; i<delIds.length; i++){
                    ids1[i] = Integer.parseInt(delIds[i]);
                }
                int result = service.deleteNoticeAll(ids1);
                break;
        }

        response.sendRedirect("list");

    }

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
        List<NoticeView> list = service.getNoticeList(field, query, page);
        int count = service.getNoticeCount(field, query);

        request.setAttribute("list", list);
        request.setAttribute("count", count);

        request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/list.jsp").forward(request, response);

    }
}
