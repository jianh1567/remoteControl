package com.wind.control.util;

import android.bluetooth.le.ScanRecord;
import android.os.ParcelUuid;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by  Huanglinqing on 2018/9/4/004.
 */

public class ScanRecordUtil {


    /**
     * Returns a string composed from a {@link SparseArray}.
     */
    static String toString(SparseArray<byte[]> array) {
        if (array == null) {
            return "null";
        }
        if (array.size() == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for (int i = 0; i < array.size(); ++i) {
            buffer.append(array.keyAt(i)).append("=").append(Arrays.toString(array.valueAt(i)));
        }
        buffer.append('}');
        return buffer.toString();
    }


    /**
     * Returns a string composed from a {@link Map}.
     */
    static <T> String toString(Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        Iterator<Map.Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<T, byte[]> entry = it.next();
            Object key = entry.getKey();
            buffer.append(key).append("=").append(Arrays.toString(map.get(key)));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    private static final String TAG = "ScanRecordUtil";

    // The following data type values are assigned by Bluetooth SIG.
    // For more details refer to Bluetooth 4.1 specification, Volume 3, Part C, Section 18.
    private static final int DATA_TYPE_FLAGS = 0x01;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL = 0x02;
    private static final int DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE = 0x03;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL = 0x04;
    private static final int DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE = 0x05;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL = 0x06;
    private static final int DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE = 0x07;
    private static final int DATA_TYPE_LOCAL_NAME_SHORT = 0x08;
    private static final int DATA_TYPE_LOCAL_NAME_COMPLETE = 0x09;
    private static final int DATA_TYPE_TX_POWER_LEVEL = 0x0A;
    private static final int DATA_TYPE_SERVICE_DATA = 0x16;
    private static final int DATA_TYPE_MANUFACTURER_SPECIFIC_DATA = 0xFF;

    // Flags of the advertising data.
    private final int mAdvertiseFlags;

    @Nullable
    private final List<ParcelUuid> mServiceUuids;

    private final SparseArray<byte[]> mManufacturerSpecificData;

    private final Map<ParcelUuid, byte[]> mServiceData;

    // Transmission power level(in dB).
    private final int mTxPowerLevel;

    // Local name of the Bluetooth LE device.
    private final String mDeviceName;

    // Raw bytes of scan record.
    private final byte[] mBytes;

    /**
     * Returns the advertising flags indicating the discoverable mode and capability of the device.
     * Returns -1 if the flag field is not set.
     */
    public int getAdvertiseFlags() {
        return mAdvertiseFlags;
    }

    /**
     * Returns a list of service UUIDs within the advertisement that are used to identify the
     * bluetooth GATT services.
     */
    public List<ParcelUuid> getServiceUuids() {
        return mServiceUuids;
    }

    /**
     * Returns a sparse array of manufacturer identifier and its corresponding manufacturer specific
     * data.
     */
    public SparseArray<byte[]> getManufacturerSpecificData() {
        return mManufacturerSpecificData;
    }

    /**
     * Returns the manufacturer specific data associated with the manufacturer id. Returns
     * {@code null} if the {@code manufacturerId} is not found.
     */
    @Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        return mManufacturerSpecificData.get(manufacturerId);
    }

    /**
     * Returns a map of service UUID and its corresponding service data.
     */
    public Map<ParcelUuid, byte[]> getServiceData() {
        return mServiceData;
    }

    /**
     * Returns the service data byte array associated with the {@code serviceUuid}. Returns
     * {@code null} if the {@code serviceDataUuid} is not found.
     */
    @Nullable
    public byte[] getServiceData(ParcelUuid serviceDataUuid) {
        if (serviceDataUuid == null) {
            return null;
        }
        return mServiceData.get(serviceDataUuid);
    }

    /**
     * Returns the transmission power level of the packet in dBm. Returns {@link Integer#MIN_VALUE}
     * if the field is not set. This value can be used to calculate the path loss of a received
     * packet using the following equation:
     * <p>
     * <code>pathloss = txPowerLevel - rssi</code>
     */
    public int getTxPowerLevel() {
        return mTxPowerLevel;
    }

    /**
     * Returns the local name of the BLE device. The is a UTF-8 encoded string.
     */
    @Nullable
    public String getDeviceName() {
        return mDeviceName;
    }

    /**
     * Returns raw bytes of scan record.
     */
    public byte[] getBytes() {
        return mBytes;
    }

    private ScanRecordUtil(List<ParcelUuid> serviceUuids,
                           SparseArray<byte[]> manufacturerData,
                           Map<ParcelUuid, byte[]> serviceData,
                           int advertiseFlags, int txPowerLevel,
                           String localName, byte[] bytes) {
        mServiceUuids = serviceUuids;
        mManufacturerSpecificData = manufacturerData;
        mServiceData = serviceData;
        mDeviceName = localName;
        mAdvertiseFlags = advertiseFlags;
        mTxPowerLevel = txPowerLevel;
        mBytes = bytes;
    }

    /**
     * Parse scan record bytes to {@link ScanRecord}.
     * <p>
     * The format is defined in Bluetooth 4.1 specification, Volume 3, Part C, Section 11 and 18.
     * <p>
     * All numerical multi-byte entities and values shall use little-endian <strong>byte</strong>
     * order.
     * @param scanRecord The scan record of Bluetooth LE advertisement and/or scan response.
     * @hide
     */
    /* LEN	TYPE	VALUE  数据类型
     2	0x01	0x06
     3	0x03	0x2818
     12	0x16	281800E6D27EC42EA9B220
     10	0x09	4D6573684C69676874
     13	0xFF	0xA8015FFBB907DA78020004FF // 01A8公司名称,5FFBB907DA78MAC地址,FF040020为版本号
    */
    public static byte[] parseFromByte(byte[] scanRecord) {
        byte[] manufacturerDataBytes = new byte[124];

        if (scanRecord == null) {
            return null;
        }
        Log.e(TAG + "MYX23P", "进入parseFromBytes");
        int currentPos = 0;
        int advertiseFlag = -1;
        try {
            while (currentPos < scanRecord.length) {
                // length is unsigned int.
                int length = scanRecord[currentPos++] & 0xFF;
                if (length == 0) {
                    break;
                }
                // Note the length includes the length of the field type itself.
                int dataLength = length - 1;
                // fieldType is unsigned int.
                int fieldType = scanRecord[currentPos++] & 0xFF;
                Log.e(TAG + "MYX23P", "fieldType: " + fieldType);
                switch (fieldType) {
                    case DATA_TYPE_FLAGS:
                        //0x01
                        advertiseFlag = scanRecord[currentPos] & 0xFF;
                        Log.e(TAG + "MYX23P", "fieldType: " + fieldType + ",0x01: " + advertiseFlag);
                        break;
                    case DATA_TYPE_SERVICE_UUIDS_16_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_16_BIT_COMPLETE:
                        //0x03
                        break;
                    case DATA_TYPE_SERVICE_UUIDS_32_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_32_BIT_COMPLETE:
                    case DATA_TYPE_SERVICE_UUIDS_128_BIT_PARTIAL:
                    case DATA_TYPE_SERVICE_UUIDS_128_BIT_COMPLETE:
                    case DATA_TYPE_LOCAL_NAME_SHORT:
                    case DATA_TYPE_LOCAL_NAME_COMPLETE:
                        String localName = new String(
                                extractBytes(scanRecord, currentPos, dataLength));
                        Log.e(TAG + "MYX23P", "fieldType: " + fieldType + ",0xf9: " + localName);
                        break;
                    case DATA_TYPE_TX_POWER_LEVEL:
                    case DATA_TYPE_SERVICE_DATA:
                        //0x16
                        break;
                    case DATA_TYPE_MANUFACTURER_SPECIFIC_DATA:
                        int manufacturerId = ((scanRecord[currentPos + 1] & 0xFF) << 8) +
                                (scanRecord[currentPos] & 0xFF);
                        manufacturerDataBytes = extractBytes(scanRecord, currentPos,dataLength);
//                        manufacturerData.put(manufacturerId, manufacturerDataBytes);
                        Log.e(TAG + "MYX23P", "fieldType: " + fieldType + ",0xff: " + bytesToHex(manufacturerDataBytes));
                        break;
                    default:
                        // Just ignore, we don't handle such data type.
                        break;
                }
                currentPos += dataLength;
            }

            return manufacturerDataBytes;
        } catch (Exception e) {
            Log.e(TAG, "unable to parse scan record: " + Arrays.toString(scanRecord));
            // As the record is invalid, ignore all the parsed results for this packet
            // and return an empty record with raw scanRecord bytes in results
            return manufacturerDataBytes;
        }
    }


    @Override
    public String toString() {
        return "ScanRecord [mAdvertiseFlags=" + mAdvertiseFlags + ", mServiceUuids=" + mServiceUuids
                + ", mManufacturerSpecificData=" + ScanRecordUtil.toString(mManufacturerSpecificData)
                + ", mServiceData=" + ScanRecordUtil.toString(mServiceData)
                + ", mTxPowerLevel=" + mTxPowerLevel + ", mDeviceName=" + mDeviceName + "]";
    }

    /**
     * byte数组转化为string
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // Parse service UUIDs.
    private static int parseServiceUuid(byte[] scanRecord, int currentPos, int dataLength,
                                        int uuidLength, List<ParcelUuid> serviceUuids) {
        while (dataLength > 0) {
            byte[] uuidBytes = extractBytes(scanRecord, currentPos,
                    uuidLength);
            serviceUuids.add(parseUuidFrom(uuidBytes));
            dataLength -= uuidLength;
            currentPos += uuidLength;

        }
        return currentPos;
    }

    // Helper method to extract bytes from byte array.
    private static byte[] extractBytes(byte[] scanRecord, int start, int length) {
        byte[] bytes = new byte[length];
        System.arraycopy(scanRecord, start, bytes, 0, length);
        return bytes;
    }

    /**
     * 转化方法
     * @param uuidBytes
     * @return
     */
    public static ParcelUuid parseUuidFrom(byte[] uuidBytes) {
        if (uuidBytes == null) {
            throw new IllegalArgumentException("uuidBytes cannot be null");
        }
        int length = uuidBytes.length;
        if (length != 16 && length != 32 &&
                length != 128) {
            throw new IllegalArgumentException("uuidBytes length invalid - " + length);
        }

        // Construct a 128 bit UUID.
        if (length == 128) {
            ByteBuffer buf = ByteBuffer.wrap(uuidBytes).order(ByteOrder.LITTLE_ENDIAN);
            long msb = buf.getLong(8);
            long lsb = buf.getLong(0);
            return new ParcelUuid(new UUID(msb, lsb));
        }

        // For 16 bit and 32 bit UUID we need to convert them to 128 bit value.
        // 128_bit_value = uuid * 2^96 + BASE_UUID
        long shortUuid;
        if (length == 16) {
            shortUuid = uuidBytes[0] & 0xFF;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
        } else {
            shortUuid = uuidBytes[0] & 0xFF ;
            shortUuid += (uuidBytes[1] & 0xFF) << 8;
            shortUuid += (uuidBytes[2] & 0xFF) << 16;
            shortUuid += (uuidBytes[3] & 0xFF) << 24;
        }
        long msb = BASE_UUID.getUuid().getMostSignificantBits() + (shortUuid << 32);
        long lsb = BASE_UUID.getUuid().getLeastSignificantBits();
        return new ParcelUuid(new UUID(msb, lsb));
    }

    public static final ParcelUuid BASE_UUID =
            ParcelUuid.fromString("00000000-0000-1000-8000-00805F9B34FB");


}
