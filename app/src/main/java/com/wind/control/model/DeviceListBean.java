package com.wind.control.model;

import java.util.List;

/**
 * 作者：Created by luow on 2018/8/23
 * 注释：
 */
public class DeviceListBean {

    /**
     * code : 1000
     * devices : [{"ir_devicename":"","datatime":"2018-08-24 16:50:13","brandid":0,"name":"七彩灯","id":11,"type":"1","title":"客厅","mac":"78:DA:07:B8:76:0A","categoryid":0},{"ir_devicename":"IR_1","datatime":"2018-08-24 16:50:24","brandid":9,"name":"美的","id":12,"type":"2","title":"卧室","mac":"","categoryid":1}]
     * msg : 查询成功
     */

    private String code;
    private String msg;
    private List<DevicesBean> devices;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DevicesBean> getDevices() {
        return devices;
    }

    public void setDevices(List<DevicesBean> devices) {
        this.devices = devices;
    }

    public static class DevicesBean {
        /**
         * ir_devicename :
         * datatime : 2018-08-24 16:50:13
         * brandid : 0
         * name : 七彩灯
         * id : 11
         * type : 1
         * title : 客厅
         * mac : 78:DA:07:B8:76:0A
         * categoryid : 0
         */

        private String ir_devicename;
        private String datatime;
        private int brandid;
        private String name;
        private int id;
        private String type;
        private String title;
        private String mac;
        private int categoryid;

        public String getIr_devicename() {
            return ir_devicename;
        }

        public void setIr_devicename(String ir_devicename) {
            this.ir_devicename = ir_devicename;
        }

        public String getDatatime() {
            return datatime;
        }

        public void setDatatime(String datatime) {
            this.datatime = datatime;
        }

        public int getBrandid() {
            return brandid;
        }

        public void setBrandid(int brandid) {
            this.brandid = brandid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }
    }
}
