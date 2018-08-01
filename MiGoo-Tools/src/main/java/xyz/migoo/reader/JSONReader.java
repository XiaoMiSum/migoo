package xyz.migoo.reader;

import com.alibaba.fastjson.JSON;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * @author xiaomi
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class JSONReader implements Reader {

    private final static String SUFFIX = ".json";
    private final static Log LOG = new Log(JSONReader.class);
    private JSON json;
    private File file;

    public JSONReader(String path) {
        this(new File(path));
    }

    public JSONReader(File file) {
        this.file = file;
    }

    public JSON read() {
        if (!Reader.super.validation(file, SUFFIX)) {
            throw new ReaderException("this file not a ' " + SUFFIX + " ' file : " + file);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
            throw new ReaderException(e.getMessage() + ". file path : " + file);
        }
        return json;
    }
}
