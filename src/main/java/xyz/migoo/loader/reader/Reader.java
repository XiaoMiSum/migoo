package xyz.migoo.loader.reader;

import com.alibaba.fastjson.JSON;
import xyz.migoo.exception.ReaderException;

/**
 * @author xiaomi
 */
public interface Reader {

    /**
     * 读文件
     * @throws ReaderException
     * @return 返回 JSON 对象
     */
    JSON read() throws ReaderException;

    /**
     * 从 JSON 对象中取出 key 对应的 value
     * @param key 指定 key
     * @throws ReaderException
     * @return 字符串 或 null
     */
    String get(String key) throws ReaderException;
}
