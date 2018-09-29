package com.wind.control.util;

/**
 * @author wangtongming
 * @create 2018/9/14
 * @Describe
 */
public class BleProtocol {

    /* device type */
    private static final int DEV_TYPE_LED = 0x1;
    private static final int DEV_TYPE_SOCKET = 0x2;

    private static byte H_word(int h) {
        return (byte) ((h & 0xff00) >> 8);
    }
    private static byte L_word(int l) {
        return (byte) (l & 0xff);
    }
    /*********************************发送命令模块*******************************************************/
    // 0 - 0xffff
    // n / 255 * 65535,发送灯的亮度控制命令
    public static byte[] BleSendLightness(int ln){
        byte[] b = new byte[6];
        b[0] = 0x1;
        b[1] = 0x4c; //type L
        b[2] = (byte) 0x82; // type H
        b[3] = 0x2; //len
        b[4] = L_word(ln);
        b[5] = H_word(ln);
        return b;
    }

    //发送开关控制命令
    public static byte[] BleSendOnOff(boolean onOff){
        byte[] b = new byte[5];
        b[0] = 0x1;
        b[1] = 0x02; //type L
        b[2] = (byte) 0x82; // type H
        b[3] = 0x1; //len
        b[4] = (byte)(onOff ? 1 : 0x0);
        return b;
    }

    // 800 - 20000, 0x320 - 0x4E20
    //发送色温控制命令
    public static byte[] BleSendTemperature(int temperature){
        byte[] b = new byte[6];
        b[0] = 0x1;
        b[1] = 0x5e; //type L
        b[2] = (byte) 0x82; // type H
        b[3] = 0x2; //len
        b[4] = L_word(temperature);
        b[5] = H_word(temperature);
        return b;
    }

    //0 - 0xffff
    //发送场景控制命令，如阅读模式，休眠模式等
    public static byte[] BleSendScene(int scene){
        byte[] b = new byte[6];
        b[0] = 0x1;
        b[1] = 0x5e; //type L
        b[2] = (byte) 0x82; // type H
        b[3] = 0x2; //len
        b[4] = L_word(scene);
        b[5] = H_word(scene);
        return b;
    }

    /*********************************接收命令模块*******************************************************/

    private static int lightness = 1;
    private static int temperature = 800;
    private static int scene;

    //接收灯的亮度调节命令
    public static int getBLELedLightness(byte msg[]) {
        if (msg.length == 11) {
            lightness = ComByte(msg[4], msg[5]);
        }
        return lightness;
    }

    //接收色温的命令
    public static int getBLELedTemperature(byte msg[]) {
        if (msg.length == 11) {
            temperature = ComByte(msg[9], msg[10]);
        }
        return temperature;
    }

    //接收插座的命令
    private static void BLE_msg_socket_handler(byte msg[]) {
        if (msg.length != 5) {
            return;
        }
        scene = msg[5];
    }

    public static int ComByte(byte n1, byte n2) {
        return (n1 & 0xff) | ((n2 & 0xff) << 8);
    }

}
