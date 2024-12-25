package xyz.migoo;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.FunctionService;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;
import util.xyz.migoo.loader.Loader;
import xyz.migoo.convert.ConvertFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

@CommandLine.Command(name = "migoo(.sh/.bat)", version = "5.1.0", mixinStandardHelpOptions = true)
class Cli implements Runnable {

    @CommandLine.Option(names = {"-h2m", "--har2migoo"}, description = "har文件转换为migoo取样器描述文件，请输入har文件路径")
    String h2m;

    @CommandLine.Option(names = {"-p2m", "--postman2migoo"}, description = "postman文件转换为migoo取样器描述文件，请输入postman文件路径")
    String p2m;

    @CommandLine.Option(names = {"-f", "--file"}, description = "migoo测试套件描述文件路径")
    String file;

    @CommandLine.Option(names = {"-r", "--report"}, description = "migoo测试报告输出路径")
    String report;

    @CommandLine.Option(names = {"-ext", "--ext"})
    String ext;

    private static void loadClasspath(File dir) throws Exception {
        if (dir.exists() && dir.isDirectory()) {
            var files = dir.listFiles();
            if (files != null) {
                for (var file : files) {
                    loadClasspath(file);
                }
            }
        } else if (dir.exists() && dir.isFile() && dir.getName().endsWith(".jar")) {
            try (var loader = new ClassLoader(dir)) {
                loader.loadClass();
            }

        }
    }

    @Override
    public void run() {
        if (StringUtils.isNotBlank(ext)) {
            System.setProperty("migoo.ext", ext);
            try {
                loadClasspath(new File(ext));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (StringUtils.isNotBlank(file)) {
            System.setProperty(Constants.REPORT_ENABLE, StringUtils.isNotBlank(report) ? "true" : "false");
            System.setProperty(Constants.REPORT_OUTPUT, StringUtils.isNotBlank(report) ? report : "");
            var yaml = Loader.toJavaObject(file, JSONObject.class);
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

    static class ClassLoader extends URLClassLoader {

        private final File file;

        ClassLoader(File file) throws Exception {
            super(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
            this.file = file;
        }

        void loadClass() throws Exception {
            try (var jar = new JarFile(file)) {
                var entries = jar.entries();

                while (entries.hasMoreElements()) {
                    var fullName = entries.nextElement().getName();
                    if (!fullName.contains("META-INF") && fullName.endsWith(".class")) {
                        var className = fullName.substring(0, fullName.length() - 6).replace("/", ".");
                        var clz = super.loadClass(className);
                        if (Function.class.isAssignableFrom(clz)) {
                            FunctionService.addService((Function) clz.getConstructor().newInstance());
                        } else if (TestElement.class.isAssignableFrom(clz)) {
                            TestElementService.addService((Class<? extends TestElement>) clz);
                        }
                    }
                }
            }

        }
    }
}
