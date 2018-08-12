package org.mendoraX.initData;

import io.vertx.core.DeploymentOptions;

/**
 * Created by kam on 2018/2/4.
 */
public abstract class AbstractVerticle extends io.vertx.reactivex.core.AbstractVerticle {
    /**
     * provide your deployment options or user default option.
     *
     * @return
     */
    public abstract DeploymentOptions deploymentOptions();
}
