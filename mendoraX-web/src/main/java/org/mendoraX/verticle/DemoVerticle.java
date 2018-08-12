package org.mendoraX.verticle;

import io.vertx.core.DeploymentOptions;
import lombok.extern.slf4j.Slf4j;
import org.mendoraX.initData.AbstractVerticle;
import org.mendoraX.initData.tag.Verticle;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
@Verticle
@Slf4j
public class DemoVerticle extends AbstractVerticle{
    @Override
    public DeploymentOptions deploymentOptions() {
        return new DeploymentOptions();
    }

    @Override
    public void start() throws Exception {
       log.info("ok");
    }
}
