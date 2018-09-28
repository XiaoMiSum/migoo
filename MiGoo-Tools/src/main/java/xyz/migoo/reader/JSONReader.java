package xyz.migoo.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.*;

/**
 * @author xiaomi
 */
public class JSONReader extends AbstractReader implements Reader {

    private JSON json;
    private String path;
    private File file;
    private final static Log LOG = new Log(JSONReader.class);

    public JSONReader(String path) {
        this.path = path;
    }

    public JSONReader(File file) {
        this.file = file;
    }

    @Override
    public JSON read(){
        if (path != null){
            super.stream(ReaderFactory.JSON_SUFFIX, path);
        }
        if (file != null){
            super.stream(ReaderFactory.JSON_SUFFIX, file);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            try {
                json = JSON.parseArray(stringBuilder.toString());
            }catch (Exception e){
                json = JSONObject.parseObject(stringBuilder.toString(), Feature.OrderedField);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + this.path);
        }
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
}