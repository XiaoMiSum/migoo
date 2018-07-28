package xyz.migoo.parser;

import xyz.migoo.exception.ParserException;
import xyz.migoo.utils.StringUtil;

import java.io.File;

/**
 * @author xiaomi
 */
public interface Parser {

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param path
     * @param suffix
     * @return
     */
    default File validation(String path, String suffix){
        if (StringUtil.isBlank(path)) {
            throw new ParserException("file path can not be null or empty");
        }
        if (!path.endsWith(suffix)){
            throw new ParserException("not a '" + suffix + "' file");
        }
        File file = new File(path);
        if (!file.exists()){
            throw new ParserException("file not exists");
        }
        if (file.isDirectory()) {
            throw new ParserException("file can not be directory");
        }
        if (file.length() == 0){
            throw new ParserException("file length == 0");
        }
        return file;
    }
}
