package org.mendoraX.initData.vo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.mendoraX.initData.Const;

import java.io.File;
import java.util.List;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc: 全局配置文件
 */
public class SysConfig {
    @Getter
    private String rootPath;

    @Inject
    private WebConfig webConfig;

    @Inject
    private DataSource dataSource;

    /**
     * all into base package will be scanning.
     */
    @Getter
    @Setter
    private String basePackage;

    /**
     * you can choose the logger factory type provide by Vert.x, such as "io.vertx.core.logging.SLF4JLogDelegateFactory".
     */
    @Setter
    @Getter
    private String loggerFactoryClass = "io.vertx.core.logging.SLF4JLogDelegateFactory";

    /**
     * provide a path for configuration that define how logger working.
     */
    @Setter
    @Getter
    private String loggerConfigPath = "/config/log4j.config";

    /**
     * the cluster heart beat interval seconds.
     */
    @Setter
    @Getter
    private String heartbeatInterval = "15";

    /**
     * define cluster port.
     */
    @Setter
    @Getter
    private int clusterPort = 5701;

    /**
     * a ip set of cluster server.
     */
    @Setter
    @Getter
    private List<String> clusterServerIps;

    public WebConfig web() {
        return webConfig;
    }

    public DataSource dataSource() {
        return dataSource;
    }

    public SysConfig() {
        this.rootPath = System.getProperty(Const.TAG_ROOT_PATH);
    }

    /**
     * 加載yaml配置文件
     *
     * @return
     */
    @SneakyThrows
    public static SysConfig loading() {
        String yamlFilePath = System.getProperty(Const.TAG_ROOT_PATH) + Const.PATH_DEFAULT_YAML;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(yamlFilePath), SysConfig.class);
    }

}
