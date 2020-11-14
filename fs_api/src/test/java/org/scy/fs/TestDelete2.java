package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDelete2 {

    public static void main(String[] args) {
        TestInit.init();

        List<String> uuids = new ArrayList<String>();
        uuids.add("wz4uep7cofi5igeuhh24owi7awl3d3f7");
        uuids.add("gkrdlv5j8i91ssdh7xfkkzb7sejdofvb");

        List<FileEntity> entities = FileSysAdapter.delete(uuids.toArray(new String[0]));
        System.out.println("==> 删除记录数：" + entities.size());
        for (FileEntity entity: entities) {
            System.out.println("==> 删除：[" + entity.getUuid() + "]" + entity.getName());
        }
    }

}
