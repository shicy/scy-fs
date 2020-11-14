package org.scy.fs;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.scy.common.ds.PageInfo;
import org.scy.common.utils.FileUtilsEx;
import org.scy.common.utils.HttpClientUtils;
import org.scy.common.web.controller.HttpResponse;
import org.scy.common.web.controller.HttpResult;
import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件管理客户端适配器
 */
@Component
public class FileSysAdapter {

    // 第三方访问的key值
    public static String access_key;

    // 文件服务地址
    public static String server_url;

    @Value("${app.fs.key:#{null}}")
    private String accessKeyTemp;

    @Value("${app.fs-service.url:#{null}}")
    private String serverUrlTemp;

    @PostConstruct
    public void init() {
        access_key = accessKeyTemp;
        server_url = serverUrlTemp;
    }

    /**
     * 查找文件实例
     * @param form 查询条件
     * @param pageInfo 分页
     * @return 符合条件的文件实例信息
     */
    public static List<FileEntity> find(SearchForm form, PageInfo pageInfo) {
        String url = getUrl("/file/list");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        if (form != null) {
            params = form.toMap(params);
        }
        if (pageInfo != null) {
            params.put("page", "" + pageInfo.getPage());
        }

        HttpResponse response = HttpClientUtils.doGet(url, params);
        return getList(response, pageInfo);
    }

