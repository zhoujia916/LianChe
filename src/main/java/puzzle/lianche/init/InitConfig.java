package puzzle.lianche.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import puzzle.lianche.entity.SystemConfig;
import puzzle.lianche.service.ISystemConfigService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component("initConfig")
public class InitConfig implements InitializingBean {
    public Map<String, String> configs;
    public Map<String, String> properties;

    private Logger logger = LoggerFactory.getLogger("Config");

    @Autowired
    private ISystemConfigService systemConfigService;

    public Map<String, String> getProperties(){
        loadSystemProperty();
        return properties;
    }

    public Map<String, String> getConfigs(){
        loadSystemConfig();
        return configs;
    }

    public String getProperty(String name){
        return properties.get(name);
    }

    public String getConfig(String name){
        return configs.get(name);
    }

    public void loadSystemProperty(){
        if(properties == null){
            properties = new HashMap<String, String>();
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath() + "system.properties";
            Properties props = new Properties();
            InputStream is = null;
            try {
                is = new FileInputStream(new File(path));
                props.load(is);
                // 读取配置文件，配置项
                Set<String> keys = props.stringPropertyNames();
                if(keys != null && keys.size() > 0){
                    for(String key : keys){
                        properties.put(key, props.getProperty(key));
                    }
                }
            } catch (IOException e) {
                logger.error("loadSystemProperty error:" + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (null != is)
                        is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
    }

    public void loadSystemConfig(){
        if(configs == null){
            configs = new HashMap<String, String>();
            try {
                List<SystemConfig> list = systemConfigService.queryList(null);
                if(list != null && list.size() > 0){
                    for(SystemConfig item : list){
                        configs.put(item.getCode(), item.getValue());
                    }
                }
            }
            catch (Exception e){
                logger.error("loadSystemConfig error:" + e.getMessage());
            }

        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        loadSystemProperty();

        loadSystemConfig();
    }
}
