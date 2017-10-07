package com.helper.west2ol.fzuhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by coder on 2017/10/7.
 */
@Entity
public class Exam {

    @Id(autoincrement = true)
    private long examId;

    private String name;
    private String xuefen;
    private String teacher;
    private String address;
    private String zuohao;

    public Exam(){}
    
    public Exam(String name, String xuefen, String teacher, String address, String zuohao) {
        this.name = name;
        this.xuefen = xuefen;
        this.teacher = teacher;
        this.address = address;
        this.zuohao = zuohao;
    }

    @Generated(hash = 1243453165)
    public Exam(long examId, String name, String xuefen, String teacher, String address,
            String zuohao) {
        this.examId = examId;
        this.name = name;
        this.xuefen = xuefen;
        this.teacher = teacher;
        this.address = address;
        this.zuohao = zuohao;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExamId() {
        return examId;
    }

    public void setExamId(long examId) {
        this.examId = examId;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZuohao() {
        return zuohao;
    }

    public void setZuohao(String zuohao) {
        this.zuohao = zuohao;
    }
}
