package org.scy.fs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scy.common.ds.mybatis.BaseMapper;
import org.scy.fs.model.FileEntityModel;

import java.util.List;

/**
 * 文件相关映射类
 * Created by shicy on 2020/10/28
 */
@Mapper
public interface FileEntityMapper extends BaseMapper<FileEntityModel> {

    FileEntityModel getByUuid(String uuid);

    FileEntityModel getDirByName(String key, String name, int parentId);

    List<FileEntityModel> getByUuids(String[] uuids);

    List<FileEntityModel> getByParentId(String key, int parentId);

}
