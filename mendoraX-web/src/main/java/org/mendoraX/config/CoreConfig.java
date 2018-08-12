package org.mendoraX.config;

import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.LoggerHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import org.mendoraX.initData.AbstractWebConfig;
import org.mendoraX.initData.tag.WebConfig;
import org.mendoraX.initData.vo.config.SysConfig;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
@WebConfig
public class CoreConfig extends AbstractWebConfig {

    @Override
    public void config(SysConfig sysConfig, Router router) {
        // use http request logging.
        router.route().handler(LoggerHandler.create(LoggerFormat.TINY));
        // use http request body as Json,Buffer,String.
        router.route("/static/*").handler(StaticHandler.create().setWebRoot("aider"));
        router.route().handler(BodyHandler.create().setBodyLimit(sysConfig.web().getHttpBodyLimit()));
    }
}
