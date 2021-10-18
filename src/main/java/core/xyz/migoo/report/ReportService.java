/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining
 *  * a copy of this software and associated documentation files (the
 *  * 'Software'), to deal in the Software without restriction, including
 *  * without limitation the rights to use, copy, modify, merge, publish,
 *  * distribute, sublicense, and/or sell copies of the Software, and to
 *  * permit persons to whom the Software is furnished to do so, subject to
 *  * the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be
 *  * included in all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 *  * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 *  * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 *  * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 *  * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package core.xyz.migoo.report;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author xiaomi
 */
public class ReportService {

    private static final Map<String, Class<? extends Report>> SERVICES = new HashMap<>(20);

    static {
        for (Report element : ServiceLoader.load(Report.class)) {
            addService(element.getClass());
        }
    }

    public static Report getService(String key) {
        Class<? extends Report> report = SERVICES.get(key.toLowerCase());
        if (report == null) {
            throw new RuntimeException("No matching test element: " + key);
        }
        try {
            return report.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Get report instance error. ", e);
        }
    }

    public static void addService(Class<? extends Report> report) {
        SERVICES.put(report.getSimpleName().toLowerCase(Locale.ROOT), report);
    }

    public static Map<String, Class<? extends Report>> getServices() {
        return new HashMap<>(SERVICES);
    }
}