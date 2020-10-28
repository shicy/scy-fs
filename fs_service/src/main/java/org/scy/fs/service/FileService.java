package org.scy.fs.service;

import org.scy.common.ds.PageInfo;
import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务
 * Created by shicy on 2020/10/28
 */
public interface FileService {

    /**
     * 获取文件信息
     * @param key 第三方key
     * @param uuid 要获取的文件唯一编号
     * @return 文件信息
     */
    FileEntity getByUuid(String key, String uuid);

    /**
     * 获取文件信息
     * @param key 第三方key
     * @param uuids 要获取的文件唯一编号集
     * @return 文件信息
     */
    List<FileEntity> getByUuids(String key, String[] uuids);

    /**
     * 查询
     * @param key 第三方key
     * @param form 查询表单
     * @param pageInfo 分页
     * @return 文件及目录信息
     */
    List<FileEntity> find(String key, SearchForm form, PageInfo pageInfo);

    /**
     * 添加文件
     * @param key 第三方key
     * @param file 文件
     * @param fileName 文件名称
     * @param path 存储路径
     * @return 文件实例
     */
    FileEntity add(String key, MultipartFile file, String fileName, String path);

    /**
     * 删除文件
     * @param key 第三方key
     * @param uuid 要删除的文件唯一编号
     * @return 被删除的文件信息
     */
    FileEntity delete(String key, String uuid);

    /**
     * 删除文件
     * @param key 第三方key
     * @param uuids 要删除的文件唯一编号集
     * @return 被删除的文件信息
     */
    List<FileEntity> delete(String key, String[] uuids);

    /**
     * 删除目录
     * @param key 第三方key
     * @param path 要删除的目录，如：/a/b
     * @param includeSubDir 为true时同时删除子目录，否则如果存在子目录将无法删除
     * @param includeFile 为true时同时删除文件，否则如果存在文件将无法删除
     * @return 被删除的文件及目录信息
     */
    List<FileEntity> deleteDir(String key, String path, boolean includeSubDir, boolean includeFile);

    /**
     * 移动文件，将文件移动到另一个目录下
     * @param key 第三方key
     * @param uuid 要移动的文件唯一编号
     * @param toPath 移动到该目录下
     * @return 移动后的文件实例
     */
    FileEntity moveFile(String key, String uuid, String toPath);

    /**
     * 移动文件，将文件移动到另一个目录下
     * @param key 第三方key
     * @param uuids 要移动的文件唯一编号集
     * @param toPath 移动到该目录下
     * @return 被移动的文件信息
     */
    List<FileEntity> moveFiles(String key, String[] uuids, String toPath);

    /**
     * 移动目录，将目录移到另一个目录下
     * @param key 第三方key
     * @param fromPath 要移动的目录
     * @param toPath 移动到该目录下
     * @return 移动后的目录实例
     */
    FileEntity moveDir(String key, String fromPath, String toPath);

}
