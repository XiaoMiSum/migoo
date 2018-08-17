package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.*;

/**
 * @author xiaomi
 */
public class PropertiesReader extends AbstractReader {

    private final static String SUFFIX = ".properties";
    private final static Log LOG = new Log(PropertiesReader.class);

    private String path;
    private java.util.Properties props;

    public PropertiesReader(String path) {
        this.path = path;
    }

    /**
     * 从项目外的指定目录中读取 properties 文件
     */
    private PropertiesReader read(String path) {
        this.props = new java.util.Properties();
        File file = new File(path);
        if (!super.validation(file, SUFFIX)){
            throw new ReaderException("this file not a ' " + SUFFIX + " ' file : " + file);
        }
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            this.props.load(inputStream);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + file);
        }
        return this;
    }

    /**
     * 从 resources 目录中读取 properties 文件
     * @return
     */
    private PropertiesReader read() {
        this.props = new java.util.Properties();
        InputStream is = this.getClass().getResourceAsStream(this.path);
        try {
            this.props.load(is);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new ReaderException(e.getMessage() + ". file path : " + path);
        }
        return this;
    }

    public String get(String key) {
        if (props != null){
            return props.getProperty(key);
        }
        if(super.isOutsideFile(this.path)){
            this.read(this.path);
        }else {
            this.read();
        }
        return props.getProperty(key);
    }
}