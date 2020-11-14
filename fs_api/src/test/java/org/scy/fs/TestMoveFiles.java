package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.util.ArrayList;
import java.util.List;

public class TestMoveFiles {

    public static void main(String[] args) {
        TestInit.init();

        List<String> uuids = new ArrayList<String>();
        uuids.add("bh2xnoyn0gocurmp55b7mfe2rgarztwn");
        uuids.add("fb6mwwsuoyysjzkitbbjjuwbaulrtcuc");

        String[] _uuids = uuids.toArray(new String[0]);
        List<FileEntity> entities = FileSysAdapter.moveFiles(_uuids, "/temp/aa/图标");
        System.out.println("==> 移动文件数：" + entities.size());
        for (FileEntity entity: entities) {
            System.out.println("==> 移动文件：[" + entity.getUuid() + "]" + entity.getName());
        }
    }

}
