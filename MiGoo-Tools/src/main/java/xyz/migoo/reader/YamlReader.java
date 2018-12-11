package xyz.migoo.reader;

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

    private String path;
    private File file;
    private JSON json;

    public YamlReader(String path){
        this.path = path;
    }

    public YamlReader(File file){
        this.file = file;
    }

    @Override
    public JSON read() throws ReaderException {
        if (path != null){
            super.stream(ReaderFactory.YAML_SUFFIX, path);
        }
        if (file != null){
            super.stream(ReaderFactory.YAML_SUFFIX, file);
        }
        Yaml yaml = new Yaml();
        Object object = yaml.load(inputStream);
        json = (JSON) JSON.toJSON(object);
        return json;
    }

    @Override
    public String get(String key) throws ReaderException {
        if (json == null){
            read();
        }
        if (json instanceof JSONObject){
            return ((JSONObject) json).getString(key);
        }
        return null;
    }
}
