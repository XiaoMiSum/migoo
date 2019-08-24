package xyz.migoo.loader;

import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
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

    /**
     * 如果 解析 json 没有抛出异常，说明传入的参数不是文件
     * 如果 解析 json 抛出了异常，说明传入的参数是文件
     *
     * @param caseOrPath 测试用例 或者 测试用例文件或所在目录
     * @return caseSets json对象的测试用例列表
     */
    public static JSONObject loadCaseSet(String caseOrPath) throws ReaderException {
        MiGooLog.log("load case sets begin: {}", caseOrPath);
        JSONObject caseSet;
        try {
            caseSet = JSONObject.parseObject(caseOrPath);
        } catch (Exception e) {
            caseSet = (JSONObject) ReaderFactory.getReader(new File(caseOrPath)).read();
        }
        MiGooLog.log("load case sets end");
        return caseSet;
    }

    public static JSONObject loadEnv(String env) throws ReaderException {
        MiGooLog.log("load env begin: {}", env);
        JSONObject json = null;
        if (StringUtil.isNotBlank(env)) {
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
