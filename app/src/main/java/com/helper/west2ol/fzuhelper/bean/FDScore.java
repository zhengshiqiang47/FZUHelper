package com.helper.west2ol.fzuhelper.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zsq on 16-8-22.
 */
public class FDScore {
    private static final String JSON_NAME = "FDScoreName";
    private static final String JSON_JIDIAN = "FDScoreJidian";
    private static final String JSON_XUEFENG = "FDScoreXuefen";
    private static final String JSON_SCORE = "FDScoreScore";
    private static final String JSON_YEAR = "FDScoreYear";
    private static final String JSON_XUENIAN = "FDScoreXuenian";


    private String name="";
    private String jidian="";
    private String xuefen="";
    private String score="";
    private int year;
    private int xuenian;

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
