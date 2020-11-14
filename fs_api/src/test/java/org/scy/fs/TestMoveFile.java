package org.scy.fs;

import org.scy.fs.model.FileEntity;

public class TestMoveFile {

    public static void main(String[] args) {
        TestInit.init();

        String uuid = "bh2xnoyn0gocurmp55b7mfe2rgarztwn";
        FileEntity entity = FileSysAdapter.moveFile(uuid, "/icons");
        if (entity != null)
            System.out.println("==> 移动文件：[" + entity.getUuid() + "]" + entity.getName());
    }

}
