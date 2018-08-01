package xyz.migoo.report;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.migoo.exception.ReportException;
import xyz.migoo.utils.DateUtil;
import xyz.migoo.utils.Log;
import xyz.migoo.utils.StringUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static xyz.migoo.config.Platform.HTTPCLIENT_VERSION;
import static xyz.migoo.config.Platform.JDK_VERSION;
import static xyz.migoo.config.Platform.OS_VERSION;

/**
 * @author xiaomi
 * @date 2018/7/25 11:13
 */
public class Report {


    private static Log log = new Log(Report.class);
    private final static String TEMPLATE_CLASS = "templates";
    private final static TemplateEngine TEMPLATE_ENGINE = new TemplateEngine();

    private Report(){
    }

    public static String generateReport(Map<String, Object> report, String reportName, String templates){
        if (StringUtil.isBlank(templates)){
            templates = "templates/migoo_report_template.html";
        }
        if (StringUtil.isBlank(reportName)){
            reportName = "Auto Test Report";
        }
        report.put("report", "Test Report:  " + reportName);
        report.put("title", reportName + " - TestReport");
        report.put("platform", platform());
        return report(reportName, render(templates, report));
    }

    /**
     * 使用 Thymeleaf 渲染 HTML
     * @param template HTML模板
     * @param report 报告数据
     * @return 渲染后的HTML
     */
    private static String render(String template, Map<String, Object> report) {
        template = loadTemplate(template);
        Context context = new Context();
        context.setVariables(report);
        return TEMPLATE_ENGINE.process(template, context);
    }

    private static Map<String, String> platform() {
        Map<String, String> platform = new HashMap<>(3);
        platform.put("jdk", "JDK " + JDK_VERSION);
        platform.put("httpclient", "HTTP Client " + HTTPCLIENT_VERSION);
        platform.put("os", OS_VERSION);
        return platform;
    }

    private static String loadTemplate(String file){
        InputStream is;
        if (file.startsWith(TEMPLATE_CLASS)) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            is = classloader.getResourceAsStream(file);
        }else {
            try {
                is = new BufferedInputStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                log.error(e.getMessage(), e);
                throw new ReportException("load template file error , file path " + file);
            }
        }
        return read(is);
    }

    private static String read(InputStream is){
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ReportException("load template file error. " + e.getMessage());
        }
        return stringBuilder.toString();
    }

    private static String report(String name , String template){
        File file = new File(new File(System.getProperty("user.dir")).getParent() + "/Reports/");
        if (!file.exists()){
         file.mkdir();
        }
       try(Writer writer = new FileWriter(file.getPath() + name + "-" +
               DateUtil.format(DateUtil.YYYYMMDD_HH_MM_SS) + ".html")){
           writer.write(template);
       }catch (Exception e){
           log.error(e.getMessage(), e);
           throw new ReportException("create report error , file path " + file.getPath());
       }
       return file.getPath();
    }

}
