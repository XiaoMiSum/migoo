package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;

import java.io.File;
import java.util.Arrays;
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

    public static Reader getReader(String suffix, File file){
        switch (suffix){
            case JSON_SUFFIX:
                return new JSONReader(file);
            case YAML_SUFFIX:
                return new YamlReader(file);
            case PROS_SUFFIX:
                return new PropertiesReader(file);
            case XLS_SUFFIX:
                return new ExcelReader(file);
            case XLSX_SUFFIX:
                return new ExcelReader(file);
            default:
                throw new ReaderException("file reader error");
        }
    }

    public static String suffix(String file){
        String suffix = "." + file.split("\\.")[1];
        List<String> list = Arrays.asList(new String[]{JSON_SUFFIX, YAML_SUFFIX, PROS_SUFFIX, XLS_SUFFIX, XLSX_SUFFIX});
        if (list.contains(suffix)){
            return suffix;
        }
        return null;
    }
}
