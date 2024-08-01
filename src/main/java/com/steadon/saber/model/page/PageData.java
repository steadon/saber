package com.steadon.saber.model.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageData<T> {
    private List<T> resultList;
    private Long total;
    private Long pageNo;
    private Long pageSize;
    private Long pages;

    /**
     * 替换list数据，自动转换为list对应数据类型
     *
     * @param <T> 原始类型
     * @param <R> 目标类型
     */
    public static <T, R> PageData<R> convert(@NonNull PageData<T> pageData, @NonNull List<R> list) {
        PageData<R> newPageData = new PageData<>();
        newPageData.total = pageData.total;
        newPageData.pageNo = pageData.pageNo;
        newPageData.pageSize = pageData.pageSize;
        newPageData.pages = pageData.pages;
        newPageData.resultList = list;
        return newPageData;
    }

    /**
     * 创建分页数据，部分接口需要联表查询作分页，无法获取IPage对象，所以需要手动创建
     *
     * @param total      总数
     * @param pageNo     当前页
     * @param pageSize   每页大小
     * @param pages      总页数
     * @param resultList 结果集
     * @param <T>        结果集类型
     * @return 分页数据
     */
    public static <T> PageData<T> create(Long total, Long pageNo, Long pageSize, Long pages, List<T> resultList) {
        PageData<T> newPageDate = new PageData<>();
        newPageDate.setTotal(total);
        newPageDate.setPageNo(pageNo);
        newPageDate.setPageSize(pageSize);
        newPageDate.setPages(pages);
        newPageDate.setResultList(resultList);
        return newPageDate;
    }

    public PageData<T> praise(IPage<T> iPage) {
        this.total = iPage.getTotal();
        this.pageNo = iPage.getCurrent();
        this.pageSize = iPage.getSize();
        this.pages = iPage.getPages();
        this.resultList = iPage.getRecords();
        return this;
    }
}