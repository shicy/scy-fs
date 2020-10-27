package org.scy.fs;

import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * 文件管理客户端适配器
 */
@Component
public class FileSysAdapter {

    // 第三方访问的key值
    @Value("${app.fs.key:#{null}}")
    private String access_key;

    /**
     * 查找文件实例
     * @param form 查询条件
     * @return 符合条件的文件实例信息
     */
    public static List<FileEntity> find(SearchForm form) {
        return null;
    }

    /**
     * 获取单个文件实例
     * @param uuid 文件唯一编号
     * @return 文件实例
     */
    public static FileEntity file(String uuid) {
        return null;
    }

    /**
     * 获取某个目录下的所有文件及子目录
     * @param dir 目录，如：/a/b
     * @return 该目录下的文件实例列表
     */
    public static List<FileEntity> dir(String dir) {
        return null;
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个文件文件
     * @param dir 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(File file, String dir) {
        return null;
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个本地文件
     * @param fileName 自定义文件名称
     * @param dir 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(File file, String fileName, String dir) {
        return null;
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个Http上传的文件
     * @param dir 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(MultipartFile file, String dir) {
        return null;
    }

    /**
     * 上传文件
     * @param file 想要上传的文件，一个Http上传的文件
     * @param fileName 自定义文件名称
     * @param dir 文件存储目录
     * @return 文件实例
     */
    public static FileEntity upload(MultipartFile file, String fileName, String dir) {
        return null;
    }

    /**
     * 下载文件
     * @param uuid 想要下载的文件唯一编号
     * @return 文件流
     */
    public static OutputStream download(String uuid) {
        return null;
    }

    /**
     * 下载多个文件，并打包成zip文件
     * @param uuids 想要下载的文件唯一编号集
     * @param zipFileName 打包的zip文件名称
     * @return 文件流
     */
    public static OutputStream download(String[] uuids, String zipFileName) {
        return null;
    }

    /**
     * 删除文件
     * @param uuid 想要删除的文件唯一编号
     * @return 删除的文件信息
     */
    public static FileEntity delete(String uuid) {
        return null;
    }

    /**
     * 删除多个文件
     * @param uuids 想要删除的文件唯一编号集
     * @return 返回删除的文件信息
     */
    public static List<FileEntity> delete(String[] uuids) {
        return null;
    }

    /**
     * 删除文件及目录
     * @param dir 想要删除的目录，如：/a/b
     * @param includeSubDir 是否包含子目录，默认当存在子目录时不会删除该目录，为true时将同时删除子目录
     * @param includeFile 是否包含目录下的文件，默认当存在文件时不会删除该目录（包含子目录），为true时将同时删除文件
     * @return 返回删除的文件及目录
     */
    public static List<FileEntity> delete(String dir, boolean includeSubDir, boolean includeFile) {
        return null;
    }

    /**
     * 将文件移动到另一个目录
     * @param uuid 想要移到的文件唯一编号
     * @param toDir 移动到该目录下
     * @return 移动后的文件
     */
    public static FileEntity move(String uuid, String toDir) {
        return null;
    }

    /**
     * 将文件或目录移到另一个目录下
     * @param fileEntity 想要移到的文件或目录
     * @param toDir 移动到该目录下
     * @return 移动后的文件或目录
     */
    public static FileEntity move(FileEntity fileEntity, String toDir) {
        return null;
    }

}
