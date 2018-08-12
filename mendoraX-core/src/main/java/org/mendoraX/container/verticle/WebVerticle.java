package org.mendoraX.container.verticle;

import com.google.inject.Inject;
import io.reactivex.Observable;
import io.vertx.core.DeploymentOptions;
import io.vertx.reactivex.ext.web.Router;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mendoraX.initData.AbstractVerticle;
import org.mendoraX.initData.AbstractWebConfig;
import org.mendoraX.initData.tag.RequestRouting;
import org.mendoraX.initData.tag.Route;
import org.mendoraX.initData.vo.config.SysConfig;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
@Slf4j
public class WebVerticle extends AbstractVerticle {
    @Inject
    private SysConfig sysConfig;
    @Inject
    private AbstractWebConfig abstractWebConfig;

    private List<Object> routeList;

    public WebVerticle setRouteList(List<Object> routeList) {
        this.routeList = routeList;
        return this;
    }

    private Router router;

    @Override
    public DeploymentOptions deploymentOptions() {
        return new DeploymentOptions();
    }

    @Override
    public void start() throws Exception {
        this.router = Router.router(vertx);
        abstractWebConfig.config(sysConfig, router);

        Observable.fromIterable(routeList)
                .subscribe(this::invokeRequestRouting,
                        err -> log.error(err.getMessage()),
                        this::launchHttpServer);
    }

    /**
     * invoke request routing method.
     *
     * @param routeItem
     */
    @SneakyThrows
    private void invokeRequestRouting(Object routeItem) {
        Class<?> clazz = routeItem.getClass();
        Route route = clazz.getAnnotation(Route.class);
        String prefix = route.value();

        Method[] methods = clazz.getMethods();
        for (Method method : Arrays.asList(methods)) {
            if (method.isAnnotationPresent(RequestRouting.class)) {
                RequestRouting requestRouting = method.getAnnotation(RequestRouting.class);
                String path = requestRouting.path();
                int order = requestRouting.order();
                path = (StringUtils.isNotEmpty(prefix)) ? prefix + path : path;
                router.route(requestRouting.method(), path).order(order).handler(rc -> {
                    try {
                        method.invoke(routeItem, rc);
                    } catch (Exception e) {
                        Throwable e0 = e.getCause();
                        if (e0 != null) {
                            log.error(e0.getClass().getName() + "==>" + e0.getStackTrace()[0].toString());
                        } else {
                            log.error("nocause：" + e.getStackTrace()[0].toString());
                        }
                    }
                });
            }
        }
    }

    /**
     * launching http server.
     */
    private void launchHttpServer() {
        log.info("all the \"route\" deployed, see the api below:");
        router.getRoutes().forEach(r -> log.info(r.getPath()));
        // when the 'port' field was empty than default port value 80.
        vertx.createHttpServer().requestHandler(router::accept)
                .listen(sysConfig.web().getHttpPort());
        log.info("web server listenning at port:{}", sysConfig.web().getHttpPort());
    }
}
