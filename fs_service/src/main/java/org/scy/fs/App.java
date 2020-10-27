package org.scy.fs;

import org.scy.common.BaseApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * 文件管理系统
 * Created by shicy on 2020/10/27
 */
@SpringBootApplication(scanBasePackages = {"org.scy"})
@EnableFeignClients(basePackages = {"org.scy"})
public class App extends BaseApplication {

    public static void main(String[] args) {
        BaseApplication.startup(App.class, args);
    }

    @Override
    protected String getDbScriptResource() {
        return "org.scy.fs.scripts";
    }

}
