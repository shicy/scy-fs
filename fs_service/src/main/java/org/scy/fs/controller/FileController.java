package org.scy.fs.controller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.scy.common.Const;
import org.scy.common.ds.PageInfo;
import org.scy.common.exception.ResultException;
import org.scy.common.utils.HttpUtilsEx;
import org.scy.common.web.controller.BaseController;
import org.scy.common.web.controller.HttpResult;
import org.scy.fs.form.SearchForm;
import org.scy.fs.manager.FileManager;
import org.scy.fs.model.FileEntity;
import org.scy.fs.model.FileEntityModel;
import org.scy.fs.model.RegisterModel;
import org.scy.fs.service.FileService;
import org.scy.fs.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理
 * Created by shicy on 2020/10/27
 */
@Controller
public class FileController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private RegisterService registerService;

    @Autowired
    private FileService fileService;

    /**
     * 查询，获取文件及目录信息
     * @param request 参数：
     *   -param key 第三方key值
     *   -param name 按名称查询
     *   -param nameLike 按名称模糊查询
     *   -param parentId 查询该目录下的文件或子目录
     *   -param path 查询该目录下的文件或子目录，需要解析，格式如：/a/b
     *   -param orderBy 排序字段
     *   -param orderDesc 是否降序
     *   -param page 页码
     *   -param size 分页大小
     */
    @RequestMapping(value = "/file/list", method = RequestMethod.GET)
    @ResponseBody
    public Object list(HttpServletRequest request) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));

        SearchForm form = new SearchForm();
        form.setName(HttpUtilsEx.getStringValue(request, "name"));
        form.setNameLike(HttpUtilsEx.getStringValue(request, "nameLike"));
        form.setParentId(HttpUtilsEx.getIntValue(request, "parentId", 0));
        form.setPath(HttpUtilsEx.getStringValue(request, "path"));
        form.setOrderBy(HttpUtilsEx.getStringValue(request, "orderBy"));
        form.setOrderDesc(HttpUtilsEx.getBooleanValue(request, "orderDesc"));

        PageInfo pageInfo = PageInfo.create(request);

        List<FileEntityModel> entities = fileService.find(key, form, pageInfo);

        return HttpResult.ok(entities, pageInfo);
    }

    /**
     * 批量获取文件信息
     * @param request 参数：
     *   -param key 第三方key值
     *   -param uuids 文件唯一编号，多个值用逗号分隔
     * @return 文件列表
     */
    @RequestMapping(value = "/file/info", method = RequestMethod.GET)
    @ResponseBody
    public Object info(HttpServletRequest request) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));

        String uuid = HttpUtilsEx.getStringValue(request, "uuids");
        if (StringUtils.isBlank(uuid))
            uuid = HttpUtilsEx.getStringValue(request, "uuid");
        uuid = StringUtils.trimToNull(uuid);
        if (uuid == null)
            return HttpResult.error(Const.MSG_CODE_PARAMMISSING);

        String[] uuids = StringUtils.split(uuid, ",");
        List<FileEntityModel> fileEntities = fileService.getByUuids(key, uuids);
        return HttpResult.ok(fileEntities);
    }

    /**
     * 获取文件信息
     * @param key 第三方key值
     * @param uuid 文件唯一编号
     * @return 文件信息
     */
    @RequestMapping(value = "/file/info/{uuid}", method = RequestMethod.GET)
    @ResponseBody
    public Object info(@RequestParam("key") String key, @PathVariable("uuid") String uuid) {
        checkKey(key);

        if (StringUtils.isNotBlank(uuid)) {
            return HttpResult.ok(fileService.getByUuid(key, uuid));
        }

        return HttpResult.ok();
    }

    /**
     * 上传
     * @param request 参数：
     *   -param key 第三方key值
     *   -param fileName 文件名称
     *   -param path 存储目录
     * @param file 文件流
     * @return 文件信息
     */
    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(HttpServletRequest request, MultipartFile file) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));

        if (file != null) {
            String fileName = HttpUtilsEx.getStringValue(request, "fileName");
            String path = HttpUtilsEx.getStringValue(request, "path");
            FileEntity entity = fileService.add(key, file, fileName, path);
            return HttpResult.ok(entity);
        }

        return HttpResult.error(Const.MSG_CODE_PARAMMISSING);
    }

    /**
     * 下载
     * @param request 参数：
     *   -param key 第三方key值
     *   -param uuid 要下载的文件唯一编号
     *   -param uuids 批量下载时，想要下载的文件唯一编号集，逗号分隔
     *   -param fileName 重命名下载文件名称
     */
    @RequestMapping(value = "/file/download")
    public Object download(HttpServletRequest request, HttpServletResponse response) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));

        response.reset();
        response.setContentType("application/octet-stream;charset=utf-8");

        OutputStream output = null;
        try {
            output = response.getOutputStream();

            String uuid = HttpUtilsEx.getStringValue(request, "uuids");
            String fileName = HttpUtilsEx.getStringValue(request, "fileName");
            if (StringUtils.isNotBlank(uuid)) { // 批量下载
                String[] uuids = StringUtils.split(uuid.trim(), ",");
                List<FileEntityModel> entities = fileService.getByUuids(key, uuids);

                FileEntity[] _entities = entities.toArray(new FileEntity[0]);
                // 总大小不确定，无法设置
                // response.addHeader("Content-Length", "" + FileManager.getSize(_entities));
                response.addHeader("Content-Encoding", "chunked");

                if (StringUtils.isBlank(fileName) && _entities.length > 0)
                    fileName = StringUtils.substringBeforeLast(_entities[0].getName(), ".") + ".zip";
                fileName = HttpUtilsEx.encodeFileName(request, fileName);
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                FileManager.stream(_entities, output);
                return null;
            }

            uuid = HttpUtilsEx.getStringValue(request, "uuid");
            if (StringUtils.isNotBlank(uuid)) { // 单文件下载
                FileEntity entity = fileService.getByUuid(key, uuid);
                if (entity != null) {
                    response.addHeader("Content-Length", "" + entity.getSize());

                    if (StringUtils.isBlank(fileName))
                        fileName = entity.getName();
                    fileName = HttpUtilsEx.encodeFileName(request, fileName);
                    response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                    FileManager.stream(entity, output);
                    return null;
                }

                response.reset();
                return HttpResult.error("文件不存在！");
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            response.reset();
            return HttpResult.error("文件下载错误");
        } finally {
            IOUtils.closeQuietly(output);
        }

        response.reset();
        return HttpResult.error(Const.MSG_CODE_PARAMMISSING);
    }

    /**
     * 删除
     * @param request 参数：
     *   -param key 第三方key值
     *   -param uuid 要删除的文件唯一编号
     *   -param uuids 批量删除，想要删除的文件唯一编号集，逗号分隔
     *   -param path 想要删除的目录
     *   -param includeSubDir 是否包含子目录，默认当存在子目录时不会删除该目录，为true时将同时删除子目录
     *   -param includeFile 是否包含目录下的文件，默认当存在文件时不会删除该目录（包含子目录），为true时将同时删除文件
     * @return 被删除的文件信息
     */
    @RequestMapping(value = "/file/delete", method = RequestMethod.POST)
    @ResponseBody
    public Object delete(HttpServletRequest request) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));

        List<FileEntity> fileEntities = new ArrayList<FileEntity>();

        String uuid = HttpUtilsEx.getStringValue(request, "uuid");
        if (StringUtils.isNotBlank(uuid)) {
            FileEntity entity = fileService.delete(key, uuid);
            if (entity != null)
                fileEntities.add(entity);
        }

        String uuids = HttpUtilsEx.getStringValue(request, "uuids");
        if (StringUtils.isNotBlank(uuids)) {
            String[] _uuids = StringUtils.split(uuids, ",");
            fileEntities.addAll(fileService.delete(key, _uuids));
        }

        String path = HttpUtilsEx.getStringValue(request, "path");
        if (StringUtils.isNotBlank(path)) {
            boolean includeSubDir = HttpUtilsEx.getBooleanValue(request, "includeSubDir");
            boolean includeFile = HttpUtilsEx.getBooleanValue(request, "includeFile");
            fileEntities.addAll(fileService.deleteDir(key, path, includeSubDir, includeFile));
        }

        return HttpResult.ok(fileEntities);
    }

    /**
     * 移动
     * @param request 参数：
     *   -param key 第三方key值
     *   -param uuid 想要移动的文件唯一编号
     *   -param uuids 想要移动的文件唯一编号集，逗号分隔
     *   -param path 想要移到的目录
     *   -param toPath 文件移到的目标文件目录
     * @return 被移动的文件或目录
     */
    @RequestMapping(value = "/file/move", method = RequestMethod.POST)
    @ResponseBody
    public Object move(HttpServletRequest request) {
        String key = checkKey(HttpUtilsEx.getStringValue(request, "key"));
        String toPath = HttpUtilsEx.getStringValue(request, "toPath");
        toPath = StringUtils.trimToEmpty(toPath);

        List<FileEntity> fileEntities = new ArrayList<FileEntity>();

        String uuid = HttpUtilsEx.getStringValue(request, "uuid");
        if (StringUtils.isNotBlank(uuid)) {
            FileEntity entity = fileService.moveFile(key, uuid, toPath);
            if (entity != null)
                fileEntities.add(entity);
        }

        String uuids = HttpUtilsEx.getStringValue(request, "uuids");
        if (StringUtils.isNotBlank(uuids)) {
            String[] _uuids = StringUtils.split(uuid, ",");
            fileEntities.addAll(fileService.moveFiles(key, _uuids, toPath));
        }

        String path = HttpUtilsEx.getStringValue(request, "path");
        if (StringUtils.isNotBlank(path)) {
            FileEntity entity = fileService.moveDir(key, path, toPath);
            if (entity != null)
                fileEntities.add(entity);
        }

        return HttpResult.ok(fileEntities);
    }

    /**
     * 验证第三方key是否有效
     * @param key 第三方key
     */
    private String checkKey(String key) {
        if (!isKeyEnabled(key))
            throw new ResultException("无效key");
        return key;
    }

    /**
     * 判断第三方key是否有效
     * @param key 第三方key
     */
    private boolean isKeyEnabled(String key) {
        if (StringUtils.isBlank(key))
            return false;

        RegisterModel registerModel = registerService.getByKey(key);
        if (registerModel == null)
            return false;

        return registerModel.getState() == 1;
    }

}
