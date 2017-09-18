package com.helper.west2ol.fzuhelper.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.json.JSONException;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zsq on 16-8-22.
 */
@Entity
public class FDScore {
    private static final String JSON_NAME = "FDScoreName";
    private static final String JSON_JIDIAN = "FDScoreJidian";
    private static final String JSON_XUEFENG = "FDScoreXuefen";
    private static final String JSON_SCORE = "FDScoreScore";
    private static final String JSON_YEAR = "FDScoreYear";
    private static final String JSON_XUENIAN = "FDScoreXuenian";

    @Id(autoincrement = true)
    private Long fdScoreId;


    private String name="";
    private String jidian="";
    private String xuefen="";
    private String score="";

    private int year;
    private int xuenian;
    @Unique()
    private String unique;

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public Long getFdScoreId() {
        return fdScoreId;
    }

    public void setFdScoreId(Long fdScoreId) {
        this.fdScoreId = fdScoreId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getXuenian() {
        return xuenian;
    }

    public void setXuenian(int xuenian) {
        this.xuenian = xuenian;
    }

    public FDScore(){}

    public FDScore(JSONObject json) throws JSONException {
        name = json.getString(JSON_NAME);
        jidian = json.getString(JSON_JIDIAN);
        xuefen = json.getString(JSON_XUEFENG);
        score = json.getString(JSON_SCORE);
        year = json.getInt(JSON_YEAR);
        xuenian = json.getInt(JSON_XUENIAN);
    }

    @Generated(hash = 2126133735)
    public FDScore(Long fdScoreId, String name, String jidian, String xuefen,
            String score, int year, int xuenian, String unique) {
        this.fdScoreId = fdScoreId;
        this.name = name;
        this.jidian = jidian;
        this.xuefen = xuefen;
        this.score = score;
        this.year = year;
        this.xuenian = xuenian;
        this.unique = unique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getJidian() {
        return jidian;
    }

    public void setJidian(String jidian) {
        this.jidian = jidian;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_NAME, name);
        json.put(JSON_JIDIAN, jidian);
        json.put(JSON_SCORE, score);
        json.put(JSON_XUENIAN, xuenian);
        json.put(JSON_XUEFENG, xuefen);
        json.put(JSON_YEAR, year);
        return json;
    }
}
