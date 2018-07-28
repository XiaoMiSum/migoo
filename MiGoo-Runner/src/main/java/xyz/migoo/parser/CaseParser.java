package xyz.migoo.parser;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.LoggerFactory;
import xyz.migoo.exception.ParserException;
import xyz.migoo.utils.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiaomi
 */
public class CaseParser implements Parser {

    private static Log log = new Log(LoggerFactory.getLogger(CaseParser.class));
    private JSONArray jsonArray;
    private File file;

    public CaseParser(String file) {
        this.read(file);
    }

    private void read(String path) {
        try {
            this.file = Parser.super.validation(path, ".json");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ParserException(e.getMessage() + ". file path : " + path);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            this.jsonArray = JSONArray.parseArray(stringBuilder.toString());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ParserException( e.getMessage() + ". file path : " + path);
        }
    }

    private String name(int index){
        return jsonArray.getJSONObject(index).getString("name");
    }

    private Map request(int index){
        return jsonArray.getJSONObject(index).getObject("request",Map.class);
    }

    private List cases(int index){
        JSONArray jsonArray = this.jsonArray.getJSONObject(index).getJSONArray("case");
        List list = new ArrayList(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            Map map =  jsonArray.getObject(i,Map.class);
            list.add(map);
        }
        return list;
    }

    public List<Map<String, Object>> caseSets(){
        List<Map<String, Object>> caseSets = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            List cases = this.cases(i);
            String name = this.name(i);
            Map request = this.request(i);

            Map<String, Object> config = new HashMap<>(2);
            config.put("path", file.getPath());
            config.put("request", request);

            Map<String, Object> map = new HashMap<>(cases.size());
            map.put("name", name);
            map.put("config", config);
            map.put("cases", cases);

            caseSets.add(map);
        }
        return caseSets;
    }
}
