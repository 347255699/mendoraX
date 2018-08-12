package org.mendoraX.container.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.vertx.reactivex.core.Vertx;
import org.mendoraX.initData.AbstractWebConfig;
import org.mendoraX.initData.tag.Component;
import org.mendoraX.initData.vo.config.WebConfig;

import java.util.List;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
public class VertxModule extends AbstractModule {

    private Vertx vertx;

    private List<Class> componentSet;

    private AbstractWebConfig webConfig;


    public VertxModule setVertx(Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    public VertxModule setComponentSet(List<Class> componentSet) {
        this.componentSet = componentSet;
        return this;
    }

    public VertxModule setWebConfig(AbstractWebConfig webConfig) {
        this.webConfig = webConfig;
        return this;
    }

    @Override
    protected void configure() {
        componentSet.forEach(clazz -> {
            Component annotation = (Component) clazz.getAnnotation(Component.class);
            Class<?> binder = annotation.binder();
            bind(binder).to(clazz);
        });
        bind(AbstractWebConfig.class).toInstance(webConfig);
    }

    @Provides
    @Singleton
    public Vertx provideVertx() {
        return vertx;
    }

    @Provides
    @Singleton
    public io.vertx.core.Vertx provideVertxDelegate() {
        return vertx.getDelegate();
    }
}