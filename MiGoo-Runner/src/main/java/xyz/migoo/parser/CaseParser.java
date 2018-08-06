package xyz.migoo.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.reader.JSONReader;
import xyz.migoo.utils.Variable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class CaseParser implements Parser {

    private final static String SUFFIX = ".json";
    private List<CaseSet> caseSets;

    public CaseParser(){
        caseSets = new ArrayList<>();
    }

    public List<CaseSet> loadCaseSets(String path){
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
            if (file.getName().endsWith(SUFFIX)){
                JSONReader reader = new JSONReader(file);
                JSONArray json = (JSONArray)reader.read();
                this.caseSets(json);
            }
        }
        return this.caseSets;
    }

    private List<CaseSet.Case> cases(JSONArray jsonCases, JSONObject variables){
        List<CaseSet.Case> caseList = new ArrayList(jsonCases.size());
        for (int i = 0; i < jsonCases.size(); i++) {
            String title = jsonCases.getJSONObject(i).getString("title");
            JSONObject body = jsonCases.getJSONObject(i).getJSONObject("body");
            JSONArray validate = jsonCases.getJSONObject(i).getJSONArray("validate");
            JSONObject setUp = jsonCases.getJSONObject(i).getJSONObject("setUp.hook");

            Variable.bindVariable(variables, body);

            CaseSet.Case aCase = new CaseSet.Case();
            aCase.setBody(body);
            aCase.setTitle(title);
            aCase.setValidate(validate);
            aCase.setSetUp(setUp);
            caseList.add(aCase);
        }
        return caseList;
    }

    private List<CaseSet> caseSets(JSONArray jsonArray){
        for (int index = 0; index < jsonArray.size(); index++) {
            JSONObject testCases = jsonArray.getJSONObject(index);
            String name = testCases.getString("name");
            JSONObject jsonConfig = testCases.getJSONObject("config");
            JSONArray jsonCases = testCases.getJSONArray("case");

            CaseSet.Config config = new CaseSet.Config();
            config.setRequest(jsonConfig.getJSONObject("request"));

            JSONObject variables = jsonConfig.getJSONObject("variables");

            CaseSet caseSet = new CaseSet();
            caseSet.setName(name);
            caseSet.setConfig(config);
            caseSet.setCases(this.cases(jsonCases, variables));

            caseSets.add(caseSet);
        }
        return caseSets;
    }
}
