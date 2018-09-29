package com.wind.control.model;

import java.util.List;

/**
 * 作者：Created by luow on 2018/9/14
 * 注释：
 */
public class SelectDeviceBean {

    /**
     * msg : 查询三元组成功
     * code : 1000
     * triadinfo : [{"triadTitle":"卧室的红外设备","phone":3,"datatime":"2018-09-13 17:27:00","id":3,"ProductKey":"a1StZOzzL3s","DeviceName":"IR_2"},{"triadTitle":"还打电话回电话","phone":4,"datatime":"2018-09-13 17:28:16","id":4,"ProductKey":"a1StZOzzL3s","DeviceName":"IR_2"},{"triadTitle":"厕所的灯","phone":5,"datatime":"2018-09-13 17:32:03","id":5,"ProductKey":"a1StZOzzL3s","DeviceName":"IR_2"}]
     */

    private String msg;
    private int code;
    private List<TriadinfoBean> triadinfo;

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

    public List<TriadinfoBean> getTriadinfo() {
        return triadinfo;
    }

    public void setTriadinfo(List<TriadinfoBean> triadinfo) {
        this.triadinfo = triadinfo;
    }

    public static class TriadinfoBean {
        /**
         * triadTitle : 卧室的红外设备
         * phone : 3
         * datatime : 2018-09-13 17:27:00
         * id : 3
         * ProductKey : a1StZOzzL3s
         * DeviceName : IR_2
         */

        private String triadTitle;
        private int phone;
        private String datatime;
        private int id;
        private String ProductKey;
        private String DeviceName;

        public String getTriadTitle() {
            return triadTitle;
        }

        public void setTriadTitle(String triadTitle) {
            this.triadTitle = triadTitle;
        }

        public int getPhone() {
            return phone;
        }

        public void setPhone(int phone) {
            this.phone = phone;
        }

        public String getDatatime() {
            return datatime;
        }

        public void setDatatime(String datatime) {
            this.datatime = datatime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProductKey() {
            return ProductKey;
        }

        public void setProductKey(String ProductKey) {
            this.ProductKey = ProductKey;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }
    }
}
