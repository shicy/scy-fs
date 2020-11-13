package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.List;

public class TestDir {

    public static void main(String[] args) {
        TestInit.init();

        String dir = "/娱乐/游玩";
        List<FileEntity> entities = FileSysAdapter.dir(dir);
        System.out.println("find results:" + entities.size());
        for (FileEntity entity: entities) {
            System.out.println("==> file: [" + entity.getUuid() + "]" + entity.getName());
        }
    }

}
