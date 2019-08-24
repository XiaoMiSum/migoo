package xyz.migoo.loader.reader;

import xyz.migoo.exception.ReaderException;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    final static String JSON_SUFFIX = ".json";
    final static String YAML_SUFFIX = ".yml";
    final static String PROS_SUFFIX = ".properties";
    static final String XLS_SUFFIX = ".xls";
    static final String XLSX_SUFFIX = ".xlsx";
    private static final List<String> LIST = new ArrayList<>();

    public static Reader getReader(File path) throws ReaderException {
        switch (suffix(path.getName())){
            case JSON_SUFFIX:
                return new JSONReader(path);
            case YAML_SUFFIX:
                return new YamlReader(path);
            case PROS_SUFFIX:
                return new PropertiesReader(path);
            case XLS_SUFFIX:
            case XLSX_SUFFIX:
                return new ExcelReader(path);
            default:
                throw new ReaderException("file reader error");
        }
    }

    private static String suffix(String file) throws ReaderException {
        String suffix = file.substring(file.lastIndexOf("."));
        if (LIST.contains(suffix)){
            return suffix;
        }
        throw new ReaderException("file reader error");
    }

    static{
        Field[] fields = ReaderFactory.class.getDeclaredFields();
        for (Field field:fields){
            try {
                if ("LIST".equals(field.getName())){
                    continue;
                }
                LIST.add(field.get(ReaderFactory.class).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
