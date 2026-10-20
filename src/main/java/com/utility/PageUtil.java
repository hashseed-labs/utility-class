package com.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页计算工具类（JDK 8）
 */
public final class PageUtil {

    private PageUtil() {
    }

    /**
     * 计算偏移量 offset（从 0 开始）
     *
     * @param pageNum  页码，从 1 开始
     * @param pageSize 每页大小
     */
    public static int getOffset(int pageNum, int pageSize) {
        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        return (safePageNum - 1) * safePageSize;
    }

    /**
     * 计算总页数
     */
    public static int getTotalPages(long total, int pageSize) {
        int safePageSize = Math.max(pageSize, 1);
        if (total <= 0) {
            return 0;
        }
        return (int) ((total + safePageSize - 1) / safePageSize);
    }

    /**
     * 校正页码到合法范围 [1, totalPages]；无数据时返回 1
     */
    public static int correctPageNum(int pageNum, long total, int pageSize) {
        int totalPages = getTotalPages(total, pageSize);
        if (totalPages <= 0) {
            return 1;
        }
        if (pageNum < 1) {
            return 1;
        }
        return Math.min(pageNum, totalPages);
    }

    /**
     * 是否有上一页
     */
    public static boolean hasPrevious(int pageNum) {
        return pageNum > 1;
    }

    /**
     * 是否有下一页
     */
    public static boolean hasNext(int pageNum, long total, int pageSize) {
        return pageNum < getTotalPages(total, pageSize);
    }

    /**
     * 对内存列表做分页切片
     */
    public static <T> List<T> pageList(List<T> list, int pageNum, int pageSize) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        int from = getOffset(safePageNum, safePageSize);
        if (from >= list.size()) {
            return Collections.emptyList();
        }
        int to = Math.min(from + safePageSize, list.size());
        return new ArrayList<T>(list.subList(from, to));
    }

    /**
     * 构建分页结果
     */
    public static <T> PageResult<T> of(List<T> records, long total, int pageNum, int pageSize) {
        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.max(pageSize, 1);
        long safeTotal = Math.max(total, 0L);
        return new PageResult<T>(
                records == null ? Collections.<T>emptyList() : records,
                safeTotal,
                safePageNum,
                safePageSize,
                getTotalPages(safeTotal, safePageSize)
        );
    }

    /**
     * 对内存列表分页并构建结果
     */
    public static <T> PageResult<T> page(List<T> all, int pageNum, int pageSize) {
        List<T> source = all == null ? Collections.<T>emptyList() : all;
        int safePageNum = correctPageNum(pageNum, source.size(), pageSize);
        List<T> records = pageList(source, safePageNum, pageSize);
        return of(records, source.size(), safePageNum, pageSize);
    }

    /**
     * 分页结果
     */
    public static final class PageResult<T> {
        private final List<T> records;
        private final long total;
        private final int pageNum;
        private final int pageSize;
        private final int totalPages;

        public PageResult(List<T> records, long total, int pageNum, int pageSize, int totalPages) {
            this.records = records;
            this.total = total;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.totalPages = totalPages;
        }

        public List<T> getRecords() {
            return records;
        }

        public long getTotal() {
            return total;
        }

        public int getPageNum() {
            return pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean isHasPrevious() {
            return PageUtil.hasPrevious(pageNum);
        }

        public boolean isHasNext() {
            return pageNum < totalPages;
        }
    }
}
