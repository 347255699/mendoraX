package org.mendoraX.container;

import com.google.inject.Injector;
import io.reactivex.Observable;
import io.vertx.reactivex.core.Vertx;
import lombok.Getter;
import org.apache.logging.log4j.core.config.Configurator;
import org.mendoraX.cluster.Cluster;
import org.mendoraX.container.module.VertxModule;
import org.mendoraX.container.verticle.WebVerticle;
import org.mendoraX.initData.AbstractVerticle;
import org.mendoraX.initData.vo.ClassPayload;
import org.mendoraX.initData.vo.MicroService;
import org.mendoraX.initData.vo.config.SysConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
public class GuiceContext {

    @Getter
    private Injector injector;
    private Logger log;
    private Vertx vertx;

    private static class GuiceContextHolder {
        private static final GuiceContext MY_GUICE_CONTEXT = new GuiceContext();
    }

    private GuiceContext() {
    }

    public GuiceContext setInjector(Injector injector) {
        this.injector = injector;
        return this;
    }

    public <T> T get(Class<T> type) {
        return injector.getInstance(type);
    }

    public void build() {
        SysConfig sysConfig = injector.getInstance(SysConfig.class);
        // initialization logger
        System.setProperty("vertx.logger-delegate-factory-class-name", sysConfig.getLoggerFactoryClass());
        Configurator.initialize("Log4j2", sysConfig.getRootPath() + sysConfig.getLoggerConfigPath());
        this.log = LoggerFactory.getLogger(GuiceContext.class);

        Classifier classifier = injector.getInstance(Classifier.class);
        classifier.setInjector(injector);

        VertxModule vertxModule = new VertxModule();
        VerticleDeployer verticleDeployer = VerticleDeployer.newDeployer();

        injector.getInstance(Cluster.class)
                .rxInit()
                .flatMap(vertx -> {
                    this.vertx = vertx;
                    vertxModule.setVertx(vertx);
                    verticleDeployer.setVertx(vertx.getDelegate());
                    return classifier.rxSort();
                })
                .map(classPayload -> {
                    vertxModule.setComponentSet(classPayload.getComponentList());
                    vertxModule.setWebConfig(classPayload.getWebConfig());
                    this.injector = injector.createChildInjector(vertxModule);
                    buildExtension(classPayload);
                    return classPayload.getVerticleList();
                })
                .flatMapObservable(Observable::fromIterable)
                .subscribe(verticleDeployer::deploy,
                        err -> log.error(err.getMessage()),
                        () -> log.info("all the \"verticle\" deploy over.")
                );
    }

    public static GuiceContext newContext() {
        return GuiceContextHolder.MY_GUICE_CONTEXT;
    }

    private void buildExtension(ClassPayload classPayload) {
        MicroService microService = injector.getInstance(MicroService.class);
        if (microService == MicroService.WEB) {
            AbstractVerticle abstractVerticle = injector.getInstance(WebVerticle.class).setRouteList(classPayload.getRouteList());
            vertx.getDelegate().deployVerticle(abstractVerticle, abstractVerticle.deploymentOptions());
        }
    }

}
