package org.scy.fs.service.impl;

import org.scy.common.web.service.MybatisBaseService;
import org.scy.fs.mapper.RegisterMapper;
import org.scy.fs.model.RegisterModel;
import org.scy.fs.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注册服务
 * Created by shicy 2020/10/28
 */
@Service
public class RegisterServiceImpl extends MybatisBaseService implements RegisterService {

    @Autowired
    private RegisterMapper registerMapper;

    @Override
    public RegisterModel getByKey(String key) {
        return registerMapper.getByKey(key);
    }

}
