/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package xyz.migoo;

import com.alibaba.fastjson2.JSONObject;
import core.xyz.migoo.function.Function;
import core.xyz.migoo.function.FunctionService;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import util.xyz.migoo.loader.Loader;
import xyz.migoo.convert.ConvertFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.jar.JarFile;

/**
 * @author mi.xiao
 * @date 2021/6/25 21:19
 */
public class CommandLine {

    public static void main(String[] args) {
        CommandLine.printLogo();
        try {
            if (args == null || args.length == 0 || "-h".equals(args[0]) || "-ext".equals(args[0])) {
                CommandLine.printHelp();
            } else {
                var arrays = Arrays.asList(args);
                if (!arrays.contains("-h2m") && !arrays.contains("p2m") && !arrays.contains("-f")) {
                    CommandLine.unsupportedCommand();
                } else {
                    CommandLine.loadClasspath(!arrays.contains("-ext") ? "" : args[arrays.indexOf("-ext")],
                            !arrays.contains("-ext") ? "" : args[arrays.indexOf("-ext") + 1]);
                    CommandLine.convert2Sampler(arrays.get(0), arrays.get(1));
                    if (arrays.contains("-f")) {
                        CommandLine.run(arrays.indexOf("-f") + 1 > arrays.size() ? null : args[arrays.indexOf("-f") + 1],
                                !arrays.contains("-r") || arrays.indexOf("-r") + 1 > arrays.size() ? null : args[arrays.indexOf("-r") + 1]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    public static void printLogo() {
        System.out.println("                _                          ");
        System.out.println("     _ __ ___  (_)  __ _   ___    ___      ");
        System.out.println("    | '_ ` _ \\ | | / _` | / _ \\  / _ \\     ");
        System.out.println("    | | | | | || || (_| || (_) || (_) |    ");
        System.out.println("    |_| |_| |_||_| \\__, | \\___/  \\___/     ");
        System.out.println("                   |___/     " + System.getProperty("migoo.version"));
        System.out.println();
    }

    public static void printHelp() {
        System.out.println("options: \n" +
                "-f: 测试用例文件路径，如：/user/migoo/testcase.yaml\n" +
                "-r: 测试报告保存路径，如：/user/migoo/report\n" +
                "-h2m: har文件路径，将指定的har文件转换为标准Http取样器文件\n" +
                "-p2m: postman文件路径，将指定的postman文件(v2.1)转换为标准Http取样器文件\n" +
                "-h: 帮助信息\n\n" +
                "example: ./migoo.sh -f ./example/example.yaml -r ./report");
    }

    public static void unsupportedCommand() {
        System.out.println();
        System.out.println("不支持的操作，请通过 -h 查看帮助");
    }

    public static void convert2Sampler(String command, String param) {
        if ("-h2m".equals(command) || "-p2m".equals(command)) {
            var file = new File(param);
            if (file.exists() && file.isFile()) {
                ConvertFactory.convert(command, file);
                System.out.println("取样器转换结束....");
            } else {
                System.out.println("文件不存在或不是一个文件");
            }
        }
    }

    public static void loadClasspath(String command, String param) throws Exception {
        if ("-ext".equals(command) && param != null && !param.isEmpty()) {
            System.setProperty("migoo.ext", param);
            loadClasspath(new File(param));
        }
    }

    public static void run(String file, String report) throws Exception {
        if (file == null || file.isEmpty()) {
            CommandLine.printHelp();
        } else {
            var yaml = Loader.toJavaObject(file, JSONObject.class);
            System.setProperty("migoo.report.output", report);
            System.out.println("用例解析完成....");
            MiGoo.start(yaml);
            System.out.println("测试执行完成....");
            System.out.println("测试报告路径: " + report);
        }
    }

    private static void loadClasspath(File dir) throws Exception {
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles(file -> file.exists() && file.isFile() && file.getName().endsWith(".jar"));
            if (files != null) {
                for (var file : files) {
                    loadClasspath(file);
                }
            }
        } else if (dir.exists() && dir.isFile() && dir.getName().endsWith(".jar")) {
            new ClassLoader(dir).loadClass();
        }
    }

    public static class ClassLoader extends URLClassLoader {

        private final File file;

        public ClassLoader(File file) throws Exception {
            super(new URL[]{file.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
            this.file = file;
        }

        public void loadClass() throws Exception {
            var entries = new JarFile(file).entries();
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
