package org.scy.fs.model;

import org.scy.common.web.model.BaseModel;

/**
 * 文件实体类
 * Created by shicy on 2020/10/27
 */
public class FileEntity extends BaseModel {

    private static final long serialVersionUID = 1002020103017430001L;

    // 唯一编号
    private String uuid;

    // 文件名称
    private String name;

    // 后缀名称
    private String ext;

    // 文件大小
    private long size;

    // 所属目录编号
    private int parentId;

    // 所有上级目录的编号集，格式如：0,2,34,124,0
    private String parentIds;

    // 是否是目录
    private short directory;

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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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

    public boolean isDir() {
        return directory == 1;
    }

    public short getDirectory() {
        return directory;
    }

    public void setDirectory(short directory) {
        this.directory = directory;
    }

}
