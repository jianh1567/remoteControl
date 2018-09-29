package com.wind.control.okhttp.api;

/**
 * 作者：Created by luow on 2018/6/29
 * 注释：url接口
 */
public class Api {
    //测试服务器地址
    private static final String BASE_URL = "http://47.100.122.188:5000";

    public static final String LOGIN = BASE_URL + "/loginUserAPI/Login"; //登录接口
    public static final String OTHER_LOGIN = BASE_URL + "/loginUserAPI/otherLogin"; //验证码登录
    public static final String GET_PHONE_CODE = BASE_URL + "/userInfoAPI/getVerifcationCode1"; //获取验证码
    public static final String GET_LOGIN_PHONE_CODE = BASE_URL + "/loginUserAPI/getTokenCode"; //获取登录验证码
    public static final String REGISTER = BASE_URL + "/userInfoAPI/registerUser"; //注册
    public static final String BIND_DEVICE = BASE_URL + "/deviceAPI/bindDevice"; //绑定设备
    public static final String QUERY_DEVICE_LIST = BASE_URL + "/deviceAPI/queryDeviceList"; //设备查询
    public static final String DELETE_DEVICE = BASE_URL + "/deviceAPI/deleteDevice"; //删除设备
    public static final String PUB = BASE_URL + "/pubAPI/pub"; //发送码库
    public static final String PASSWORD = BASE_URL + "/user/password"; //找回密码/验证手机验证码
    public static final String EBIKE = BASE_URL + "/ebike"; //绑定设备
    public static final String WARN = BASE_URL + "/ebike/warn/"; //获取指定设备历史推送消息
    public static final String VERSION = BASE_URL + "/versionAPI/getVersionInfo"; //获取最新版本软件信息
    public static final String PACKAGE = BASE_URL + "/versionAPI/download"; //获取软件安装包

    public static final String ALL_DEVICE_REALTIME_INFO = BASE_URL + "/ebike/realtime";
    public static final String HISTORY_TRACK = BASE_URL + "/ebike/traces/";
    public static final String USER_INFO = BASE_URL + "/user";

    public static final String SAVE_SCENE_SETTING = BASE_URL + "/sceneAPI/sceneBind";

    public static final String QUERY_SCENE_LIST = BASE_URL + "/sceneAPI/querySceneList";
    public static final String DELETE_SCENE = BASE_URL + "/sceneAPI/delScene";
    public static final String OP_FEED_BACK = BASE_URL + "/userCtlAPI/opFeedback";//意见反馈
    public static final String GET_USER_INFO = BASE_URL + "/userCtlAPI/getUserInfo";//获取用户信息
    public static final String UPDATE_PROFILE_PICTURE = BASE_URL + "/userCtlAPI/updateProfilePicture";//修改头像
    public static final String UPDATE_USER_INFO = BASE_URL + "/userCtlAPI/updateUserInfo";//修改用户信息
    public static final String UPDATE_SCENE_SETTING = BASE_URL + "/sceneAPI/updateScene";
    public static final String ADD_TRIAD = BASE_URL + "/triadAPI/addTriad";//添加三元组信息
    public static final String QUERY_TRIAD = BASE_URL + "/triadAPI/queryTriad";//查询三元组信息

}
