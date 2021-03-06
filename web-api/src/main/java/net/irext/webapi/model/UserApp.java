package net.irext.webapi.model;

public class UserApp {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.app_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String appName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.admin_id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private Integer adminId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.admin_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String adminName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.app_type
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private Byte appType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.android_package_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String androidPackageName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.android_signature
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String androidSignature;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.ios_identity
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String iosIdentity;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.is_debug
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private Byte isDebug;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.app_key
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String appKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.app_secret
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String appSecret;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_app.update_time
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    private String updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.id
     *
     * @return the value of user_app.id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.id
     *
     * @param id the value for user_app.id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.app_name
     *
     * @return the value of user_app.app_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAppName() {
        return appName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.app_name
     *
     * @param appName the value for user_app.app_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.admin_id
     *
     * @return the value of user_app.admin_id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.admin_id
     *
     * @param adminId the value for user_app.admin_id
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.admin_name
     *
     * @return the value of user_app.admin_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.admin_name
     *
     * @param adminName the value for user_app.admin_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.app_type
     *
     * @return the value of user_app.app_type
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public Byte getAppType() {
        return appType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.app_type
     *
     * @param appType the value for user_app.app_type
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.android_package_name
     *
     * @return the value of user_app.android_package_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAndroidPackageName() {
        return androidPackageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.android_package_name
     *
     * @param androidPackageName the value for user_app.android_package_name
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAndroidPackageName(String androidPackageName) {
        this.androidPackageName = androidPackageName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.android_signature
     *
     * @return the value of user_app.android_signature
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAndroidSignature() {
        return androidSignature;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.android_signature
     *
     * @param androidSignature the value for user_app.android_signature
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAndroidSignature(String androidSignature) {
        this.androidSignature = androidSignature;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.ios_identity
     *
     * @return the value of user_app.ios_identity
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getIosIdentity() {
        return iosIdentity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.ios_identity
     *
     * @param iosIdentity the value for user_app.ios_identity
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setIosIdentity(String iosIdentity) {
        this.iosIdentity = iosIdentity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.is_debug
     *
     * @return the value of user_app.is_debug
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public Byte getIsDebug() {
        return isDebug;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.is_debug
     *
     * @param isDebug the value for user_app.is_debug
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setIsDebug(Byte isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.app_key
     *
     * @return the value of user_app.app_key
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAppKey() {
        return appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.app_key
     *
     * @param appKey the value for user_app.app_key
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.app_secret
     *
     * @return the value of user_app.app_secret
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.app_secret
     *
     * @param appSecret the value for user_app.app_secret
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_app.update_time
     *
     * @return the value of user_app.update_time
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_app.update_time
     *
     * @param updateTime the value for user_app.update_time
     *
     * @mbggenerated Fri May 26 19:47:48 CST 2017
     */
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}