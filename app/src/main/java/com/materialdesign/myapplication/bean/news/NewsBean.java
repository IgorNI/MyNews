package com.materialdesign.myapplication.bean.news;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Description : 新闻实体类
 * Author : NI
 * Email : lifengni2015@gmail.com
 * Date : 2017/4/6
 */

public class NewsBean implements Serializable{

    /**
     * docid
     * */
    @SerializedName("docid")
    private String docid;

    /**
     * docid
     * */
    @SerializedName("title")
    private String title;
    /**
     * 笑内容
     * */
    @SerializedName("digest")
    private String digest;
    /**
     * 图片地址
     * */
    @SerializedName("imgsrc")
    private String imgsrc;
    /**
     * 来源
     * */
    @SerializedName("source")
    private String source;
    /**
     * 时间
     * */
    @SerializedName("ptime")
    private String ptime;
    /**
     * TAG
     * */
    @SerializedName("tag")
    private String tag;
    public boolean hasFadein = false;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setImgSrc(String imgSrc) {
        this.imgsrc = imgSrc;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPtime() {
        return ptime;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
