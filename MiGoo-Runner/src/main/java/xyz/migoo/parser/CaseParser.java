package xyz.migoo.parser;

import com.alibaba.fastjson.JSONArray;
import xyz.migoo.exception.ParserException;
import xyz.migoo.utils.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaomi
 */
public class CaseParser implements Parser {

    private final static String SUFFIX = ".json";
    private static Log log = new Log(CaseParser.class);
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
            if (Parser.super.validation(file, SUFFIX)){
                this.loadCaseByFile(path);
            }
        }
        return this.caseSets;
    }

    private void loadCaseByFile(String file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            this.caseSets(JSONArray.parseArray(stringBuilder.toString()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ParserException( e.getMessage() + ". file path : " + file);
        }
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
