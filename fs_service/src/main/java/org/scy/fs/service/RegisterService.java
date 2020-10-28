package org.scy.fs.service;

import org.scy.fs.model.RegisterModel;

/**
 * 注册服务
 * Created by shicy on 2020/10/28
 */
public interface RegisterService {

    /**
     * 根据key值获取注册信息
     * @param key 第三方key值
     * @return 第三方注册信息
     */
    RegisterModel getByKey(String key);

}
