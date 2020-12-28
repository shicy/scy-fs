package org.scy.fs;

import java.util.Date;

public class TestDownload {

    public static void main(String[] args) {
        TestInit.init();

        try {
            String downloadFileName = "/mywork/temp/download/" + new Date().getTime() + ".png";
            FileSysAdapter.download("wksjwhdzsjstn0fr2zvpbpi8tg8hixub", downloadFileName);
            System.out.println("==> 下载完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
