package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author xiaomi
 */
public abstract class AbstractReader {

    protected static final String CLASSPATH = "classpath://";
    private final static Log LOG = new Log(AbstractReader.class);
    protected InputStream inputStream = null;

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param file 文件
     * @param suffix 指定的文件后缀
     */
    protected void validation(File file, String suffix) throws ReaderException {
        if (!file.exists()){
            throw new ReaderException("file not found : " + file.getPath());
        }
        if (!file.getName().endsWith(suffix)){
            throw new ReaderException("this file not a ' " + suffix + " ' file : " + file);
        }
        if (file.length() == 0){
            throw new ReaderException("file length == 0");
        }
    }

    protected boolean isClassPath(String path){
        return path.toLowerCase().startsWith(CLASSPATH);
    }

    protected void stream(String suffix, String path) throws ReaderException {
        try {
            if (!isClassPath(path)){
                stream(suffix, new File(path));
            }else {
                path = path.substring(CLASSPATH.length());
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                inputStream = classLoader.getResourceAsStream(path);
            }
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
            throw new ReaderException("file read exception: " + e.getMessage());
        }
    }

    protected void stream(String suffix, File file) throws ReaderException {
        try {
            validation(file, suffix);
            inputStream = new BufferedInputStream(new FileInputStream(file));
        }catch (Exception e){
            LOG.error(e.getMessage(), e);
            throw new ReaderException("file read exception: " + e.getMessage());
        }
    }
}
