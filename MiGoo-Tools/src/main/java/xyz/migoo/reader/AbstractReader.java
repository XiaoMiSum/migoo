package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;

import java.io.File;

/**
 * @author xiaomi
 */
public abstract class AbstractReader {

    private static final String DOT = ".";

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param file 文件
     * @param suffix 指定的文件后缀
     * @return
     */
    protected boolean validation(File file, String suffix){
        if (!file.exists()){
            throw new ReaderException("file not found : " + file.getPath());
        }
        if (!file.getName().endsWith(suffix)){
            return false;
        }
        if (file.length() == 0){
            throw new ReaderException("file length == 0");
        }
        return true;
    }

    protected boolean isOutsideFile(String path){
        return path.startsWith(File.separator) || path.startsWith(DOT);
    }
}