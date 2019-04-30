package xyz.migoo.utils.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.Log;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作Excel表格的功能类
 * @author xiaomi
 */
public class ExcelReader extends AbstractReader implements Reader {

    private Log log = new Log(ExcelReader.class);
    private Workbook workbook;
    private Sheet sheet;
    private int rowCount;
    private int columnCount;

    public ExcelReader(File file) {
        init(file);
    }

    public ExcelReader(String path) {
        init(path);
    }

    public ExcelReader(File file, String sheet) {
        init(file);
        parse(sheet);
    }

    private void init (File file){
        try {
            if (file.getName().endsWith(ReaderFactory.XLS_SUFFIX)) {
                super.stream(ReaderFactory.XLS_SUFFIX, file);
                workbook = new HSSFWorkbook(inputStream);
            }
            if (file.getName().endsWith(ReaderFactory.XLSX_SUFFIX)){
                super.stream(ReaderFactory.XLSX_SUFFIX, file);
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            log.error("reader file error", e);
        }
    }

    private void init (String path) {
        try {
            if (path.trim().toLowerCase().endsWith(ReaderFactory.XLS_SUFFIX)) {
                super.stream(ReaderFactory.XLS_SUFFIX, path);
                workbook = new HSSFWorkbook(inputStream);
            }
            if (path.trim().toLowerCase().endsWith(ReaderFactory.XLSX_SUFFIX)) {
                super.stream(ReaderFactory.XLSX_SUFFIX, path);
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (Exception e) {
            log.error("reader file error", e);
        }
    }

    /**
     * 解析指定工作表
     * @param sheet
     * @return
     */
    public ExcelReader parse(String sheet) {
        this.sheet = workbook.getSheet(sheet.trim());
        this.rowCount = this.sheet.getLastRowNum();
        this.columnCount = this.sheet.getRow(0).getPhysicalNumberOfCells();
        return this;
    }

    public String[] sheets() {
        String[] sheets = new String[workbook.getNumberOfSheets()];
        for (int i = 0; i < sheets.length; i++) {
            sheets[i] = workbook.getSheetAt(i).getSheetName();
        }
        return sheets;
    }

    @Override
    public JSON read() throws ReaderException {
        try {
            JSONArray jsonArray = new JSONArray(rowCount);
            Object[] title = null;
            for (int i = 0; i <= rowCount; i++) {
                String[] content = new String[columnCount];
                Map<String, String> map = new HashMap<>(content.length);
                Row row = sheet.getRow(i);
                for (int j = 0; j < columnCount; j++) {
                    content[j] = this.getValue(row.getCell(j)).trim();
                    if (i > 0){
                        map.put(title[j].toString(), content[j]);
                    }
                }
                if (i == 0){
                    title = content;
                    continue;
                }
                jsonArray.add(JSON.toJSON(map));
            }
            return jsonArray;
        }catch (Exception e){
            throw new ReaderException("please call method ' parse(String sheet) ' set excel sheet name");
        }

    }

    public <T> List<T>  toJavaList(Class<T> clazz) throws ReaderException {
        return ((JSONArray)read()).toJavaList(clazz);
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getValue(Cell cell) {
        if (null == cell) {
            return "";
        }
        switch (cell.getCellTypeEnum()) {
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return cell.getCellFormula();
                }
            }
            case FORMULA: {
                CreationHelper crateHelper = workbook.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                return this.getValue(evaluator.evaluateInCell(cell));
            }
            case STRING: {
                return cell.getRichStringCellValue().getString();
            }
            case BOOLEAN: {
                return String.valueOf(cell.getBooleanCellValue());
            }
            default:
                return "";
        }
    }

    @Override
    public String get(String key) {
        return null;
    }
}