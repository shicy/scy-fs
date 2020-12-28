package org.scy.fs;

import org.scy.fs.model.FileEntity;

public class TestUploadFromUrl {

    public static void main(String[] args) {
        TestInit.init();

        try {
            String url = "https://hanyu-word-gif.cdn.bcebos.com/b49ee6f4f427711e5a320c8e0eb15ce01.gif";
            FileEntity entity = FileSysAdapter.uploadFromUrl(url, null, "/temp/img");
            if (entity != null) {
                System.out.println("==>上传成功：" + entity.getUuid());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
