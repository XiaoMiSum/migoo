/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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


package xyz.migoo.framework.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import xyz.migoo.exception.ReaderException;
import xyz.migoo.loader.reader.YamlReader;

import java.util.regex.Pattern;

/**
 * @author xiaomi
 * @date 2018/7/25 14:10
 */
public class Platform {

    private Platform() {
    }

    private static final JSONObject PROPERTIES = new JSONObject();

    static {
        String[] properties = new String[]{"application.yml", "migoo.yml"};
        for (String file : properties) {
            try {
                YamlReader reader = new YamlReader("classpath://" + file);
                if (reader.isNull()) {
                    continue;
                }
                ((JSONObject)reader.read()).forEach(PROPERTIES::put);
            } catch (ReaderException e) {
                e.printStackTrace();
            }
        }
    }

    private static final JSONArray CHECK_JSON = PROPERTIES.getJSONArray("check.json");

    public static boolean isJson(String str) {
        for (int i = 0; i < CHECK_JSON.size(); i++) {
            if (Pattern.compile(CHECK_JSON.getString(i)).matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    public static final JSONArray CHECK_BODY = PROPERTIES.getJSONArray("check.body");

    public static final JSONArray CHECK_CODE = PROPERTIES.getJSONArray("check.code");

    public static final boolean MAIL_SEND = Boolean.parseBoolean(PROPERTIES.getString("mail.send").trim());

    public static final String MAIL_IMAP_HOST = PROPERTIES.getString("mail.imap.host").trim();

    public static final String MAIL_SEND_FROM = PROPERTIES.getString("mail.send.from").trim();

    public static final String MAIL_SEND_PASS = PROPERTIES.getString("mail.send.password").trim();

    public static final JSONArray IGNORE_DIRECTORY = PROPERTIES.getJSONArray("ignore.directory");

    public static final Object[] MAIL_SEND_TO_LIST = PROPERTIES.getJSONArray("mail.send.toList").toArray();

}
