package org.scy.fs.form;

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
    private int dirId;

    // 查询该目录下的文件或子目录
    private String path;

    // 排序自动，可选：name, size, createTime
    private String orderBy;

    // 是否降序
    private boolean orderDesc;

    // 页码
    private int page;

    // 查询记录数
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLike() {
        return nameLike;
    }

    public void setNameLike(String nameLike) {
        this.nameLike = nameLike;
    }

    public int getDirId() {
        return dirId;
    }

    public void setDirId(int dirId) {
        this.dirId = dirId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(boolean orderDesc) {
        this.orderDesc = orderDesc;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
