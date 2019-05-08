package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.reader.Reader;
import xyz.migoo.utils.reader.ReaderFactory;

import java.io.File;

/**
 * @author xiaomi
 */
public class CaseParser {

    private JSONObject caseSets;

    public CaseParser() {
    }

    /**
     * 如果 解析 json 没有抛出异常，说明传入的参数不是文件
     * 如果 解析 json 抛出了异常，说明传入的参数是文件
     *
     * @param caseOrPath 测试用例 或者 测试用例文件或所在目录
     * @return List<JSONObject> json对象的测试用例列表
     */
    public JSONObject loadCaseSets(String caseOrPath) throws ReaderException {
        try {
            this.caseSets = JSONObject.parseObject(caseOrPath);
        } catch (Exception e) {
            this.loadCaseSetsByPath(caseOrPath);
        }
        return this.caseSets;
    }

    public static JSONObject loadVariables(String vars) throws ReaderException {
        if (StringUtil.isNotBlank(vars)) {
            try {
                return JSONObject.parseObject(vars);
            } catch (Exception e) {
                File file = new File(vars);
                if (!file.isDirectory()) {
                    String suffix = ReaderFactory.suffix(vars);
                    if (suffix != null) {
                        Reader reader = ReaderFactory.getReader(suffix, vars);
                        return (JSONObject) reader.read();
                    }
                }
            }
        }
        return null;
    }

    private void loadCaseSetsByPath(String path) throws ReaderException {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] fList = file.list();
            assert fList != null;
            for (String f : fList) {
                if (StringUtil.contains(f, "vars.")
                    ||f.startsWith(".")) {
                    continue;
                }
                if (!path.endsWith("/")) {
                    path = path + "/";
                }
                this.loadCaseSetsByPath(path + f);
            }
        } else {
            this.loadCaseSetsByFile(path);
        }
    }

    private void loadCaseSetsByFile(String path) throws ReaderException {
        String suffix = ReaderFactory.suffix(path);
        if (suffix != null) {
            Reader reader = ReaderFactory.getReader(suffix, path);
            JSON json = reader.read();
            this.caseSets = (JSONObject)json;
        }
    }
}
