package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.exception.ParserException;
import xyz.migoo.runner.TestResult;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.StringUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static xyz.migoo.config.Version.HTTPCLIENT_VERSION;
import static xyz.migoo.config.Version.JDK_VERSION;
import static xyz.migoo.config.Version.OS_VERSION;

/**
 * @author xiaomi
 * @date 2018/7/25 11:13
 */
public class Report {

    private Report(){
    }

    private final static TemplateEngine templateEngine = new TemplateEngine();


    public static void genReport(TestResult result, String reportName, String templates){
        if (StringUtil.isBlank(templates)){
            templates = "templates/migoo_report_template.html";
        }
        if (StringUtil.isBlank(reportName)){
            reportName = "Auto Test Report";
        }
        String template = templates(templates);
        Map<String, Object> params = result.report();
        params.put("report", "Test Report:  " + reportName);
        params.put("title", reportName + " - TestReport");
        params.put("platform", platform());
        String report = render(template,params);
        outPut(reportName, report);
    }

    /**
     * 使用 Thymeleaf 渲染 HTML * @param template HTML模板 * @param params 参数 * @return 渲染后的HTML
     */
    private static String render(String template, Map<String, Object> params) {
        Context context = new Context();
        context.setVariables(params);
        return templateEngine.process(template, context);
    }

    private static Map<String, String> platform() {
        Map<String, String> platform = new HashMap<>(3);
        platform.put("jdk", "JDK " + JDK_VERSION);
        platform.put("httpclient", "HTTP Client " + HTTPCLIENT_VERSION);
        platform.put("os", OS_VERSION);
        return platform;
    }

    private static String templates(String file){
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is;
        if (file.startsWith("templates")) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            is = classloader.getResourceAsStream(file);
        }else {
            try {
                is = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new ParserException("load template file error , file path " + file);
            }
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            throw new ParserException("load template file error , file path " + file);
        }
        return stringBuilder.toString();
    }

    private static void outPut(String name , String template){
        File file = new File(System.getProperty("user.dir") + "/reports/" + name + "-" +
                DateUtil.format(System.currentTimeMillis(), DateUtil.YYYYMMDD_HH_MM_SS) + ".html");
       try(Writer writer = new FileWriter(file)){
           writer.write(template);
           System.out.println(file.getPath());
       }catch (FileNotFoundException e){
           e.printStackTrace();
       }catch (IOException e){
           e.printStackTrace();
       }
    }

}
