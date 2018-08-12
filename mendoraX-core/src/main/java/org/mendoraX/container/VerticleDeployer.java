package org.mendoraX.container;

import io.vertx.core.Vertx;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mendoraX.initData.AbstractVerticle;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
@Slf4j
class VerticleDeployer {
    @Setter
    private Vertx vertx;

    private VerticleDeployer() {

    }

    static VerticleDeployer newDeployer() {
        return new VerticleDeployer();
    }

    /**
     * deploy config and record info.
     *
     * @param verticle
     */
    void deploy(AbstractVerticle verticle) {
        vertx.deployVerticle(verticle, verticle.deploymentOptions(), res -> {
            if (res.succeeded()) {
                log.info(verticle.getClass().getName() + " deployed.");
            } else {
                log.error("deploy {} error happened.", verticle.getClass().getName());
            }
        });
    }
}
