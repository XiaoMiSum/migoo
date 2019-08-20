package xyz.migoo.parser.reader;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.MiGooLog;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author xiaomi
 */
public class PropertiesReader extends AbstractReader implements Reader {


    private JSONObject json;

    public PropertiesReader(String path) throws ReaderException {
        super.stream(ReaderFactory.PROS_SUFFIX, path);
    }

    public PropertiesReader(File file) throws ReaderException {
        super.stream(ReaderFactory.PROS_SUFFIX, file);
    }

    @Override
    public JSONObject read() throws ReaderException {
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            MiGooLog.log(e.getMessage(), e);
            throw new ReaderException("file read exception: " + e.getMessage());
        }
        json = (JSONObject)JSONObject.toJSON(props);
        return json;
    }

    @Override
    public String get(String key) throws ReaderException {
        if (json == null){
            read();
        }
        return json.getString(key);
    }
}
