package org.scy.fs.model;

/**
 * 文件实例
 * Created by shicy on 2020/10/27
 */
public class FileEntityModel extends FileEntity {

    private static final long serialVersionUID = 1002020103017430002L;

    // 第三方访问key值
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
