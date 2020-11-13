package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.ArrayList;
import java.util.List;

public class TestFiles {

    public static void main(String[] args) {
        TestInit.init();

        List<String> uuids = new ArrayList<String>();
        uuids.add("3lgofdlwzgjdoyqqjqo8setopufyszwv");
        uuids.add("tdnbz5sgd0khlm4bqpwcmcjkwhvthy9k");

        List<FileEntity> entities = FileSysAdapter.files(uuids.toArray(new String[0]));
        System.out.println("find results: " + entities.size());
        for (FileEntity entity: entities) {
            System.out.println("==>file: [" + entity.getUuid() + "]" + entity.getName());
        }
    }

}
