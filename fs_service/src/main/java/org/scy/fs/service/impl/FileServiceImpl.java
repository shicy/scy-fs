package org.scy.fs.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.scy.common.Const;
import org.scy.common.ds.PageInfo;
import org.scy.common.ds.query.Oper;
import org.scy.common.ds.query.Selector;
import org.scy.common.utils.ArrayUtilsEx;
import org.scy.common.utils.StringUtilsEx;
import org.scy.common.web.service.MybatisBaseService;
import org.scy.fs.form.SearchForm;
import org.scy.fs.manager.FileManager;
import org.scy.fs.mapper.FileEntityMapper;
import org.scy.fs.mapper.RegisterMapper;
import org.scy.fs.model.FileEntity;
import org.scy.fs.model.FileEntityModel;
import org.scy.fs.model.RegisterModel;
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

    @Autowired
    private RegisterMapper registerMapper;

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

        selector.addFilter("`key`", key);
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
            fileName = file.getOriginalFilename();
        String ext = StringUtils.substringAfterLast(fileName, ".");
        if (ext.length() > 20)
            ext = ext.substring(0, 20);

        FileEntityModel model = new FileEntityModel();
        model.setKey(key);
        model.setUuid(getUuid());
        model.setName(fileName);
        model.setExt(ext);
        model.setSize(file.getSize());
        model.setDirectory((short)0);
        model.setCreateDate(new Date());
        model.setState(Const.ENABLED);

        FileEntityModel parent = getByPath(key, path, true);
        model.setParentId(parent != null ? parent.getId() : 0);
        model.setParentIds(getParentIds(parent));

        entityMapper.add(model);

        RegisterModel registerModel = registerMapper.getByKey(key);
        registerModel.setSize(registerModel.getSize() + model.getSize());
        registerModel.setUpdateDate(new Date());
        registerMapper.update(registerModel);

        FileManager.save(model, file);

        return model;
    }

    @Override
    public FileEntityModel update(String key, FileEntity entity) {
        FileEntityModel model = getByUuid(key, entity.getUuid());
        if (model != null) {
            String name = entity.getName();
            if (StringUtils.isNotBlank(name)) {
                String ext = StringUtils.trimToEmpty(entity.getExt());
                if (StringUtils.isBlank(ext))
                    ext = StringUtils.substringAfterLast(name, ".");
                if (ext.length() > 20)
                    ext = ext.substring(0, 20);
                model.setName(name);
                model.setExt(ext);
            }
            model.setUpdateDate(new Date());
            entityMapper.update(model);
            return model;
        }
        return null;
    }

    @Override
    public FileEntityModel delete(String key, String uuid) {
        List<FileEntityModel> models = delete(key, new String[]{uuid});
        return models.size() > 0 ? models.get(0) : null;
    }

    @Override
    public List<FileEntityModel> delete(String key, String[] uuids) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        if (uuids != null && uuids.length > 0) {
            RegisterModel registerModel = registerMapper.getByKey(key);
            for (String uuid: uuids) {
                if (StringUtils.isNotBlank(uuid)) {
                    FileEntityModel model = entityMapper.getByUuid(uuid);
                    if (model != null && model.getKey().equals(key)) {
                        entityMapper.delete(model);
                        FileManager.remove(model);
                        registerModel.setSize(registerModel.getSize() - model.getSize());
                        entityModels.add(model);
                    }
                }
            }
            registerModel.setUpdateDate(new Date());
            registerMapper.update(registerModel);
        }
        return entityModels;
    }

    @Override
    public List<FileEntityModel> deleteDir(String key, String path, boolean includeSubDir, boolean includeFile) {
        FileEntityModel entityModel = getByPath(key, path, false);
        if (entityModel != null) {
            List<FileEntityModel> models = deleteDir(entityModel, includeSubDir, includeFile);
            if (models.size() > 0) {
                RegisterModel registerModel = registerMapper.getByKey(key);
                long totalSize = registerModel.getSize();
                for (FileEntityModel model: models) {
                    if (!model.isDir())
                        totalSize -= model.getSize();
                }
                if (totalSize != registerModel.getSize()) {
                    registerModel.setSize(totalSize);
                    registerModel.setUpdateDate(new Date());
                    registerMapper.update(registerModel);
                }
            }
            return models;
        }
        return null;
    }

    @Override
    public FileEntityModel moveFile(String key, String uuid, String toPath) {
        FileEntityModel entityModel = getByUuid(key, uuid);
        if (entityModel != null) {
            return move(entityModel, getByPath(key, toPath, true));
        }
        return null;
    }

    @Override
    public List<FileEntityModel> moveFiles(String key, String[] uuids, String toPath) {
        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        if (uuids != null && uuids.length > 0) {
            FileEntityModel parentModel = getByPath(key, toPath, true);
            for (String uuid: uuids) {
                if (StringUtils.isBlank(uuid))
                    continue;
                FileEntityModel model = getByUuid(key, uuid.trim());
                entityModels.add(move(model, parentModel));
            }
        }
        return entityModels;
    }

    @Override
    public FileEntityModel moveDir(String key, String fromPath, String toPath) {
        FileEntityModel entityModel = getByPath(key, fromPath, false);
        if (entityModel != null) {
            return move(entityModel, getByPath(key, toPath, true));
        }
        return null;
    }

    @Override
    public List<FileEntityModel> getFilePath(String key, String uuid) {
        FileEntityModel entityModel = getByUuid(key, uuid);
        if (entityModel == null)
            return new ArrayList<FileEntityModel>();

        String[] parentIds = StringUtils.split(entityModel.getParentIds(), ",");
        int[] ids = ArrayUtilsEx.toPrimitiveInt(parentIds);
        List<FileEntityModel> models = entityMapper.getByIds(ids);

        List<FileEntityModel> entityModels = new ArrayList<FileEntityModel>();
        for (int id: ids) {
            for (FileEntityModel model: models) {
                if (model.getId() == id)
                    entityModels.add(model);
            }
        }
        entityModels.add(entityModel);
        return entityModels;
    }

    @Override
    public List<FileEntityModel> getDirPath(String key, String path) {
        String[] dirNames = FileManager.getPaths(path);
        return getPathDirs(key, dirNames, false);
    }

    // ========================================================================
    /**
     * 获取文件唯一编号
     */
    private String getUuid() {
        String uuid = StringUtilsEx.getRandomString(32);
        uuid = uuid.toLowerCase();
        FileEntityModel model = entityMapper.getByUuid(uuid);
        if (model != null)
            return getUuid();
        return uuid;
    }

    /**
     * 作为上级目录，获取所有上级目录编号
     */
    private String getParentIds(FileEntityModel model) {
        if (model != null) {
            String parentIds = model.getParentIds();
            if (StringUtils.isBlank(parentIds))
                return "0," + model.getId() + ",0";
            parentIds = parentIds.substring(0, parentIds.length() - 1);
            return parentIds + model.getId() + ",0";
        }
        return "";
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
        model.setSize(0L);
        model.setDirectory((short)1);
        model.setCreateDate(new Date());
        model.setParentId(parent != null ? parent.getId() : 0);
        model.setParentIds(getParentIds(parent));
        model.setState(Const.ENABLED);
        entityMapper.add(model);
        return model;
    }

    /**
     * 删除目录
     * @param parent 想要删除的目录
     * @param includeSubDir 为true时删除子目录
     * @param includeFile 为true时删除文件
     * @return 被删除的文件及目录
     */
    private List<FileEntityModel> deleteDir(FileEntityModel parent, boolean includeSubDir, boolean includeFile) {
        List<FileEntityModel> deleteModels = new ArrayList<FileEntityModel>();

        int deleteCount = 0;
        List<FileEntityModel> models = entityMapper.getByParentId(parent.getKey(), parent.getId());
        if (models != null && models.size() > 0) {
            for (FileEntityModel model: models) {
                if (model.isDir()) {
                    if (includeSubDir) {
                        deleteModels.addAll(deleteDir(model, true, includeFile));
                        if (model.getState() == Const.DISABLED)
                            deleteCount += 1;
                    }
                }
                else if (includeFile) {
                    deleteCount += 1;
                    entityMapper.delete(model);
                    FileManager.remove(model);
                    deleteModels.add(model);
                }
            }
        }

        if (models == null || deleteCount == models.size()) {
            parent.setState(Const.DISABLED);
            entityMapper.delete(parent);
            deleteModels.add(parent);
        }

        return deleteModels;
    }

    /**
     * 移动文件或目录
     * @param model 被移动的文件或目录
     * @param parent 被移动到的上级目录
     * @return 移动后的文件信息
     */
    private FileEntityModel move(FileEntityModel model, FileEntityModel parent) {
        int parentId = parent != null ? parent.getId() : 0;
        String parentIds = getParentIds(parent);
        return move(model, parentId, parentIds);
    }

    /**
     * 移动文件或目录
     * @param model 被移动的文件或目录
     * @param parentId 上级目录编号
     * @param parentIds 所有上上级目录编号
     */
    private FileEntityModel move(FileEntityModel model, int parentId, String parentIds) {
        if (model.getId() != parentId &&
                !StringUtils.contains(parentIds, "," + model.getId() + ",")) {
            model.setParentId(parentId);
            model.setParentIds(parentIds);
            entityMapper.updateParent(model);
            if (model.isDir()) {
                List<FileEntityModel> models = entityMapper.getByParentId(model.getKey(), model.getId());
                if (models != null && models.size() > 0) {
                    parentId = model.getId();
                    parentIds = getParentIds(model);
                    for (FileEntityModel _model: models) {
                        move(_model, parentId, parentIds);
                    }
                }
            }
        }
        return model;
    }

}
