package com.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * 文件工具类（JDK 8）
 */
public final class FileUtil {

    private FileUtil() {
    }

    /**
     * 是否存在
     */
    public static boolean exists(String path) {
        return path != null && new File(path).exists();
    }

    /**
     * 是否为文件
     */
    public static boolean isFile(String path) {
        return path != null && new File(path).isFile();
    }

    /**
     * 是否为目录
     */
    public static boolean isDirectory(String path) {
        return path != null && new File(path).isDirectory();
    }

    /**
     * 获取文件名（含扩展名）
     */
    public static String getName(String path) {
        if (StringUtil.isBlank(path)) {
            return path;
        }
        File file = new File(path);
        return file.getName();
    }

    /**
     * 获取扩展名（不含点）；无扩展名返回空串
     */
    public static String getExtension(String path) {
        String name = getName(path);
        if (StringUtil.isBlank(name)) {
            return StringUtil.EMPTY;
        }
        int index = name.lastIndexOf('.');
        if (index < 0 || index == name.length() - 1) {
            return StringUtil.EMPTY;
        }
        return name.substring(index + 1);
    }

    /**
     * 读取文件为字符串（UTF-8）
     */
    public static String readString(String path) {
        return readString(path, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件为字符串
     */
    public static String readString(String path, Charset charset) {
        byte[] bytes = readBytes(path);
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        return new String(bytes, cs);
    }

    /**
     * 读取文件为字节数组
     */
    public static byte[] readBytes(String path) {
        File file = requireFile(path);
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            return IoUtil.readBytes(in);
        } catch (IOException e) {
            throw new IllegalArgumentException("读取文件失败: " + path, e);
        } finally {
            IoUtil.closeQuietly(in);
        }
    }

    /**
     * 写入字符串（UTF-8，覆盖）
     */
    public static void writeString(String path, String content) {
        writeString(path, content, StandardCharsets.UTF_8, false);
    }

    /**
     * 写入字符串
     *
     * @param append true 追加，false 覆盖
     */
    public static void writeString(String path, String content, Charset charset, boolean append) {
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        byte[] bytes = content == null ? new byte[0] : content.getBytes(cs);
        writeBytes(path, bytes, append);
    }

    /**
     * 写入字节数组（覆盖）
     */
    public static void writeBytes(String path, byte[] data) {
        writeBytes(path, data, false);
    }

    /**
     * 写入字节数组
     */
    public static void writeBytes(String path, byte[] data, boolean append) {
        if (StringUtil.isBlank(path)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalArgumentException("创建目录失败: " + parent.getAbsolutePath());
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            out.write(data == null ? new byte[0] : data);
            out.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("写入文件失败: " + path, e);
        } finally {
            IoUtil.closeQuietly(out);
        }
    }

    /**
     * 删除文件或空目录；不存在视为成功
     */
    public static boolean delete(String path) {
        if (StringUtil.isBlank(path)) {
            return false;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        try {
            return Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 复制文件
     */
    public static void copyFile(String source, String target) {
        byte[] bytes = readBytes(source);
        writeBytes(target, bytes, false);
    }

    private static File requireFile(String path) {
        if (StringUtil.isBlank(path)) {
            throw new IllegalArgumentException("文件路径不能为空");
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("文件不存在: " + path);
        }
        return file;
    }
}
