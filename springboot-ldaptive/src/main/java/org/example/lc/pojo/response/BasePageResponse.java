package org.example.lc.pojo.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasePageResponse<Condition, T> implements IPage<T> {
    private static final long serialVersionUID = 6992537305369600956L;
    @ApiModelProperty("查询条件")
    protected Condition condition;
    @ApiModelProperty("查询数据列表")
    protected List<T> records;
    @ApiModelProperty(
            value = "总记录数",
            example = "10",
            required = true
    )
    protected long total;
    @ApiModelProperty(
            value = "每页显示条数，默认 10",
            example = "10",
            required = true
    )
    protected long size;
    @ApiModelProperty(
            value = "当前页,第一页用1表示",
            example = "1",
            required = true
    )
    protected long current;
    @JsonIgnore
    @ApiModelProperty(
            value = "排序字段信息",
            required = false,
            hidden = true
    )
    protected List<OrderItem> orders;
    @JsonIgnore
    @ApiModelProperty(
            value = "是否自动优化 COUNT SQL",
            hidden = true
    )
    protected boolean optimizeCountSql;
    @JsonIgnore
    @ApiModelProperty(
            value = "是否进行 count 查询",
            hidden = true
    )
    protected boolean isSearchCount;
    @JsonIgnore
    @ApiModelProperty(
            value = "是否命中count缓存",
            hidden = true
    )
    protected boolean hitCount;
    @JsonIgnore
    @ApiModelProperty(
            value = "COUNT SQL的方法名",
            hidden = true
    )
    protected String countId;
    @JsonIgnore
    @ApiModelProperty(
            value = "最大每页分页数限制",
            hidden = true
    )
    protected Long maxLimit;

    public BasePageResponse() {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
    }

    public BasePageResponse(long current, long size) {
        this(current, size, 0L);
    }

    public BasePageResponse(long current, long size, long total) {
        this(current, size, total, true);
    }

    public BasePageResponse(long current, long size, boolean isSearchCount) {
        this(current, size, 0L, isSearchCount);
    }

    public BasePageResponse(long current, long size, long total, boolean isSearchCount) {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList();
        this.optimizeCountSql = true;
        this.isSearchCount = true;
        this.hitCount = false;
        if (current > 1L) {
            this.current = current;
        }

        this.size = size;
        this.total = total;
        this.isSearchCount = isSearchCount;
    }

    public List<OrderItem> orders() {
        return this.orders;
    }

    public BasePageResponse<Condition, T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    public BasePageResponse<Condition, T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public BasePageResponse<Condition, T> setSize(long size) {
        this.size = size;
        return this;
    }

    public BasePageResponse<Condition, T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    public boolean hasPrevious() {
        return this.current > 1L;
    }

    public boolean hasNext() {
        return this.current < this.getPages();
    }

    public Condition getCondition() {
        return this.condition;
    }

    public List<T> getRecords() {
        return this.records;
    }

    public long getTotal() {
        return this.total;
    }

    public long getSize() {
        return this.size;
    }

    public long getCurrent() {
        return this.current;
    }

    public List<OrderItem> getOrders() {
        return this.orders;
    }

    public boolean isOptimizeCountSql() {
        return this.optimizeCountSql;
    }

    public boolean isSearchCount() {
        return this.isSearchCount;
    }

    public boolean isHitCount() {
        return this.hitCount;
    }

    public String getCountId() {
        return this.countId;
    }

    public Long getMaxLimit() {
        return this.maxLimit;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @JsonIgnore
    public void setOrders(List<OrderItem> orders) {
        this.orders = orders;
    }

    @JsonIgnore
    public void setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
    }

    @JsonIgnore
    public void setSearchCount(boolean isSearchCount) {
        this.isSearchCount = isSearchCount;
    }

    @JsonIgnore
    public void setHitCount(boolean hitCount) {
        this.hitCount = hitCount;
    }

    @JsonIgnore
    public void setCountId(String countId) {
        this.countId = countId;
    }

    @JsonIgnore
    public void setMaxLimit(Long maxLimit) {
        this.maxLimit = maxLimit;
    }
}
