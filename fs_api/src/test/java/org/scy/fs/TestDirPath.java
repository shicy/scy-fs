package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.List;

public class TestDirPath {

    public static void main(String[] args) {
        TestInit.init();

        String dir = "/temp/aa/图标";
        List<FileEntity> entities = FileSysAdapter.dirPaths(dir);
        System.out.println("==> 目录：" + dir);
        for (FileEntity entity: entities) {
            System.out.println("==> 目录：[" + entity.getId() + "]" + entity.getName());
        }
    }

}
