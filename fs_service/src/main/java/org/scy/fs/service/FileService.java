package org.scy.fs.service;

import org.scy.common.ds.PageInfo;
import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;
import org.scy.fs.model.FileEntityModel;
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
    FileEntityModel getByUuid(String key, String uuid);

    /**
     * 获取文件信息
     * @param key 第三方key
     * @param uuids 要获取的文件唯一编号集
     * @return 文件信息
     */
    List<FileEntityModel> getByUuids(String key, String[] uuids);

    /**
     * 查询
     * @param key 第三方key
     * @param form 查询表单
     * @param pageInfo 分页
     * @return 文件及目录信息
     */
    List<FileEntityModel> find(String key, SearchForm form, PageInfo pageInfo);

    /**
     * 添加文件
     * @param key 第三方key
     * @param file 文件
     * @param fileName 文件名称
     * @param path 存储路径
     * @return 文件实例
     */
    FileEntityModel add(String key, MultipartFile file, String fileName, String path);

    /**
     * 修改文件信息
     * @param key 第三方key
     * @param entity 最新的文件信息
     * @return 返回文件信息
     */
    FileEntityModel update(String key, FileEntity entity);

    /**
     * 删除文件
     * @param key 第三方key
     * @param uuid 要删除的文件唯一编号
     * @return 被删除的文件信息
     */
    FileEntityModel delete(String key, String uuid);

    /**
     * 删除文件
     * @param key 第三方key
     * @param uuids 要删除的文件唯一编号集
     * @return 被删除的文件信息
     */
    List<FileEntityModel> delete(String key, String[] uuids);

    /**
     * 删除目录
     * @param key 第三方key
     * @param path 要删除的目录，如：/a/b
     * @param includeSubDir 为true时同时删除子目录，否则如果存在子目录将无法删除
     * @param includeFile 为true时同时删除文件，否则如果存在文件将无法删除
     * @return 被删除的文件及目录信息
     */
    List<FileEntityModel> deleteDir(String key, String path, boolean includeSubDir, boolean includeFile);

    /**
     * 移动文件，将文件移动到另一个目录下
     * @param key 第三方key
     * @param uuid 要移动的文件唯一编号
     * @param toPath 移动到该目录下
     * @return 移动后的文件实例
     */
    FileEntityModel moveFile(String key, String uuid, String toPath);

    /**
     * 移动文件，将文件移动到另一个目录下
     * @param key 第三方key
     * @param uuids 要移动的文件唯一编号集
     * @param toPath 移动到该目录下
     * @return 被移动的文件信息
     */
    List<FileEntityModel> moveFiles(String key, String[] uuids, String toPath);

    /**
     * 移动目录，将目录移到另一个目录下
     * @param key 第三方key
     * @param fromPath 要移动的目录
     * @param toPath 移动到该目录下
     * @return 移动后的目录实例
     */
    FileEntityModel moveDir(String key, String fromPath, String toPath);

    /**
     * 获取文件路径
     * @param key 第三方key
     * @param uuid 文件唯一编号
     * @return 完整路径信息
     */
    List<FileEntityModel> getFilePath(String key, String uuid);

    /**
     * 获取目录文件路径
     * @param key 第三方key
     * @param path 路径，如：/a/b
     * @return 完整路径信息
     */
    List<FileEntityModel> getDirPath(String key, String path);

}
