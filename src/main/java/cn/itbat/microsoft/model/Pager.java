package cn.itbat.microsoft.model;

import lombok.Data;

/**
 * 分页参数
 *
 * @author mjj
 * @date 2020年05月12日 16:30:29
 */
@Data
public class Pager {


    /**
     * 当前页码
     */
    private Integer pageIndex;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 排序  公共枚举 SqlOrderByEnum
     */
    private Integer orderByClause;


    public Pager() {

    }

    public Pager(Integer orderByClause) {
        this.orderByClause = orderByClause;
    }

    public Pager(Integer pageIndex, Integer pageSize, Integer orderByClause) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.orderByClause = orderByClause;
    }

    public Pager(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}