package org.mendoraX.initData.vo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.mendoraX.initData.Const;

import java.io.File;

/**
 * @auther menfre
 * @date 2018/8/11
 * version: 1.0
 * desc:
 */
public class WebConfig {
    /**
     * http listening at which port.
     */
    @Setter
    @Getter
    private int httpPort = 8080;

    /**
     * http request body limit size.
     */
    @Setter
    @Getter
    private long httpBodyLimit = 2048576L;

    /**
     * the password for jwt key.
     */
    @Setter
    @Getter
    private String jwtKeyPasswd;

    /**
     * jwt token expires time at minutes.
     */
    @Setter
    @Getter
    private int jwtExpiresMinutes = 30;

    /**
     * jwt token issuer.
     */
    @Setter
    @Getter
    private String jwtIssuer;


    /**
     * 加載yaml配置文件
     *
     * @return
     */
    @SneakyThrows
    public static WebConfig loading() {
        String yamlFilePath = System.getProperty(Const.TAG_ROOT_PATH) + Const.PATH_WEB_YAML;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(yamlFilePath), WebConfig.class);
    }
}
