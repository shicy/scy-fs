package org.scy.fs;

import org.scy.fs.model.FileEntity;

public class TestDelete {

    public static void main(String[] args) {
        TestInit.init();

        String uuid = "womf5vpi6sq5yclv5shqdhjza3ofwdee";
        FileEntity entity = FileSysAdapter.delete(uuid);
        if (entity != null)
            System.out.println("==>删除成功：" + entity.getName());
    }

}
