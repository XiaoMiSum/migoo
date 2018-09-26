package xyz.migoo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.config.Dict;
import xyz.migoo.reader.JSONReader;
import xyz.migoo.utils.Variable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class CaseParser{

    private final static String SUFFIX = ".json";
    private List<JSONObject> caseSets;

    public CaseParser(){
        caseSets = new ArrayList<>();
    }

    public List<JSONObject> loadCaseSets(String pathOrSet){
        try {
            loadCaseSetBySet(JSON.parse(pathOrSet));
        }catch (Exception e){
            loadCaseSetsByPath(pathOrSet);
        }
        return this.caseSets;
    }

    private void loadCaseSetsByPath(String path){
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

    private void loadCaseSetsByFile(File file){
        if (file.getName().endsWith(SUFFIX)){
            JSONReader reader = new JSONReader(file);
            JSON json = reader.read();
            if (json instanceof JSONArray) {
                this.caseSets((JSONArray) json);
            }else{
                JSONArray jsonArray = new JSONArray(1);
                jsonArray.add(json);
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