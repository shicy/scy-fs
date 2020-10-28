package org.scy.fs.service.impl;

import org.scy.common.ds.PageInfo;
import org.scy.common.web.service.MybatisBaseService;
import org.scy.fs.form.SearchForm;
import org.scy.fs.model.FileEntity;
import org.scy.fs.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件服务
 * Created by shicy on 2020/10/28
 */
@Service
public class FileServiceImpl extends MybatisBaseService implements FileService {

    @Override
    public FileEntity getByUuid(String key, String uuid) {
        return null;
    }

    @Override
    public List<FileEntity> getByUuids(String key, String[] uuids) {
        return null;
    }

    @Override
    public List<FileEntity> find(String key, SearchForm form, PageInfo pageInfo) {
        return null;
    }

    @Override
    public FileEntity add(String key, MultipartFile file, String fileName, String path) {
        return null;
    }

    @Override
    public FileEntity delete(String key, String uuid) {
        return null;
    }

    @Override
    public List<FileEntity> delete(String key, String[] uuids) {
        return null;
    }

    @Override
    public List<FileEntity> deleteDir(String key, String path, boolean includeSubDir, boolean includeFile) {
        return null;
    }

    @Override
    public FileEntity moveFile(String key, String uuid, String toPath) {
        return null;
    }

    @Override
    public List<FileEntity> moveFiles(String key, String[] uuids, String toPath) {
        return null;
    }

    @Override
    public FileEntity moveDir(String key, String fromPath, String toPath) {
        return null;
    }

}
