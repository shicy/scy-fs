package org.scy.fs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDownload2 {

    public static void main(String[] args) {
        TestInit.init();

        try {
            String zipFileName = "/mywork/temp/download/" + new Date().getTime() + ".zip";

            List<String> uuids = new ArrayList<String>();
            uuids.add("3lgofdlwzgjdoyqqjqo8setopufyszwv");
            uuids.add("tdnbz5sgd0khlm4bqpwcmcjkwhvthy9k");

            FileSysAdapter.download(uuids.toArray(new String[0]), zipFileName);
            System.out.println("==> 下载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
