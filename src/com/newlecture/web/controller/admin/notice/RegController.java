package com.newlecture.web.controller.admin.notice;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.service.NoticeService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.List;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 파일 크기가 1MB가 넘으면 임시파일을 씀
        maxFileSize = 1024 * 1024 * 50,      // 하나의 파일 크기(50MB)
        maxRequestSize = 1024 * 1024 * 50 * 5  // 전체 파일 크기(250MB)
)
@WebServlet("/admin/board/notice/reg")
public class RegController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/view/admin/board/notice/reg.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String title = request.getParameter("title");

        System.out.print("title: ");
        System.out.println(title);

        String content = request.getParameter("content");

        String isOpen = request.getParameter("open");
        boolean pub = false;
        if (isOpen != null) {
            pub = true;
        }

        Collection<Part> parts = request.getParts();
        StringBuilder builder = new StringBuilder();
        for (Part p : parts) {
            if (!p.getName().equals("file")) continue;
            if (p.getSize() == 0) continue;

            String fileName = p.getSubmittedFileName();
            builder.append(fileName);
            builder.append(",");
            InputStream fis = p.getInputStream();
            String realPath = request.getServletContext().getRealPath("/upload");

            // realPath에 디렉토리가 있는지 확인 및 없으면 디렉토리 생성
            File path = new File(realPath);
            if (path.exists()) {
                path.mkdirs();
            }

            
            String filePath = realPath + File.separator + fileName;
            FileOutputStream fos = new FileOutputStream(filePath);

            byte[] buf = new byte[1024];
            int size = 0;
            while ((size = fis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            fis.close();
        }
        builder.delete(builder.length() - 1, builder.length()); // 마지막 파일 구분자 ","를 제거해줌


        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setPub(pub);
        notice.setWriterId("newlec");
        notice.setFiles(builder.toString());

        NoticeService service = new NoticeService();
        service.insertNotice(notice);

        response.sendRedirect("list");

    }
}
