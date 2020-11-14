package org.scy.fs;

public class TestFilePath {

    public static void main(String[] args) {
        TestInit.init();

        String uuid = "bh2xnoyn0gocurmp55b7mfe2rgarztwn";
        String path = FileSysAdapter.filePath(uuid);
        System.out.println("==> 文件：" + path);
    }

}
