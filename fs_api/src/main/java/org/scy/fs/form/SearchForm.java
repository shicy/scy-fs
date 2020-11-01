package org.scy.fs.form;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询表单
 * Create by shicy on 2020/10/17
 */
public class SearchForm {

    // 按名称查询
    private String name;

    // 按名称模糊查询
    private String nameLike;

    // 查询该目录下的文件或子目录
    // 0-代表根目录 -1-代表所有目录
    private int parentId;

    // 查询该目录下的文件或子目录
    private String path;

    // 排序自动，可选：name, size, createTime
    private String orderBy;

    // 是否降序
    private boolean orderDesc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = StringUtils.trimToNull(name);
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = StringUtils.trimToNull(nameLike);
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = StringUtils.trimToNull(path);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        if ("name".equals(orderBy) || "size".equals(orderBy) || "createTime".equals(orderBy)) {
            this.orderBy = orderBy;
        }
        else {
            this.orderBy = null;
        }
    }

    public boolean isOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public Map<String, String> toMap() {
        return toMap(new HashMap<String, String>());
    }

    public Map<String, String> toMap(Map<String, String> map) {
        if (name != null)
            map.put("name", name);
        if (nameLike != null)
            map.put("nameLike", nameLike);
        map.put("parentId", "" + parentId);
        if (path != null)
            map.put("path", path);
        if (orderBy != null) {
            map.put("orderBy", orderBy);
            map.put("orderDesc", orderDesc ? "true" : "false");
        }
        return map;
    }

}
