package com.wind.control.model;

/**
 * 作者：Created by luow on 2018/9/5
 * 注释：
 */
public class UserInfoBean {

    /**
     * msg : 获取用户信息成功
     * code : 1000
     * userinfo : {"birthday":"","headportrait":"http://saveuserheadimg.oss-cn-shanghai.aliyuncs.com/?Expires=1536143810&OSSAccessKeyId=LTAIasxf2JcQ88q7&Signature=BMOslrQs8f%2B6j1UL%2BodALcYaCa4%3D","sex":"","username":"DKv8UB"}
     */

    private String msg;
    private int code;
    private UserinfoBean userinfo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * birthday :
         * headportrait : http://saveuserheadimg.oss-cn-shanghai.aliyuncs.com/?Expires=1536143810&OSSAccessKeyId=LTAIasxf2JcQ88q7&Signature=BMOslrQs8f%2B6j1UL%2BodALcYaCa4%3D
         * sex :
         * username : DKv8UB
         */

        private String birthday;
        private String headportrait;
        private String sex;
        private String username;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHeadportrait() {
            return headportrait;
        }

        public void setHeadportrait(String headportrait) {
            this.headportrait = headportrait;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
