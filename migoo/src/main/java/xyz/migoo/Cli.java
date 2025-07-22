package xyz.migoo;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import support.xyz.migoo.TestDataLoader;
import xyz.migoo.convert.ConvertFactory;

import java.io.File;

@CommandLine.Command(name = "migoo(.sh/.bat)", version = "5.2.1", mixinStandardHelpOptions = true)
class Cli implements Runnable {

    @CommandLine.Option(names = {"-h2m", "--har2migoo"}, description = "har文件转换为migoo取样器描述文件，请输入har文件路径")
    String h2m;

    @CommandLine.Option(names = {"-p2m", "--postman2migoo"}, description = "postman文件转换为migoo取样器描述文件，请输入postman文件路径")
    String p2m;

    @CommandLine.Option(names = {"-f", "--file"}, description = "migoo测试套件描述文件路径")
    String file;

    @CommandLine.Option(names = {"-r", "--report"}, description = "migoo测试报告输出路径")
    String report;

    @Override
    public void run() {

        if (StringUtils.isNotBlank(file)) {
            System.setProperty(Constants.REPORT_ENABLE, StringUtils.isNotBlank(report) ? "true" : "false");
            System.setProperty(Constants.REPORT_OUTPUT, StringUtils.isNotBlank(report) ? report : "");
            var yaml = TestDataLoader.toJavaObject(file, JSONObject.class);
            System.out.println("用例解析完成....");
            MiGoo.start(yaml);
            System.out.println("测试执行完成....");
            System.out.println("测试报告路径: " + report);
        } else if (StringUtils.isNoneBlank(h2m, p2m)) {
            var f = new File(StringUtils.isNotBlank(h2m) ? h2m : p2m);
            if (f.exists() && f.isFile()) {
                ConvertFactory.convert(StringUtils.isNotBlank(h2m) ? "h2m" : "p2m", f);
                System.out.println("取样器转换结束....");
            } else {
                System.out.println("文件不存在或不是一个文件");
            }
        } else {
            System.out.println("没有找到有效操作参数，请通过 -h 查看帮助");
        }
    }
}
