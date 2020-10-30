package org.scy.fs.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.scy.common.ds.mybatis.BaseMapper;
import org.scy.fs.model.RegisterModel;

/**
 * 注册信息映射类
 * Created by shicy on 2020/10/28
 */
@Mapper
public interface RegisterMapper extends BaseMapper<RegisterModel> {

    RegisterModel getByKey(String key);

}
