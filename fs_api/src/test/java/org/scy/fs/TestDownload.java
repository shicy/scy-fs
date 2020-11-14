package org.scy.fs;

import java.util.Date;

public class TestDownload {

    public static void main(String[] args) {
        TestInit.init();

        try {
            String downloadFileName = "/mywork/temp/download/" + new Date().getTime() + ".png";
            FileSysAdapter.download("tdnbz5sgd0khlm4bqpwcmcjkwhvthy9k", downloadFileName);
            System.out.println("==> 下载完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
