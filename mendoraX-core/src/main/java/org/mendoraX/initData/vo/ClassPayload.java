package org.mendoraX.initData.vo;

import lombok.Getter;
import org.mendoraX.initData.AbstractVerticle;
import org.mendoraX.initData.AbstractWebConfig;

import java.util.List;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
public class ClassPayload {

    @Getter
    private List<AbstractVerticle> verticleList;
    @Getter
    private List<Object> routeList;
    @Getter
    private List<Class> componentList;
    @Getter
    private AbstractWebConfig webConfig;

    public ClassPayload setVerticleList(List<AbstractVerticle> verticleList) {
        this.verticleList = verticleList;
        return this;
    }

    public ClassPayload setRouteList(List<Object> routeList) {
        this.routeList = routeList;
        return this;
    }

    public ClassPayload setComponentList(List<Class> componentList) {
        this.componentList = componentList;
        return this;
    }

    public ClassPayload setWebConfig(AbstractWebConfig webConfig) {
        this.webConfig = webConfig;
        return this;
    }
}
