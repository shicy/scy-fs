package org.scy.fs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.BAD_PARAM;
import org.scy.common.ds.PageInfo;
import org.scy.common.ds.query.Oper;
import org.scy.common.ds.query.Selector;
import org.scy.common.utils.StringUtilsEx;
import org.scy.common.web.service.MybatisBaseService;
import org.scy.fs.form.SearchForm;
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
            List<FileEntityModel> models = getByPath(form.getPath());
            if (models.size() > 0) {
                selector.addFilter("parentId", models.get(models.size() - 1).getId());
            }
        }

        if (form.getOrderBy() != null)
            selector.addOrder(form.getOrderBy(), !form.isOrderDesc());

        if (pageInfo != null)
            pageInfo.setTotal(entityMapper.countFind(selector));
        return entityMapper.find(selector);
    }

    @Override
    public FileEntityModel add(String key, MultipartFile file, String fileName, String path) {

        return null;
    }

    @Override
    public FileEntityModel delete(String key, String uuid) {
        return null;
    }

    @Override
    public List<FileEntityModel> delete(String key, String[] uuids) {
        return null;
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
    private String getUuid() {
        String uuid = StringUtilsEx.getRandomString(32);
        FileEntityModel model = entityMapper.getByUuid(uuid);
        if (model != null)
            return getUuid();
        return uuid;
    }

    private List<FileEntityModel> getByPath(String path) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        if (StringUtils.isNotBlank(path)) {
            int parentId = 0;
            String[] paths = StringUtils.split(path, "/");
            for (String _path: paths) {
                _path = StringUtils.trimToEmpty(_path);
                if (_path.length() == 0)
                    continue;
                FileEntityModel entityModel = entityMapper.getDirByName(_path, parentId);
                if (entityModel == null)
                    break;
                entityModels.add(entityModel);
                parentId = entityModel.getId();
            }
        }
        return  entityModels;
    }

    private List<FileEntityModel> mkdirs(String key, String path) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        if (StringUtils.isNotBlank(path)) {
            int parentId = 0;
            String[] paths = StringUtils.split(path.trim(), "/");
            for (String _path: paths) {
                _path = StringUtils.trimToEmpty(_path);
                if (_path.length() == 0)
                    continue;
                FileEntityModel entityModel = entityMapper.getDirByName(_path, parentId);
                if (entityModel == null) {
                    entityModel = new FileEntityModel();

                }
            }
        }
        return entityModels;
    }

    private FileEntityModel addDir(String key, String name, FileEntityModel parent) {
        FileEntityModel model = new FileEntityModel();
        model.setKey(key);
        model.setName(name);
        model.setDirectory((short)1);
        model.setCreateDate(new Date());
        if (parent != null) {
            model.setParentId(parent.getId());
            String parentIds = parent.getParentIds();
            if (parentIds.length() > 0) {
                String[] _parentIds = StringUtils.split(parentIds, ",");
                _parentIds[_parentIds.length - 1] = "" + parent.getId();
                parentIds = StringUtils.join(_parentIds, ",") + ",0";
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
