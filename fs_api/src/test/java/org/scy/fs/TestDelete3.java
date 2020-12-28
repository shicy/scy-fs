package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.List;

public class TestDelete3 {

    public static void main(String[] args) {
        TestInit.init();

        String path = "/temp/img";
        List<FileEntity> entities = FileSysAdapter.delete(path, true, true);
        System.out.println("==> 删除记录数：" + entities.size());
        for (FileEntity entity: entities) {
            System.out.println("==> 删除：[" + entity.getUuid() + "]" + entity.getName());
        }
    }

}
