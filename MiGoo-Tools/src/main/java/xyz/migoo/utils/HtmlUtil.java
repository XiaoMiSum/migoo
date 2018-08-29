package xyz.migoo.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author xiaomi
 */
public class HtmlUtil {

    private HtmlUtil() {
    }

    /**
     * 通过 HTML节点 属性 进行解析，如：name 、 class 等
     *
     * @param s         获取到的html源码 或 HTML文件完整路径
     * @param cssQuery  指定定位方式
     * @param index     指定第 index 个元素
     * @param attribute 节点属性，如：name 、 class 等
     * @return
     */
    public static String parse(String s, String cssQuery, int index, String attribute) {
        Document doc = Jsoup.parse(s);
        Element element = doc.select(cssQuery).get(index);
        String str = element.attr(attribute);
        if (StringUtil.isBlank(str)) {
            return element.text();
        }
        return str;
    }

    public static String parse(String s, String cssQuery, String attribute) {
        try {
            int index = Integer.parseInt(attribute);
            return parse(s, cssQuery, index, "value");
        } catch (Exception e) {
            return parse(s, cssQuery, 0, attribute);
        }
    }

    public static String parse(String s, String cssQuery) {
        return parse(s, cssQuery, 0, "value");
    }

    public static String parse(String s, String[] o) {
        if (o == null || o.length == 0) {
            return null;
        }
        if (o.length == 1) {
            return parse(s, o[0]);
        }
        if (o.length == (1 + 1)) {
            return parse(s, o[0], o[1]);
        }
        if (o.length == (1 + 1 + 1)) {
            return parse(s, o[0], Integer.parseInt(o[1]), o[2]);
        }
        return null;
    }
}
