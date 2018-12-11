package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.reader.Reader;
import xyz.migoo.reader.ReaderFactory;
import xyz.migoo.utils.Variable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class CaseParser{

    private List<JSONObject> caseSets;

    public CaseParser(){
        caseSets = new ArrayList<>();
    }

    public List<JSONObject> loadCaseSets(String pathOrSet) throws ReaderException {
        try {
            loadCaseSetBySet(JSON.parse(pathOrSet));
        }catch (Exception e){
            loadCaseSetsByPath(pathOrSet);
        }
        return this.caseSets;
    }

    private void loadCaseSetsByPath(String path) throws ReaderException {
        File file = new File(path);
        if (file.isDirectory()){
            String[] fList = file.list();
            for (String f : fList){
                if (!path.endsWith(File.separator)){
                    path = path + File.separator;
                }
                this.loadCaseSets(path + f);
            }
        }else {
            this.loadCaseSetsByFile(file);
        }
    }

    private void loadCaseSetsByFile(File file) throws ReaderException {
        String suffix = ReaderFactory.suffix(file.getName());
        if (suffix != null){
            Reader reader = ReaderFactory.getReader(suffix, file);
            JSON json = reader.read();
            if (json instanceof JSONArray) {
                this.caseSets((JSONArray) json);
            }else{
                JSONArray jsonArray = new JSONArray(1);
                jsonArray.add(json);
                this.caseSets(jsonArray);
            }
        }
    }

    private void loadCaseSetBySet(Object set){
        JSONArray caseArray;
        if (set instanceof JSONObject){
            caseArray = new JSONArray(1);
            caseArray.add(set);
        }else {
            caseArray = (JSONArray) set;
        }
        this.caseSets(caseArray);
    }

    private List<JSONObject> caseSets(JSONArray jsonArray){
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject testCases = jsonArray.getJSONObject(index);
            JSONObject variables = testCases.getJSONObject(Dict.CONFIG).getJSONObject(Dict.CONFIG_VARIABLES);
            Variable.evalVariable(variables);
            Variable.bindVariable(variables, variables);
            JSONArray caseArray = testCases.getJSONArray(Dict.CASE);
            for (int i = 0; i < caseArray.size(); i++) {
                JSONObject jsonCase = caseArray.getJSONObject(i);
                JSONObject setUp = jsonCase.getJSONObject(Dict.CASE_SETUP);
                Variable.evalVariable(setUp);
                Variable.bindVariable(setUp, setUp);
            }
            testCases.put(Dict.CASE, caseArray);
            caseSets.add(testCases);
        }
        return caseSets;
    }
}