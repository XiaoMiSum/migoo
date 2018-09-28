package xyz.migoo.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/09/28 14:25:22
 */
public class YamlReader extends AbstractReader implements Reader{

    private String path;
    private File file;
    private JSONObject json;

    public YamlReader(String path){
        this.path = path;
    }

    public YamlReader(File file){
        this.file = file;
    }

    @Override
    public JSON read(){
        if (path != null){
            super.stream(ReaderFactory.PROS_SUFFIX, path);
        }
        if (file != null){
            super.stream(ReaderFactory.PROS_SUFFIX, file);
        }
        Yaml yaml = new Yaml();
        Object object = yaml.load(inputStream);
        json = (JSONObject)JSONObject.toJSON(object);
        return json;
    }

    @Override
    public String get(String key) {
        if (json == null){
            read();
        }
        return json.getString(key);
    }
}
