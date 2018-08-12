package org.mendoraX.initData;

import io.vertx.reactivex.ext.web.Router;
import org.mendoraX.initData.vo.config.SysConfig;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
public abstract class AbstractWebConfig {
    public abstract void config(SysConfig sysConfig, Router router);
}
