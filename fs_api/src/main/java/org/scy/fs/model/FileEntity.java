package org.scy.fs.model;

import org.scy.common.web.model.BaseModel;

/**
 * 文件实体类
 * Created by shicy on 2020/10/27
 */
public class FileEntity extends BaseModel {

    // 唯一编号
    private String uuid;

    // 文件名称
    private String name;

    // 文件大小
    private long size;

    // 所属目录编号
    private int parentId;

    // 所有上级目录的编号集，格式如：0,2,34,124,0
    private String parentIds;

    // 是否是目录
    private boolean directory;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

}
