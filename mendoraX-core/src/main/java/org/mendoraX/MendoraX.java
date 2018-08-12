package org.mendoraX;

import com.google.inject.Guice;
import org.mendoraX.container.GuiceContext;
import org.mendoraX.container.module.CoreModule;
import org.mendoraX.initData.Const;
import org.mendoraX.initData.vo.MicroService;
import org.mendoraX.initData.vo.config.SysConfig;

import java.net.URL;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
public class MendoraX {
    public static void launch(ClassLoader cl, MicroService microService) {
        // loading app root path.
        URL rootUrl = SysConfig.class.getProtectionDomain().getCodeSource().getLocation();
        String rootPath = rootUrl.getPath().substring(0, rootUrl.getPath().lastIndexOf("/"));
        System.setProperty(Const.TAG_ROOT_PATH, rootPath);

        GuiceContext.newContext()
                .setInjector(Guice.createInjector(new CoreModule(cl, microService)))
                .build();
    }
}
