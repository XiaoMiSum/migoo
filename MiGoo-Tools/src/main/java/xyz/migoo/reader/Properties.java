package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.*;

/**
 * @author xiaomi
 */
public class Properties implements Reader {

    private final static String SUFFIX = ".properties";
    private final static Log LOG = new Log(Properties.class);

    private String path;
    private java.util.Properties props;

    public Properties(String path) {
        this.path = path;
    }

    private Properties read(String path) {
        this.props = new java.util.Properties();
        File file = new File(path);
        if (!Reader.super.validation(file, SUFFIX)){
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

    private Properties read() {
        this.props = new java.util.Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(this.path);
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
        if(this.path.startsWith(File.separator)){
            this.read(this.path);
        }else {
            this.read();
        }
        return props.getProperty(key);
    }
}