package xyz.migoo.loader.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.report.MiGooLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author xiaomi
 */
public class JSONReader extends AbstractReader implements Reader {

    private JSON json;
    private String path;
    private File file;

    public JSONReader(String path) throws ReaderException {
        super.stream(ReaderFactory.JSON_SUFFIX, path);
    }

    public JSONReader(File file) throws ReaderException {
        super.stream(ReaderFactory.JSON_SUFFIX, file);
    }

    @Override
    public JSON read() throws ReaderException {
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
            MiGooLog.log(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + this.path);
        }
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