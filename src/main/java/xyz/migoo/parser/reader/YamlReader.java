package xyz.migoo.parser.reader;

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
        super.stream(ReaderFactory.YAML_SUFFIX, path);
    }

    public YamlReader(File file) throws ReaderException {
        super.stream(ReaderFactory.YAML_SUFFIX, file);
    }

    @Override
    public JSON read() throws ReaderException {
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
