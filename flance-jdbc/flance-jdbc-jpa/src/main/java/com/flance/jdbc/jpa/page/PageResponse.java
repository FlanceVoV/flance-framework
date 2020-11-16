package com.flance.jdbc.jpa.page;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * 分页响应封装
 * @author jhf
 * @param <T>   数据类型
 */
public class PageResponse<T> {

    private Long total;

    private String scroll;

    private List<T> data;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getScroll() {
        return scroll;
    }

    public void setScroll(String scroll) {
        this.scroll = scroll;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @ConstructorProperties({"total", "scroll", "data"})
    public PageResponse(Long total, String scroll, List<T> data) {
        this.total = total;
        this.scroll = scroll;
        this.data = data;
    }

    public static <T> PageResponseBuilder builder() {
        return new PageResponseBuilder<>();
    }

    public static class PageResponseBuilder<T> {
        private Long total;
        private String scroll;
        private List<T> data;

        PageResponseBuilder() {
        }

        public PageResponse.PageResponseBuilder total(Long total) {
            this.total = total;
            return this;
        }

        public PageResponse.PageResponseBuilder scroll(String scroll) {
            this.scroll = scroll;
            return this;
        }

        public PageResponse.PageResponseBuilder data(List<T> data) {
            this.data = data;
            return this;
        }

        public PageResponse<T> build() {
            return new PageResponse<>(this.total, this.scroll, this.data);
        }

        @Override
        public String toString() {
            return "PageResponse.PageResponseBuilder(total=" + this.total + ", scroll=" + this.scroll + ", data=" + this.data + ")";
        }
    }

    @Override
    public String toString() {
        return "PageResponse(total=" + this.getTotal() + ", scroll=" + this.getScroll() + ", data=" + this.getData() + ")";
    }
}
