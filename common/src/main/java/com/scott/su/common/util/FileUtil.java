package com.scott.su.common.util;

import android.support.annotation.IntDef;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

public class FileUtil {
    public static final int FILE_SIZE_TYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int FILE_SIZE_TYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int FILE_SIZE_TYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int FILE_SIZE_TYPE_GB = 4;// 获取文件大小单位为GB的double值

    private static final long B_OF_KB = 1024;
    private static final long B_OF_MB = 1024 * B_OF_KB;
    private static final long B_OF_GB = 1024 * B_OF_MB;
    private static final long KB_OF_MB = 1024;
    private static final long KB_OF_GB = 1024 * KB_OF_MB;
    private static final long MB_OF_GB = 1024;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FILE_SIZE_TYPE_B,
            FILE_SIZE_TYPE_KB,
            FILE_SIZE_TYPE_MB,
            FILE_SIZE_TYPE_GB,})
    public @interface FileSizeType {
    }

    /**
     * 获取文件或文件夹指定单位的大小
     *
     * @param filePath 文件路径
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, @FileSizeType int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getFileSizeAutoFormat(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileSizeBytes
     * @return
     */
    public static String formatFileSize(long fileSizeBytes) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString;

        if (fileSizeBytes < B_OF_KB) {
            fileSizeString = df.format((double) fileSizeBytes) + "B";
        } else if (fileSizeBytes < B_OF_MB) {
            fileSizeString = df.format((double) fileSizeBytes / B_OF_KB) + "KB";
        } else if (fileSizeBytes < B_OF_GB) {
            fileSizeString = df.format((double) fileSizeBytes / B_OF_MB) + "MB";
        } else {
            fileSizeString = df.format((double) fileSizeBytes / B_OF_GB) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileSizeBytes
     * @param sizeType
     * @return
     */
    public static double formatFileSize(long fileSizeBytes, @FileSizeType int sizeType) {
        DecimalFormat df = new DecimalFormat("#.0");
        double fileSizeLong = 0;
        switch (sizeType) {
            case FILE_SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileSizeBytes));
                break;
            case FILE_SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileSizeBytes / B_OF_KB));
                break;
            case FILE_SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileSizeBytes / B_OF_MB));
                break;
            case FILE_SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileSizeBytes / B_OF_GB));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

}