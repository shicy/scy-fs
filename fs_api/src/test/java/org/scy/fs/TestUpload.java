package org.scy.fs;

import org.scy.fs.model.FileEntity;

import java.io.File;

public class TestUpload {

    public static void main(String[] args) {
        TestInit.init();

        String uploadFile = "/mywork/temp/code.png";
        FileEntity entity = FileSysAdapter.upload(new File(uploadFile), "/test/icons");
        if (entity != null)
            System.out.println("==> 上传成功：" + entity.getUuid());
    }

}
