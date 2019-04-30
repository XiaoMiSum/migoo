package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.CaseKeys;
import xyz.migoo.exception.InvokeException;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.utils.StringUtil;
import xyz.migoo.utils.Variable;
import xyz.migoo.utils.reader.Reader;
import xyz.migoo.utils.reader.ReaderFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class CaseParser {

    private List<JSONObject> caseSets;

    public CaseParser() {
        caseSets = new ArrayList<>();
    }

    /**
     * 如果 解析 json 没有抛出异常，说明传入的参数不是文件
     * 如果 解析 json 抛出了异常，说明传入的参数是文件
     *
     * @param caseOrPath 测试用例 或者 测试用例文件或所在目录
     * @param vars       全局变量
     * @return List<JSONObject> json对象的测试用例列表
     */
    public List<JSONObject> loadCaseSets(String caseOrPath, JSONObject vars) throws InvokeException, ReaderException {

        try {
            Object caseSet = JSON.parse(caseOrPath);
            this.loadCaseSetsByCaseSet(caseSet, vars);
        } catch (Exception e) {
            this.loadCaseSetsByPath(caseOrPath, vars);
        }
        return this.caseSets;
    }

    public static JSONObject loadVariables(String vars) {
        if (StringUtil.isNotBlank(vars)) {
            try {
                return JSONObject.parseObject(vars);
            } catch (Exception e) {
                try {
                    File file = new File(vars);
                    if (!file.isDirectory()) {
                        String suffix = ReaderFactory.suffix(vars);
                        if (suffix != null) {
                            Reader reader = ReaderFactory.getReader(suffix, vars);
                            return (JSONObject) reader.read();
                        }
                    }
                } catch (ReaderException re) {
                    return null;
                }
            }
        }
        return null;
    }

    private void loadCaseSetsByPath(String path, JSONObject vars) throws InvokeException, ReaderException {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] fList = file.list();
            assert fList != null;
            for (String f : fList) {
                if (StringUtil.contains(f, "vars.")) {
                    continue;
                }
                StringBuilder pathBuilder = new StringBuilder(path);
                if (!pathBuilder.toString().endsWith(File.separator)) {
                    pathBuilder.append(File.separator);
                }
                this.loadCaseSetsByPath(pathBuilder + f, vars);
            }
        } else {
            this.loadCaseSetsByFile(path, vars);
        }
    }

    private void loadCaseSetsByFile(String path, JSONObject vars) throws InvokeException, ReaderException {
        String suffix = ReaderFactory.suffix(path);
        if (suffix != null) {
            xyz.migoo.utils.reader.Reader reader = ReaderFactory.getReader(suffix, path);
            JSON json = reader.read();
            if (json instanceof JSONArray) {
                this.caseSets((JSONArray) json, vars);
            } else {
                JSONArray jsonArray = new JSONArray(1);
                jsonArray.add(json);
                this.caseSets(jsonArray, vars);
            }
        }
    }

    private void loadCaseSetsByCaseSet(Object caseSet, JSONObject vars) throws InvokeException {
        JSONArray caseArray;
        if (caseSet instanceof JSONObject) {
            caseArray = new JSONArray();
            caseArray.add(caseSet);
        } else {
            caseArray = (JSONArray) caseSet;
        }
        this.caseSets(caseArray, vars);
    }

    private List<JSONObject> caseSets(JSONArray jsonArray, JSONObject vars) throws InvokeException {
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject testCases = jsonArray.getJSONObject(index);
            JSONObject config = testCases.getJSONObject(CaseKeys.CONFIG);
            JSONObject variables = config.getJSONObject(CaseKeys.CONFIG_VARIABLES);
            bindVariables(vars, variables,null, testCases);
            JSONArray caseArray = testCases.getJSONArray(CaseKeys.CASE);
            for (int i = 0; i < caseArray.size(); i++) {
                Object obj = caseArray.get(i);
                JSONObject caseJson;
                if (obj instanceof String){
                    System.out.println(obj);
                    caseJson = JSONObject.parseObject(obj.toString());
                }else {
                    caseJson = (JSONObject) obj;
                }
                JSONObject caseVars = caseJson.getJSONObject(CaseKeys.CASE_VARIABLES);
                bindVariables(vars, variables, caseVars, caseJson);
            }
            testCases.put(CaseKeys.CASE, caseArray);
            caseSets.add(testCases);
        }
        return caseSets;
    }

    private void bindVariables(JSONObject overall, JSONObject variables,JSONObject caseVars , JSONObject caseJson) throws InvokeException {
        // 1. 先将 variables 里面的变量执行绑定 或 计算
        Variable.loopBindVariables(variables, variables);
        Variable.bindVariable(overall, caseJson);
        // 1. 使用 variables 替换 case.setUp 中的变量
        Variable.bindVariable(variables, caseVars);
        // 2. 使用 setUp 替换 case.setUp 中的变量
        Variable.loopBindVariables(caseVars, caseVars);
        // 3. 使用 setUp 替换 case 中的变量
        Variable.loopBindVariables(caseVars, caseJson);
    }
}
