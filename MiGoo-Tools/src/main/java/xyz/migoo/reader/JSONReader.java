package xyz.migoo.reader;

import com.alibaba.fastjson.JSON;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.io.*;

/**
 * @author xiaomi
 */
public class JSONReader implements Reader {

    private final static String SUFFIX = ".json";
    private final static Log LOG = new Log(JSONReader.class);
    private JSON json;
    private String path;
    File file;
    public JSONReader(String path) {
        this.path = path;
    }
    public JSONReader(File file) {
        this.file = file;
    }

    public JSON read(){
        InputStream inputStream;
        try {
            if(StringUtil.isNotBlank(this.path)){
                if (this.path.startsWith(File.separator)){
                    file = new File(path);
                    inputStream = this.file();
                }else {
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    inputStream = classloader.getResourceAsStream(this.path);
                }
            }else {
                inputStream = this.file();
            }
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
            throw new ReaderException("file read exception: " + e.getMessage());
        }
        return this.read(inputStream);
    }

    private InputStream file() throws FileNotFoundException {
        if (!Reader.super.validation(file, SUFFIX)) {
            throw new ReaderException("this file not a ' " + SUFFIX + " ' file : " + file);
        }
        return new FileInputStream(file);
    }

    private JSON read(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            try {
                json = JSON.parseArray(stringBuilder.toString());
            }catch (Exception e){
                json = JSON.parseObject(stringBuilder.toString());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + this.path);
        }
        return json;
    }
}