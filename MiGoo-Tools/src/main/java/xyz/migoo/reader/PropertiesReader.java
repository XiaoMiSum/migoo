package xyz.migoo.reader;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.*;
import java.util.Properties;

/**
 * @author xiaomi
 */
public class PropertiesReader extends AbstractReader implements Reader{

    private final static Log LOG = new Log(PropertiesReader.class);

    private String path;
    private File file;
    private JSONObject json;

    public PropertiesReader(String path) {
        this.path = path;
    }

    public PropertiesReader(File file) {
        this.file = file;
    }

    @Override
    public JSONObject read() throws ReaderException {
        if (path != null){
            super.stream(ReaderFactory.PROS_SUFFIX, path);
        }
        if (file != null){
            super.stream(ReaderFactory.PROS_SUFFIX, file);
        }
        Properties props = new Properties();
        try {
            props.load(inputStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
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

    public InputStream getInputStream(){
        try {
            super.stream(ReaderFactory.PROS_SUFFIX, path);
        } catch (ReaderException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
