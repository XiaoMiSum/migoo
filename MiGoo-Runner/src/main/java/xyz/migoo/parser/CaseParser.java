package xyz.migoo.parser;

import com.alibaba.fastjson.JSONArray;
import xyz.migoo.reader.JSONReader;

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

    private List<CaseSet.Case> cases(JSONArray jsonArray, int index){
        JSONArray caseJsonArray = jsonArray.getJSONObject(index).getJSONArray("case");
        List<CaseSet.Case> caseList = new ArrayList(caseJsonArray.size());
        for (int i = 0; i < caseJsonArray.size(); i++) {
            CaseSet.Case aCase = new CaseSet.Case();
            aCase.setBody(caseJsonArray.getJSONObject(i).getJSONObject("body"));
            aCase.setTitle(caseJsonArray.getJSONObject(i).getString("title"));
            aCase.setValidate(caseJsonArray.getJSONObject(i).getJSONArray("validate"));
            caseList.add(aCase);
        }
        return caseList;
    }

    private List<CaseSet> caseSets(JSONArray jsonArray){
        for (int index = 0; index < jsonArray.size(); index++) {
            String name = jsonArray.getJSONObject(index).getString("name");

            CaseSet.Config config = new CaseSet.Config();
            config.setRequest(jsonArray.getJSONObject(index).getJSONObject("request"));

            CaseSet caseSet = new CaseSet();
            caseSet.setName(name);
            caseSet.setConfig(config);
            caseSet.setCases(this.cases(jsonArray, index));

            caseSets.add(caseSet);
        }
        return caseSets;
    }
}
