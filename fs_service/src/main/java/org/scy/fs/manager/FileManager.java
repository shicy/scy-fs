package org.scy.fs.manager;

import org.apache.commons.lang3.StringUtils;
import org.scy.common.utils.FileUtilsEx;
import org.scy.common.utils.StringUtilsEx;
import org.scy.fs.model.FileEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件管理
 * Created by shicy 2020/10/28
 */
@Component
public class FileManager {

    private final static Logger logger = LoggerFactory.getLogger(FileManager.class);

    // 文件存储根目录
    private static String fileRoot;

    @Value("${app.fs.fileRoot:#{null}}")
    private String fileRootTemp;

    @PostConstruct
    public void init() {
        fileRoot = fileRootTemp;
    }

    /**
     * 存储文件
     * @param entity 文件信息
     * @param file Http上传的文件
     */
    public static void save(FileEntity entity, MultipartFile file) {
        String fileName = getFileFullName(entity);
        FileUtilsEx.makeDirectory(fileName);
        try {
            FileUtilsEx.write(fileName, file);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param entity 文件信息
     */
    public static void remove(FileEntity entity) {
        String fileName = getFileFullName(entity);
        FileUtilsEx.deleteFile(fileName);
    }

    /**
     * 获取本地文件
     * @param entity 文件实例
     */
    public static File getFile(FileEntity entity) {
        String fileName = getFileFullName(entity);
        File file = new File(fileName);
        return file.exists() ? file : null;
    }

    /**
     * 获取本地文件
     * @param entities 文件实例
     */
    public static File[] getFiles(FileEntity[] entities) {
        List<File> files = new ArrayList<File>();
        if (entities != null && entities.length > 0) {
            for (FileEntity entity: entities) {
                File file = getFile(entity);
                if (file != null)
                    files.add(file);
            }
        }
        return files.toArray(new File[0]);
    }

    /**
     * 生成文件下载流
     * @param entity 要下载的文件信息
     */
    public static void stream(FileEntity entity, OutputStream output) throws IOException {
        File file = getFile(entity);
        if (file != null) {
            FileUtilsEx.write(output, file);
        }
    }

    /**
     * 生成批量文件下载流，生成zip文件
     * @param entities 要下载的文件信息
     */
    public static void stream(FileEntity[] entities, OutputStream output) throws IOException {
        File[] files = getFiles(entities);
        if (files.length > 0) {
            List<String> fileNames = new ArrayList<String>();
            for (FileEntity entity: entities) {
                fileNames.add(entity.getName());
            }
            FileUtilsEx.zipFiles(files, output, fileNames.toArray(new String[0]));
        }
    }

    /**
     * 获取文件大小
     */
    public static long getSize(FileEntity[] entities) {
        int size = 0;
        if (entities != null && entities.length > 0) {
            for (FileEntity entity: entities) {
                size += entity.getSize();
            }
        }
        return size;
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

    /**
     * 获取文件全路径名称
     */
    public static String getFileFullName(FileEntity model) {
        String fileName = getFilePath(model.getUuid());
        fileName += "/" + model.getUuid();
        // fileName += "." + model.getExt();
        fileName += ".f";
        return fileName;
    }

    /**
     * 获取文件路径
     * @param uuid 文件唯一编号
     * @return 返回文件路径，如：/.../a/b/c
     */
    public static String getFilePath(String uuid) {
        String path = "";
        path += "/" + uuid.charAt(0);
        path += "/" + uuid.charAt(1);
        path += "/" + uuid.charAt(2);
        path = fileRoot + path.toLowerCase();
        return StringUtilsEx.tranToFileSeparator(path);
    }

}
