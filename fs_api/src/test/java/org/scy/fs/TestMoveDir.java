package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.List;

public class TestMoveDir {

    public static void main(String[] args) {
        TestInit.init();

        String fromPath = "/icons";
        String toPath = "/temp/icons";
        FileEntity entity = FileSysAdapter.moveDir(fromPath, toPath);
        if (entity != null)
            System.out.println("==> 移动文件：[" + entity.getUuid() + "]" + entity.getName());
    }

}
