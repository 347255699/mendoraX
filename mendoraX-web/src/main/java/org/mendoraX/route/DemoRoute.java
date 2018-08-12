package org.mendoraX.route;

import io.vertx.reactivex.ext.web.RoutingContext;
import org.mendoraX.initData.tag.RequestRouting;
import org.mendoraX.initData.tag.Route;

/**
 * @auther menfre
 * @date 2018/8/12
 * version: 1.0
 * desc:
 */
@Route("/api")
public class DemoRoute {
    @RequestRouting(path = "/hello")
    public void demo(RoutingContext rc) {
        rc.response().end("ok");
    }
}
