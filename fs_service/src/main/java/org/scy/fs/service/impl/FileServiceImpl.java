package org.scy.fs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.scy.common.ds.PageInfo;
import org.scy.common.ds.query.Oper;
import org.scy.common.ds.query.Selector;
import org.scy.common.utils.StringUtilsEx;
import org.scy.common.web.service.MybatisBaseService;
import org.scy.fs.form.SearchForm;
import org.scy.fs.manager.FileManager;
import org.scy.fs.mapper.FileEntityMapper;
import org.scy.fs.model.FileEntityModel;
import org.scy.fs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文件服务
 * Created by shicy on 2020/10/28
 */
@Service
public class FileServiceImpl extends MybatisBaseService implements FileService {

    @Autowired
    private FileEntityMapper entityMapper;

    @Override
    public FileEntityModel getByUuid(String key, String uuid) {
        FileEntityModel entityModel = entityMapper.getByUuid(uuid);
        if (entityModel != null) {
            if (entityModel.getKey().equals(key))
                return entityModel;
        }
        return null;
    }

    @Override
    public List<FileEntityModel> getByUuids(String key, String[] uuids) {
        List<FileEntityModel> results = new ArrayList<FileEntityModel>();
        if (uuids != null && uuids.length > 0) {
            List<FileEntityModel> entityModels = entityMapper.getByUuids(uuids);
            for (FileEntityModel entityModel: entityModels) {
                if (entityModel.getKey().equals(key))
                    results.add(entityModel);
            }
        }
        return results;
    }

    @Override
    public List<FileEntityModel> find(String key, SearchForm form, PageInfo pageInfo) {
        Selector selector = Selector.build(pageInfo);

        selector.addFilter("key", key);
        if (form.getName() != null)
            selector.addFilter("name", form.getName());
        if (form.getNameLike() != null)
            selector.addFilter("name", form.getNameLike(), Oper.LIKE);
        if (form.getParentId() >= 0)
            selector.addFilter("parentId", form.getParentId());
        if (form.getPath() != null) {
            FileEntityModel model = getByPath(key, form.getPath(), false);
            if (model != null)
                selector.addFilter("parentId", model.getId());
        }

        if (form.getOrderBy() != null)
            selector.addOrder(form.getOrderBy(), !form.isOrderDesc());

        if (pageInfo != null)
            pageInfo.setTotal(entityMapper.countFind(selector));
        return entityMapper.find(selector);
    }

    @Override
    public FileEntityModel add(String key, MultipartFile file, String fileName, String path) {
        if (StringUtils.isBlank(fileName))
            fileName = file.getName();
        String ext = StringUtils.substringAfterLast(fileName, ".");
        if (ext.length() > 20)
            ext = ext.substring(0, 20);

        FileEntityModel model = new FileEntityModel();
        model.setKey(key);
        model.setUuid(getUuid());
        model.setName(fileName);
        model.setExt(ext);
        model.setDirectory((short)0);
        model.setCreateDate(new Date());
        model.setParentId(0);
        model.setParentIds("");

        FileEntityModel parent = getByPath(key, path, true);
        if (parent != null) {
            model.setParentId(parent.getId());
            String parentIds = parent.getParentIds();
            if (parentIds.length() > 0) {
                parentIds = parentIds.substring(0, parentIds.length() - 1);
                parentIds += parent.getId() + ",0";
                model.setParentIds(parentIds);
            }
        }

        entityMapper.add(model);

        FileManager.save(model, file);

        return model;
    }

    @Override
    public FileEntityModel delete(String key, String uuid) {
        FileEntityModel model = entityMapper.getByUuid(uuid);
        if (model != null && model.getKey().equals(key)) {
            entityMapper.delete(model);
            FileManager.remove(model);
            return model;
        }
        return null;
    }

    @Override
    public List<FileEntityModel> delete(String key, String[] uuids) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        for (String uuid: uuids) {
            if (StringUtils.isNotBlank(uuid)) {
                FileEntityModel entityModel = delete(key, uuid);
                if (entityModel != null)
                    entityModels.add(entityModel);
            }
        }
        return entityModels;
    }

    @Override
    public List<FileEntityModel> deleteDir(String key, String path, boolean includeSubDir, boolean includeFile) {
        return null;
    }

    @Override
    public FileEntityModel moveFile(String key, String uuid, String toPath) {
        return null;
    }

    @Override
    public List<FileEntityModel> moveFiles(String key, String[] uuids, String toPath) {
        return null;
    }

    @Override
    public FileEntityModel moveDir(String key, String fromPath, String toPath) {
        return null;
    }

    // ========================================================================
    /**
     * 获取文件唯一编号
     */
    private String getUuid() {
        String uuid = StringUtilsEx.getRandomString(32);
        FileEntityModel model = entityMapper.getByUuid(uuid);
        if (model != null)
            return getUuid();
        return uuid;
    }

    /**
     * 根据路径获取目录信息
     * @param key 第三方key
     * @param path 路径，如：/a/b
     * @param autoCreate 是否自动创建目录
     * @return 该路径末尾的目录信息
     */
    private FileEntityModel getByPath(String key, String path, boolean autoCreate) {
        String[] dirNames = FileManager.getPaths(path);
        if (dirNames.length > 0) {
            List<FileEntityModel> models = getPathDirs(key, dirNames, autoCreate);
            if (dirNames.length == models.size()) {
                return models.get(dirNames.length - 1);
            }
        }
        return null;
    }

    /**
     * 获取或创建目录
     * @param key 第三方key
     * @param dirNames 目录名称
     * @param autoCreate 是否自动创建目录
     * @return 目录信息
     */
    private List<FileEntityModel> getPathDirs(String key, String[] dirNames, boolean autoCreate) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        int parentId = 0;
        FileEntityModel parentModel = null;
        for (String dirName: dirNames) {
            FileEntityModel model = entityMapper.getDirByName(key, dirName, parentId);
            if (model == null && autoCreate) {
                model = addDir(key, dirName, parentModel);
            }
            if (model == null)
                break;
            entityModels.add(model);
            parentId = model.getId();
            parentModel = model;
        }
        return entityModels;
    }

    /**
     * 添加目录
     * @param key 第三方key
     * @param name 目录名称
     * @param parent 上级目录
     * @return 目录实例
     */
    private FileEntityModel addDir(String key, String name, FileEntityModel parent) {
        FileEntityModel model = new FileEntityModel();
        model.setKey(key);
        // model.setUuid(getUuid()); // 目录就不给唯一编号了
        model.setName(name);
        model.setDirectory((short)1);
        model.setCreateDate(new Date());
        if (parent != null) {
            model.setParentId(parent.getId());
            String parentIds = parent.getParentIds();
            if (parentIds.length() > 0) {
                parentIds = parentIds.substring(0, parentIds.length() - 1);
                parentIds += parent.getId() + ",0";
            }
            else {
                parentIds = "0," + parent.getId() + ",0";
            }
            model.setParentIds(parentIds);
        }
        else {
            model.setParentId(0);
            model.setParentIds("");
        }
        entityMapper.add(model);
        return model;
    }

}
