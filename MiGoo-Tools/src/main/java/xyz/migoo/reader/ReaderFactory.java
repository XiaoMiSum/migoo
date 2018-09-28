package xyz.migoo.reader;

import xyz.migoo.exception.ReaderException;

import java.io.File;

/**
 * @author xiaomi
 */
public class ReaderFactory {

    public final static String JSON_SUFFIX = ".json";
    public final static String YAML_SUFFIX = ".yml";
    public final static String PROS_SUFFIX = ".properties";
    public static final String XLS_SUFFIX = ".xls";
    public static final String XLSX_SUFFIX = ".xlsx";

    public static Reader getReader(String fileSuffix, File file){
        switch (fileSuffix){
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
        if(file.endsWith(JSON_SUFFIX)){
            return JSON_SUFFIX;
        }
        if (file.endsWith(YAML_SUFFIX)){
            return YAML_SUFFIX;
        }
        if (file.endsWith(PROS_SUFFIX)){
            return PROS_SUFFIX;
        }
        if (file.endsWith(XLS_SUFFIX)){
            return XLS_SUFFIX;
        }
        if (file.endsWith(XLSX_SUFFIX)){
            return XLSX_SUFFIX;
        }
        return null;
    }
}
