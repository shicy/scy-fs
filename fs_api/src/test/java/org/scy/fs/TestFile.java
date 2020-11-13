package org.scy.fs;

import org.scy.fs.model.FileEntity;

public class TestFile {

    public static void main(String[] args) {
        TestInit.init();

        String uuid = "tdnbz5sgd0khlm4bqpwcmcjkwhvthy9k";
        FileEntity entity = FileSysAdapter.file(uuid);
        if (entity != null)
            System.out.println("==>file:" + entity.getName());
    }

}
