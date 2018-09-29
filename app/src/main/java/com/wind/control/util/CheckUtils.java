package com.wind.control.util;

/**
 * 检查用户输入数据工具类
 * Created by luow on 2016/8/26.
 */
public class CheckUtils {

    private static final String PHONE_NO_REG = "^0?(13[0-9]|14[5-8]|15[0-3,5-9]|16[6]|17[0-3,5-9]|18[0-9]|19[89])[0-9]{8}$";
    private static final String PASSWORD_REG = "^\\w{6,20}$";
    //    private static final String PASSWORD_REG = "^[a-zA-Z0-9_]{6,20}$"; 与上行的PASSWORD_REG等效
    private static final String PAY_PASSWORD_REG = "^[0-9]{6}$";
    private static final String EMAIL_REG = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    private static final String VERIFY_REG = "\\d{6}";
    private static final String ID_CARD_NUM_REG = "^(\\d{6})(19|20)(\\d{2})(1[0-2]|0[1-9])(0[1-9]|[1-2][0-9]|3[0-1])(\\d{3})(\\d|X|x)?$";

    /**
     * 检查数据是否为空
     *
     * @param content
     * @return
     */
    public static boolean isEmpty(String content) {
        return (null == content || "".equals(content) || " ".equals(content) || "null".equalsIgnoreCase(content));
    }

    /**
     * 检查手机号输入是否正确
     *
     * @param phoneNo
     * @return boolean
     */
    public static boolean isPhoneNum(String phoneNo) {
        if (isEmpty(phoneNo)) {
            return false;
        }
        return phoneNo.matches(PHONE_NO_REG);
    }

    /**
     * 检查密码输入是否正确
     *
     * @param pwd
     * @return boolean
     */
    public static boolean isPassword(String pwd) {
        if (isEmpty(pwd.trim())) {
            return false;
        }
        return pwd.matches(PASSWORD_REG);
    }

    /**
     * 检查支付密码输入是否正确
     *
     * @param pwd
     * @return boolean
     */
    public static boolean isPayPassword(String pwd) {
        if (isEmpty(pwd.trim())) {
            return false;
        }
        return pwd.matches(PAY_PASSWORD_REG);
    }

    /**
     * 检查邮箱是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (isEmpty(email.trim())) {
            return false;
        }
        return email.matches(EMAIL_REG);
    }

    /**
     * 检查验证码格式是否有误
     *
     * @param code
     * @return
     */
    public static boolean isVerifyCode(String code) {
        if (isEmpty(code)) {
            return false;
        }
        return code.matches(VERIFY_REG);
    }

    /**
     * 检查身份证号格式是否有误
     *
     * @param num
     * @return
     */
    public static boolean isIDCardNum(String num) {
        if (isEmpty(num)) {
            return false;
        }
        return num.matches(ID_CARD_NUM_REG);
    }
}
