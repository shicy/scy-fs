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
    private int dirId;

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

    public int getDirId() {
        return dirId;
    }

    public void setDirId(int dirId) {
        this.dirId = dirId;
    }

    public boolean isDirectory() {
        return directory;
    }

    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

}
