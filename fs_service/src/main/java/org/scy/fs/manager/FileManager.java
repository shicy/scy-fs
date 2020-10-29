package org.scy.fs.manager;

import org.apache.commons.lang3.StringUtils;
import org.scy.fs.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理
 * Created by shicy 2020/10/28
 */
public class FileManager {

    /**
     * 存储文件
     * @param entity 文件信息
     * @param file Http上传的文件
     */
    public static void save(FileEntity entity, MultipartFile file) {

    }

    /**
     * 删除文件
     * @param entity 文件信息
     */
    public static void remove(FileEntity entity) {

    }

    /**
     * 生成文件下载流
     * @param entity 要下载的文件信息
     * @return 返回文件输入流，注意关闭流
     */
    public static InputStream stream(FileEntity entity) {
        return null;
    }

    /**
     * 生成批量文件下载流，生成zip文件
     * @param entities 要下载的文件信息
     * @return 返回文件输入流，注意关闭流
     */
    public static InputStream stream(FileEntity[] entities) {
        return null;
    }

    /**
     * 解析路径，获取目录名称
     * @param path 路径，如：/a/b
     */
    public static String[] getPaths(String path) {
        List<String> results = new ArrayList<String>();
        if (StringUtils.isNotBlank(path)) {
            String[] paths = StringUtils.split(path, "/");
            for (String _path: paths) {
                if (StringUtils.isNotBlank(_path))
                    results.add(_path.trim());
            }
        }
        return results.toArray(new String[0]);
    }

}
