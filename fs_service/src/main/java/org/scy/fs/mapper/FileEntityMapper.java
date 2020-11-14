package org.scy.fs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.scy.common.ds.mybatis.BaseMapper;
import org.scy.fs.model.FileEntityModel;

import java.util.List;

/**
 * 文件相关映射类
 * Created by shicy on 2020/10/28
 */
@Mapper
public interface FileEntityMapper extends BaseMapper<FileEntityModel> {

    FileEntityModel getByUuid(@Param("uuid") String uuid);

    FileEntityModel getDirByName(@Param("key") String key, @Param("name") String name, @Param("parentId") int parentId);

    List<FileEntityModel> getByIds(@Param("ids") int[] ids);

    List<FileEntityModel> getByUuids(@Param("uuids") String[] uuids);

    List<FileEntityModel> getByParentId(@Param("key") String key, @Param("parentId") int parentId);

    void updateParent(FileEntityModel model);

}
