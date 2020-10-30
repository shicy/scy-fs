package org.scy.fs.model;

import org.scy.common.web.model.BaseModel;

/**
 * 第三方注册信息
 * Created by shicy on 2020/10/28
 */
public class RegisterModel extends BaseModel {

    private static final long serialVersionUID = 1002020103017440001L;

    // key值
    private String key;

    // 名称
    private String name;

    // 当前文件总大小
    private long size;

    // 限制总文件大小
    private long limit;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }
}
