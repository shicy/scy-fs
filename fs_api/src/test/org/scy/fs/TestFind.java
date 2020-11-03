package org.scy.fs;

import org.scy.common.ds.PageInfo;
import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;

import java.util.List;

/**
 * 测试 find 方法
 * Created by shicy on 2020/11/1
 */
public class TestFind {

    public static void main(String[] args) {
        FileSysAdapter.access_key = "sEXJLy0ZjD52EqKDDtmkpaLqSG8M5cul";
        FileSysAdapter.server_url = "http://127.0.0.1:12104";

        SearchForm form = new SearchForm();
        form.setPath("/娱乐");
        PageInfo pageInfo = new PageInfo(1, 2, 0);

        List<FileEntity> entities = FileSysAdapter.find(form, pageInfo);
        System.out.println("find results: " + pageInfo.getTotal());
        if (entities != null) {
            for (FileEntity entity: entities) {
                System.out.println("==>[" + entity.getUuid() + "]" + entity.getName());
            }
        }
    }

}
