package xyz.migoo.utils.reader;

import xyz.migoo.exception.ReaderException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    public final static String JSON_SUFFIX = ".json";
    public final static String YAML_SUFFIX = ".yml";
    public final static String PROS_SUFFIX = ".properties";
    public static final String XLS_SUFFIX = ".xls";
    public static final String XLSX_SUFFIX = ".xlsx";
    private static final List<String> LIST = new ArrayList<>();

    public static Reader getReader(String suffix, String path) throws ReaderException {
        switch (suffix){
            case JSON_SUFFIX:
                return new JSONReader(path);
            case YAML_SUFFIX:
                return new YamlReader(path);
            case PROS_SUFFIX:
                return new PropertiesReader(path);
            case XLS_SUFFIX:
                return new ExcelReader(path);
            case XLSX_SUFFIX:
                return new ExcelReader(path);
            default:
                throw new ReaderException("file reader error");
        }
    }

    public static String suffix(String file){
        String suffix = file.substring(file.lastIndexOf("."));
        if (LIST.contains(suffix)){
            return suffix;
        }
        return null;
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
