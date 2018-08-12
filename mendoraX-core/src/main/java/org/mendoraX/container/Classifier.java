package org.mendoraX.container;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mendoraX.container.scanner.PackageScanner;
import org.mendoraX.initData.AbstractVerticle;
import org.mendoraX.initData.AbstractWebConfig;
import org.mendoraX.initData.Const;
import org.mendoraX.initData.tag.Component;
import org.mendoraX.initData.tag.Route;
import org.mendoraX.initData.tag.Verticle;
import org.mendoraX.initData.tag.WebConfig;
import org.mendoraX.initData.vo.ClassPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc: classifier
 */
@Slf4j
class Classifier {

    @Setter
    private Injector injector;

    @Inject
    private PackageScanner packageScanner;
    @Inject
    @Named(Const.TAG_BASE_PACKAGE)
    private String basePackage;
    private List<Object> routeList = new ArrayList<>();
    private List<AbstractVerticle> verticleList = new ArrayList<>();
    private List<Class> componentList = new ArrayList<>();
    private AbstractWebConfig webConfig;


    private boolean isVerticle(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Verticle.class)) {
            verticleList.add((AbstractVerticle)injector.getInstance(clazz));
            return false;
        }
        return true;
    }

    private boolean isRouter(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Route.class)) {
            routeList.add(injector.getInstance(clazz));
            return false;
        }
        return true;
    }

    private boolean isComponent(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Component.class)) {
            componentList.add(clazz);
            return false;
        }
        return true;
    }

    private boolean isWebConfig(Class<?> clazz) {
        if (clazz.isAnnotationPresent(WebConfig.class)) {
            this.webConfig = (AbstractWebConfig) injector.getInstance(clazz);
            return false;
        }
        return true;
    }

    /**
     * sort the class with rx format.
     *
     * @return
     */
    Single<ClassPayload> rxSort() {
        return Single.create(singleEmitter -> sort(asyncResult -> {
            if (asyncResult.succeeded())
                singleEmitter.onSuccess(asyncResult.result());
            else
                singleEmitter.onError(asyncResult.cause());
        }));
    }

    /**
     * sort the class.
     */
    private void sort(Handler<AsyncResult<ClassPayload>> handler) {
        Observable.fromIterable(packageScanner.classNames(basePackage))
                .map(Class::forName)
                .filter(this::isVerticle)
                .filter(this::isRouter)
                .filter(this::isComponent)
                .filter(this::isWebConfig)
                .subscribe(clazz -> {
                        },
                        err -> {
                            handler.handle(Future.failedFuture(err));
                        },
                        () -> {
                            log.info("all the \"class\" scanning over.");
                            ClassPayload payload = new ClassPayload()
                                    .setComponentList(componentList)
                                    .setRouteList(routeList)
                                    .setVerticleList(verticleList)
                                    .setWebConfig(webConfig);
                            handler.handle(Future.succeededFuture(payload));
                        });
    }
}
