package org.mendoraX.cluster;

import com.google.inject.Inject;
import com.hazelcast.config.*;
import io.reactivex.Single;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.VertxOptions;
import io.vertx.reactivex.core.Vertx;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;
import lombok.extern.slf4j.Slf4j;
import org.mendoraX.initData.Const;
import org.mendoraX.initData.vo.config.SysConfig;

/**
 * created by:xmf
 * date:2018/3/12
 * description:
 */
@Slf4j
public class Cluster {
    @Inject
    private SysConfig sysConfig;

    public Single<Vertx> rxInit() {
        return Single.create(singleEmitter -> init(vertxAsyncResult -> {
            if (vertxAsyncResult.succeeded())
                singleEmitter.onSuccess(vertxAsyncResult.result());
            else
                singleEmitter.onError(vertxAsyncResult.cause());
        }));
    }

    /**
     * initialization cluster
     */
    private void init(Handler<AsyncResult<Vertx>> handler) {
        Config config = new Config()
                .setProperty("hazelcast.logging.type", Const.TYPE_HAZELCAST_LOGGING)
                .setProperty("hazelcast.heartbeat.interval.seconds", sysConfig.getHeartbeatInterval());
        TcpIpConfig tcpIpConfig = new TcpIpConfig()
                .setEnabled(true)
                .setMembers(sysConfig.getClusterServerIps());
        JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(new MulticastConfig().setEnabled(false))
                .setTcpIpConfig(tcpIpConfig);
        NetworkConfig networkConfig = new NetworkConfig()
                .setPort(sysConfig.getClusterPort())
                .setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);

        // configuration and launching Vertx cluster.
        VertxOptions options = new VertxOptions()
                .setClusterManager(new HazelcastClusterManager(config));
        Vertx.clusteredVertx(options, res -> {
            if (res.succeeded()) {
                handler.handle(Future.succeededFuture(res.result()));
            } else {
                handler.handle(Future.failedFuture(res.cause()));
            }
        });
    }
}
