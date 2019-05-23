package com.ruihua.libvideorecord;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;

/**
 * describe ： 转码工具类
 *
 * @author : Yich
 * date: 2019/4/11
 */
public class TranscordUtils {

    public static int getColorFormat() {
        int colorFormat = -100;
        int[] formats = getMediaCodecList();
        for (int format : formats) {
            switch (format) {
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                    colorFormat = format;
                    break;
                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                    colorFormat = format;
                    break;
                default:
                    break;
            }
        }
        //默认值
        if (colorFormat <= 0) {
            colorFormat = MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
        }
        return colorFormat;
    }

    /**
     * //获取解码器列表
     *
     * @return 解码器列表
     */
    private static int[] getMediaCodecList() {
        //获取解码器列表
        int numCodecs = MediaCodecList.getCodecCount();
        MediaCodecInfo codecInfo = null;
        for (int i = 0; i < numCodecs && codecInfo == null; i++) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            String[] types = info.getSupportedTypes();
            boolean found = false;
            //轮训所要的解码器
            for (int j = 0; j < types.length && !found; j++) {
                if ("video/avc".equals(types[j])) {
                    found = true;
                }
            }
            if (!found) {
                continue;
            }
            codecInfo = info;
        }
        if (codecInfo == null) {
            return new int[]{};
        }
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType("video/avc");
        return capabilities.colorFormats;
    }

    /**
     * 转码
     *
     * @param inputWidth  宽度
     * @param inputHeight 高度
     * @param argb        照片数据
     * @param colorFormat 转成的格式
     * @return 转码后的数据
     */

    public static byte[] getNV12(int inputWidth, int inputHeight, int[] argb, int colorFormat) {
        byte[] yuv = new byte[inputWidth * inputHeight * 3 / 2];
        switch (colorFormat) {
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                encodeYUV420SP(yuv, argb, inputWidth, inputHeight);
                break;
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                break;
            default:
                yuv = null;
                break;
        }
        return yuv;
    }

    private static void encodeYUV420SP(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;
        int yIndex = 0;
        int uvIndex = frameSize;
        int r, g, b, y, u, v;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                r = (argb[index] & 0xff0000) >> 16;
                g = (argb[index] & 0xff00) >> 8;
                b = (argb[index] & 0xff) >> 0;
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                v = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                u = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                yuv420sp[yIndex++] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[uvIndex++] = (byte) ((v < 0) ? 0 : ((v > 255) ? 255 : v));
                    yuv420sp[uvIndex++] = (byte) ((u < 0) ? 0 : ((u > 255) ? 255 : u));
                }
                index++;
            }
        }
    }

    private static void encodeYUV420P(byte[] yuv420sp, int[] argb, int width, int height) {
        final int frameSize = width * height;
        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + width * height / 4;
        int r, g, b, y, u, v;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                r = (argb[index] & 0xff0000) >> 16;
                g = (argb[index] & 0xff00) >> 8;
                b = (argb[index] & 0xff) >> 0;
                y = ((66 * r + 129 * g + 25 * b + 128) >> 8) + 16;
                v = ((-38 * r - 74 * g + 112 * b + 128) >> 8) + 128;
                u = ((112 * r - 94 * g - 18 * b + 128) >> 8) + 128;
                yuv420sp[yIndex++] = (byte) ((y < 0) ? 0 : ((y > 255) ? 255 : y));
                if (j % 2 == 0 && index % 2 == 0) {
                    yuv420sp[vIndex++] = (byte) ((u < 0) ? 0 : ((u > 255) ? 255 : u));
                    yuv420sp[uIndex++] = (byte) ((v < 0) ? 0 : ((v > 255) ? 255 : v));
                }
                index++;
            }
        }
    }

}
