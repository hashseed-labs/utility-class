package com.utility;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * IO 工具类（JDK 8）
 */
public final class IoUtil {

    private static final int BUFFER_SIZE = 4096;

    private IoUtil() {
    }

    /**
     * 安静关闭
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception ignore) {
            // ignore
        }
    }

    /**
     * 读取流为字节数组
     */
    public static byte[] readBytes(InputStream in) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream 不能为空");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            copy(in, out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("读取输入流失败", e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 读取流为字符串（UTF-8）
     */
    public static String readString(InputStream in) {
        return readString(in, StandardCharsets.UTF_8);
    }

    /**
     * 读取流为字符串
     */
    public static String readString(InputStream in, Charset charset) {
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        return new String(readBytes(in), cs);
    }

    /**
     * 读取 Reader 为字符串
     */
    public static String readString(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader 不能为空");
        }
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        try {
            int len;
            while ((len = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("读取 Reader 失败", e);
        } finally {
            closeQuietly(reader);
        }
    }

    /**
     * 拷贝输入流到输出流
     *
     * @return 拷贝字节数
     */
    public static long copy(InputStream in, OutputStream out) throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException("流不能为空");
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        long total = 0;
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            total += len;
        }
        out.flush();
        return total;
    }

    /**
     * InputStream 转 Reader
     */
    public static Reader toReader(InputStream in, Charset charset) {
        if (in == null) {
            throw new IllegalArgumentException("InputStream 不能为空");
        }
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        return new InputStreamReader(in, cs);
    }
}
