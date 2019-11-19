package xyz.migoo.loader.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;
import xyz.migoo.exception.ReaderException;

import java.io.File;

/**
 * @author xiaomi
 * @date 2018/09/28 14:25:22
 */
public class YamlReader extends AbstractReader implements Reader{

    private JSON json;

    public YamlReader(String path) throws ReaderException {
        stream(ReaderFactory.YAML_SUFFIX, path);
    }

    public YamlReader(File file) throws ReaderException {
        stream(ReaderFactory.YAML_SUFFIX, file);
    }

    @Override
    public JSON read(){
        Yaml yaml = new Yaml();
        Object object = yaml.load(inputStream);
        json = (JSON) JSON.toJSON(object);
        return json;
    }

    @Override
    public String get(String key) {
        if (json == null){
            read();
        }
        if (json instanceof JSONObject){
            return ((JSONObject) json).getString(key);
        }
        return null;
    }

    @Override
    public String toString(){
        if (json == null){
            read();
        }
        return json.toString();
    }
}
