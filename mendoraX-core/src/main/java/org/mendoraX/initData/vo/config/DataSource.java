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
public class DataSource {
    /**
     * connect info for postgre or mysql.
     */
    @Setter
    @Getter
    private String dbPostgreUri;

    /**
     * connect info for mongodb.
     */
    @Setter
    @Getter
    private String dbMongoUri;

    /**
     * 加載yaml配置文件
     *
     * @return
     */
    @SneakyThrows
    public static DataSource loading() {
        String yamlFilePath = System.getProperty(Const.TAG_ROOT_PATH) + Const.PATH_DATA_SOURCE_YAML;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(new File(yamlFilePath), DataSource.class);
    }

}
