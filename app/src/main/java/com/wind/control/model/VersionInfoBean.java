package com.wind.control.model;

import java.io.Serializable;

/**
 * 作者：Created by luow on 2018/7/25
 * 注释：
 */
public class VersionInfoBean implements Serializable{

    /**
     * result : {"code":1000,"msg":"获取版本信息成功!","versioninfo":{"date":"2018-09-19","name":"entranceguardsystem.apk","force":"0","version":"1.0.0","url":"http://xxxx","content":"1.hjasoihwd;2.jasdhjew"}}
     */

    private int code;

    private String msg;

    private VersionInfo versioninfo;

    public void setResultCode(int code){
        this.code = code;
    }

    public int getResultCode(){
        return this.code;
    }

    public void setMsg(String message){
        this.msg = message;
    }

    public String getMsg(){
        return this.msg;
    }

    public VersionInfo getVersionInfo() {
        return this.versioninfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versioninfo = versionInfo;
    }

    public static class VersionInfo {
        /**
         * version : {"date":"2018-09-19","name":"entranceguardsystem.apk","force":"0","version":"1.0.0","url":"http://xxxx","content":"1.hjasoihwd;2.jasdhjew"}
         */

        private String date;

        private String name;

        private String force;

        private String version;

        private String url;

        private String content;

        public String getDate(){
            return this.date;
        }

        public void setDate(String date){
            this.date = date;
        }

        public String getName(){
            return this.name;
        }

        public void setName(String name){
            this.name = name;
        }

        public void setForce(String force){
            this.force = force;
        }

        public String getForce(){
            return this.force;
        }

        public void setVersion(String version){
            this.version = version;
        }

        public String getVersion(){
            return this.version;
        }

        public void setUrl(String url){
            this.url = url;
        }

        public String getUrl(){
            return this.url;
        }

        public void setContent(String content){
            this.content = content;
        }

        public String getContent(){
            return this.content;
        }

    }

}
