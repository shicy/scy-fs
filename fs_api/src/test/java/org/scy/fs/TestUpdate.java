package org.scy.fs;

import org.scy.fs.model.FileEntity;

public class TestUpdate {

    public static void main(String[] args) {
        TestInit.init();

        String uuid = "u6xbuuu57jrqouu4xiigxjzyomdoa8ao";
        FileEntity entity = FileSysAdapter.updateName(uuid, "new_name.png");
        if (entity != null)
            System.out.println("==> 文件：[" + entity.getUuid() + "]" + entity.getName());
    }

}
