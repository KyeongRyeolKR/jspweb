package com.newlecture.web.entity;

import java.sql.Date;

public class NoticeView extends Notice {

    private int cmtCount;

    public NoticeView(){}

    public NoticeView(int id, String title, String writerId, Date regDate, String hit, String files, int cmtCount) {
        super(id, title, writerId, regDate, hit, files, "");
        this.cmtCount = cmtCount;
    }

    public int getCmtCount() {
        return cmtCount;
    }

    public void setCmtCount(int cmtCount) {
        this.cmtCount = cmtCount;
    }
}
