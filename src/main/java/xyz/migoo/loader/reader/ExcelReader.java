/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package xyz.migoo.loader.reader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xyz.migoo.exception.ReaderException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 操作Excel表格的功能类
 *
 * @author xiaomi
 */
public class ExcelReader extends AbstractReader implements Reader {

    private Workbook workbook;
    private Sheet sheet;
    private int rowCount;
    private int columnCount;

    public ExcelReader(File file) throws ReaderException {
        init(file);
    }

    public ExcelReader(String path) throws ReaderException {
        init(path);
    }

    public ExcelReader(File file, String sheet)throws ReaderException {
        init(file);
        parse(sheet);
    }

    private void init(File file) throws ReaderException {
        try {
            if (file.getName().endsWith(ReaderFactory.XLS_SUFFIX)) {
                super.stream(ReaderFactory.XLS_SUFFIX, file);
                workbook = new HSSFWorkbook(inputStream);
            }
            if (file.getName().endsWith(ReaderFactory.XLSX_SUFFIX)) {
                super.stream(ReaderFactory.XLSX_SUFFIX, file);
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(String path) throws ReaderException {
        try {
            if (path.trim().toLowerCase().endsWith(ReaderFactory.XLS_SUFFIX)) {
                super.stream(ReaderFactory.XLS_SUFFIX, path);
                workbook = new HSSFWorkbook(inputStream);
            }
            if (path.trim().toLowerCase().endsWith(ReaderFactory.XLSX_SUFFIX)) {
                super.stream(ReaderFactory.XLSX_SUFFIX, path);
                workbook = new XSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析指定工作表
     *
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
                    if (i > 0) {
                        map.put(title[j].toString(), content[j]);
                    }
                }
                if (i == 0) {
                    title = content;
                    continue;
                }
                jsonArray.add(JSON.toJSON(map));
            }
            return jsonArray;
        } catch (Exception e) {
            throw new ReaderException("please call method ' parse(String sheet) ' set excel sheet name");
        }

    }

    public <T> List<T> toJavaList(Class<T> clazz) throws ReaderException {
        return ((JSONArray) read()).toJavaList(clazz);
    }

    /**
     * 根据HSSFCell类型设置数据
     *
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
