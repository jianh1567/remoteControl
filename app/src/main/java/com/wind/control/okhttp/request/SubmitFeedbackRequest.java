package com.wind.control.okhttp.request;

import java.util.List;

/**
 * 提交反馈
 * Created by luow on 2017/3/3.
 */

public class SubmitFeedbackRequest {

    /**
     * phone : 15026513389
     * userTitle : test
     * pbDescription : test
     * userImg : [{"imgStream":"jsdgfqwidehuqwe235r376e2134rjewdhgt34u"},{"imgStream":"23423wdfew45vdfrge5bgrgtb45bgdr434v"}]
     * userLink : 15028213921
     */

    private String phone;
    private String userTitle;
    private String pbDescription;
    private String userLink;
    private String token;
    private List<UserImgBean> userImg;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public String getPbDescription() {
        return pbDescription;
    }

    public void setPbDescription(String pbDescription) {
        this.pbDescription = pbDescription;
    }

    public String getUserLink() {
        return userLink;
    }

    public void setUserLink(String userLink) {
        this.userLink = userLink;
    }

    public List<UserImgBean> getUserImg() {
        return userImg;
    }

    public void setUserImg(List<UserImgBean> userImg) {
        this.userImg = userImg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserImgBean {
        /**
         * imgStream : jsdgfqwidehuqwe235r376e2134rjewdhgt34u
         */

        private String imgStream;

        public String getImgStream() {
            return imgStream;
        }

        public void setImgStream(String imgStream) {
            this.imgStream = imgStream;
        }
    }
}
