package org.scy.fs;

import org.scy.common.utils.StringUtilsEx;

public class MainTest {

    public static void main(String[] args) {
        String ids = "0,1,4,6,232,5432,0";
        System.out.println(ids.substring(0, ids.length() - 1));

        System.out.println(StringUtilsEx.getRandomString(32));
    }

}
