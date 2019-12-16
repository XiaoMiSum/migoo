package xyz.migoo.loader;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.framework.entity.MiGooCase;
import xyz.migoo.loader.reader.AbstractReader;
import xyz.migoo.loader.reader.ReaderFactory;
import xyz.migoo.report.MiGooLog;
import xyz.migoo.utils.StringUtil;

import java.io.File;

/**
 * @author xiaomi
 */
public class CaseLoader {

    private CaseLoader() {
    }

    public static MiGooCase loadMiGooCase(String path) throws ReaderException {
        MiGooLog.log("load case sets begin: {}", path);
        AbstractReader reader = (AbstractReader)ReaderFactory.getReader(new File(path));
        MiGooCase cases = JSONObject.parseObject(reader.toString(), MiGooCase.class, Feature.OrderedField);
        MiGooLog.log("load case sets end");
        return cases;
    }

    public static JSONObject loadEnv(String env) throws ReaderException {
        MiGooLog.log("load env begin: {}", env);
        JSONObject json = null;
        if (!StringUtil.isEmpty(env)) {
            try {
                json = JSONObject.parseObject(env);
            } catch (Exception e) {
                File file = new File(env);
                if (!file.isDirectory()) {
                    json = (JSONObject) ReaderFactory.getReader(file).read();
                }
            }
        }
        MiGooLog.log("load env end");
        return json;
    }
}
