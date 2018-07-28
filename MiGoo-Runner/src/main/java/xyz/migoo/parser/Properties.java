package xyz.migoo.parser;

import org.slf4j.LoggerFactory;
import xyz.migoo.exception.ParserException;
import xyz.migoo.utils.Log;

import java.io.*;

/**
 * @author xiaomi
 */
public class Properties implements Parser {

    private static Log log = new Log(LoggerFactory.getLogger(Properties.class));

    private String file;
    private java.util.Properties props;

    public Properties(String file) {
        this.file = file;
    }

    private Properties read(String file) {
        this.props = new java.util.Properties();
        try {
            Parser.super.validation(file,".properties");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new ParserException(e.getMessage() + ". file path : " + file);
        }

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            this.props.load(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ParserException(e.getMessage() + ". file path : " + file);
        }
        return this;
    }

    private Properties read() {
        this.props = new java.util.Properties();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(this.file);
        try {
            this.props.load(is);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ParserException(e.getMessage() + ". file path : " + file);
        }
        return this;
    }


    public String getValue(String key) {
        if(this.file.startsWith("/")){
            this.read(this.file);
        }else {
            this.read();
        }
        return props.getProperty(key);
    }
}