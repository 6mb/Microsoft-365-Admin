package cn.itbat.microsoft.utils;


import cn.itbat.microsoft.model.Pager;

import java.util.Collections;
import java.util.List;

/**
 * 自定义内存分页
 *
 * @author mjj
 * @date 2020年08月06日 19:23:16
 */
public class PageInfo<T> {

    private long total;

    private Integer pageNum;

    private Integer pageSize;

    private List<T> list;

    public PageInfo() {

    }

    public PageInfo(List<T> list, Pager pager) {
        if (list.isEmpty()) {
            this.total = 0L;
            this.pageNum = 1;
            this.pageSize = 10;
            this.list = Collections.emptyList();
        } else {
            this.total = list.size();
            this.list = list;
            this.pageNum = (pager.getPageSize() == null || 0 == pager.getPageIndex()) ? 1 : pager.getPageIndex();
            this.pageSize = (pager.getPageIndex() == null || 0 == pager.getPageSize()) ? 10 : pager.getPageSize();
            this.paging();
        }
    }

    private void paging() {
        Integer totalNum = list.size();
        Integer totalPage = 0;
        if (totalNum > 0) {
            totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        }
        if (pageNum > totalPage) {
            pageNum = totalPage;
        }
        int startPoint = (pageNum - 1) * pageSize;
        int endPoint = startPoint + pageSize;
        if (totalNum <= endPoint) {
            endPoint = totalNum;
        }
        //分页处理
        this.list = list.subList(startPoint, endPoint);
    }

    public List<T> getList() {
        return this.list;
    }

    public long getTotal() {
        return this.total;
    }
}
