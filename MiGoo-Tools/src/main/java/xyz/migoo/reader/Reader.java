package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;

import java.io.File;

/**
 * @author xiaomi
 */
public interface Reader {

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param file 文件
     * @param suffix 指定的文件后缀
     * @return
     */
    default boolean validation(File file, String suffix){
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
}