    /**
     * 获取单个文件实例
     * @param uuid 文件唯一编号
     * @return 文件实例
     */
    public static FileEntity file(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            String url = getUrl("/file/info/" + uuid);

            Map<String, String> params = new HashMap<String, String>();
            params.put("key", access_key);

            HttpResponse response = HttpClientUtils.doGet(url, params);
            return getOne(response);
        }
        return null;
    }

    /**
     * 批量获取多个文件
     * @param uuids 文件唯一编号集
     * @return 文件实例
     */
    public static List<FileEntity> files(String[] uuids) {
        if (uuids == null || uuids.length == 0)
            return new ArrayList<FileEntity>();

        String url = getUrl("/file/info");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuids", StringUtils.join(uuids, ","));

        HttpResponse response = HttpClientUtils.doGet(url, params);
        return getList(response);
    }

    /**
     * 获取某个目录下的所有文件及子目录
     * @param path 目录，如：/a/b
     * @return 该目录下的文件实例列表
     */
    public static List<FileEntity> dir(String path) {
        String url = getUrl("/file/list");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("size", "1000"); // 限制文件数
        if (StringUtils.isBlank(path)) {
            params.put("parentId", "0");
        }
        else {
            params.put("parentId", "-1");
            params.put("path", path);
        }

        HttpResponse response = HttpClientUtils.doGet(url, params);
        return getList(response);
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个文件文件
     * @param path 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(File file, String path) {
        return upload(file, file.getName(), path);
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个本地文件
     * @param fileName 自定义文件名称
     * @param path 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(File file, String fileName, String path) {
        String url = getUrl("/file/upload");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("fileName", fileName);
        params.put("path", path);

        HttpResponse response = HttpClientUtils.doUpload(url, file, params);
        return getOne(response);
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个Http上传的文件
     * @param path 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(MultipartFile file, String path) {
        return upload(file, file.getOriginalFilename(), path);
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个Http上传的文件
     * @param fileName 自定义文件名称
     * @param path 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(MultipartFile file, String fileName, String path) {
        String url = getUrl("/file/upload");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("fileName", fileName);
        params.put("path", path);

        HttpResponse response = HttpClientUtils.doUpload(url, file, params);
        return getOne(response);
    }

    /**
     * 下载文件
     * @param uuid 想要下载的文件唯一编号
     * @param output 输出流
     */
    public static void download(String uuid, OutputStream output) {
        if (StringUtils.isBlank(uuid))
            return;

        String url = getUrl("/file/download");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuid", uuid);

        HttpResponse response = HttpClientUtils.doDownload(url, params, output);
        if (response.hasError())
            throw new RuntimeException(response.getErrorMessage());
    }

    /**
     * 下载文件
     * @param uuid 想要下载的文件唯一编号
     * @param file 本地存储文件
     * @throws IOException 异常
     */
    public static void download(String uuid, File file) throws IOException {
        if (StringUtils.isBlank(uuid))
            return;

        FileUtilsEx.makeDirectory(file.getAbsolutePath());

        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            download(uuid, output);
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * 下载文件
     * @param uuid 想要下载的文件唯一编号
     * @param destFile 本地存储文件路径
     * @throws IOException 异常
     */
    public static void download(String uuid, String destFile) throws IOException {
        download(uuid, new File(destFile));
    }

    /**
     * 下载多个文件，并打包成zip文件
     * @param uuids 想要下载的文件唯一编号集
     * @param output 输出流
     * @param zipFileName 打包的zip文件名称
     */
    public static void download(String[] uuids, OutputStream output, String zipFileName) {
        if (uuids == null || uuids.length == 0)
            return;

        String url = getUrl("/file/download");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuids", StringUtils.join(uuids, ","));
        params.put("fileName", zipFileName);

        HttpResponse response = HttpClientUtils.doDownload(url, params, output);
        if (response.hasError())
            throw new RuntimeException(response.getErrorMessage());
    }

    /**
     * 下载多个文件，并打包成zip文件
     * @param uuids 想要下载的文件唯一编号
     * @param zipFile 本地存储文件
     * @throws IOException 异常
     */
    public static void download(String[] uuids, File zipFile) throws IOException {
        if (uuids == null || uuids.length == 0)
            return;

        FileUtilsEx.makeDirectory(zipFile.getAbsolutePath());

        OutputStream output = null;
        try {
            output = new FileOutputStream(zipFile);
            download(uuids, output, zipFile.getName());
        } finally {
            IOUtils.closeQuietly(output);
        }
    }

    /**
     * 下载多个文件，并打包成zip文件
     * @param uuids 想要下载的文件唯一编号
     * @param zipFileName 本地存储文件
     * @throws IOException 异常
     */
    public static void download(String[] uuids, String zipFileName) throws IOException {
        download(uuids, new File(zipFileName));
    }

    /**
     * 删除文件
     * @param uuid 想要删除的文件唯一编号
     * @return 删除的文件信息
     */
    public static FileEntity delete(String uuid) {
        if (StringUtils.isBlank(uuid))
            return null;

        String url = getUrl("/file/delete");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuid", uuid);

        HttpResponse response = HttpClientUtils.doPost(url, params);
        List<FileEntity> entities = getList(response);
        if (entities != null && entities.size() > 0)
            return entities.get(0);
        return null;
    }

    /**
     * 删除多个文件
     * @param uuids 想要删除的文件唯一编号集
     * @return 返回删除的文件信息
     */
    public static List<FileEntity> delete(String[] uuids) {
        if (uuids == null || uuids.length == 0)
            return new ArrayList<FileEntity>();

        String url = getUrl("/file/delete");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuids", StringUtils.join(uuids, ","));

        HttpResponse response = HttpClientUtils.doPost(url, params);
        return getList(response);
    }

    /**
     * 删除文件及目录
     * @param path 想要删除的目录，如：/a/b
     * @param includeSubDir 是否包含子目录，默认当存在子目录时不会删除该目录，为true时将同时删除子目录
     * @param includeFile 是否包含目录下的文件，默认当存在文件时不会删除该目录（包含子目录），为true时将同时删除文件
     * @return 返回删除的文件及目录
     */
    public static List<FileEntity> delete(String path, boolean includeSubDir, boolean includeFile) {
        String url = getUrl("/file/delete");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("path", path);
        params.put("includeSubDir", includeSubDir ? "true" : "false");
        params.put("includeFile", includeFile ? "true" : "false");

        HttpResponse response = HttpClientUtils.doPost(url, params);
        return getList(response);
    }

    /**
     * 将文件移动到另一个目录
     * @param uuid 想要移到的文件唯一编号
     * @param toPath 移动到该目录下
     * @return 移动后的文件
     */
    public static FileEntity moveFile(String uuid, String toPath) {
        if (StringUtils.isBlank(uuid))
            return null;

        String url = getUrl("/file/move");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuid", uuid);
        params.put("toPath", toPath);

        HttpResponse response = HttpClientUtils.doPost(url, params);
        List<FileEntity> entities = getList(response);
        if (entities != null && entities.size() > 0)
            return entities.get(0);
        return null;
    }

    /**
     * 批量移动文件，将多个文件移动到另一个目录
     * @param uuids 想要移动的文件唯一编号集，逗号分隔
     * @param toPath 移到到该目录下
     * @return 移动后的文件
     */
    public static List<FileEntity> moveFiles(String[] uuids, String toPath) {
        if (uuids == null || uuids.length == 0)
            return new ArrayList<FileEntity>();

        String url = getUrl("/file/move");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuids", StringUtils.join(uuids, ","));
        params.put("toPath", toPath);

        HttpResponse response = HttpClientUtils.doPost(url, params);
        return getList(response);
    }

    /**
     * 将文件或目录移到另一个目录下
     * @param fromPath 想要移到的文件或目录
     * @param toPath 移动到该目录下
     * @return 移动后的文件或目录
     */
    public static FileEntity moveDir(String fromPath, String toPath) {
        String url = getUrl("/file/move");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("path", fromPath);
        params.put("toPath", toPath);

        HttpResponse response = HttpClientUtils.doPost(url, params);
        List<FileEntity> entities = getList(response);
        if (entities != null && entities.size() > 0)
            return entities.get(0);
        return null;
    }

    /**
     * 获取文件路径
     * @param uuid 文件唯一编号
     * @return 文件路径
     */
    public static String filePath(String uuid) {
        List<FileEntity> entities = filePaths(uuid);
        StringBuilder pathname = new StringBuilder();
        for (FileEntity entity: entities) {
            pathname.append("/").append(entity.getName());
        }
        return pathname.toString();
    }

    /**
     * 获取文件路径
     * @param uuid 文件唯一编号
     * @return 文件路径信息
     */
    public static List<FileEntity> filePaths(String uuid) {
        if (StringUtils.isBlank(uuid))
            return new ArrayList<FileEntity>();

        String url = getUrl("/file/path");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("uuid", uuid);

        HttpResponse response = HttpClientUtils.doGet(url, params);
        return getList(response);
    }

    /**
     * 获取目录路径
     * @param path 目录，如：/a/b
     * @return 目录路径信息
     */
    public static List<FileEntity> dirPaths(String path) {
        if (StringUtils.isBlank(path))
            return new ArrayList<FileEntity>();

        String url = getUrl("/file/path");

        Map<String, String> params = new HashMap<String, String>();
        params.put("key", access_key);
        params.put("path", path);

        HttpResponse response = HttpClientUtils.doGet(url, params);
        return getList(response);
    }

    private static String getUrl(String path) {
        if (server_url == null)
            throw new RuntimeException("未知文件服务");
        return server_url + path;
    }

    private static FileEntity getOne(HttpResponse response) {
        if (response.hasError())
            throw new RuntimeException(response.getErrorMessage());
        HttpResult result = response.getResult();
        return result.getData(FileEntity.class);
    }

    private static List<FileEntity> getList(HttpResponse response) {
        return getList(response, null);
    }

    private static List<FileEntity> getList(HttpResponse response, PageInfo pageInfo) {
        if (response.hasError())
            throw new RuntimeException(response.getErrorMessage());
        HttpResult result = response.getResult();
        if (pageInfo != null && result.getPageInfo() != null) {
            pageInfo.setTotal(result.getPageInfo().getTotal());
        }
        return result.getDataList(FileEntity.class);
    }

}
