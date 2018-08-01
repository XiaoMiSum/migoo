package xyz.migoo.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
* @author xiaomi
 */
public class HtmlUtil {

    private HtmlUtil(){}

    /**
     * 通过 HTML节点 属性 进行解析，如：name 、 class 等
     * @param s 获取到的html源码 或 HTML文件完整路径
     * @param attribute 节点属性，如：name 、 class 等
     * @param value 节点属性名称，如： name="merOrderId" ，取 merOrderId
     * @param index 取第几个节点的值
     * @return 找到的第一个符合条件的节点的 value 中的字符串
     */

    public static String parse(String s, String attribute, String value,int index) {
        Document doc = Jsoup.parse(s);
        Elements elements = doc.getElementsByAttributeValue(attribute, value);
        String str = elements.get(index).attr("value");
        if ("" == str.trim()){
            return elements.get(index).text();
        }
        return str;
    }

    public static String parse(String s, String attribute, String value,String attributeKey) {
        Document doc = Jsoup.parse(s);
        Elements elements = doc.getElementsByAttributeValue(attribute, value);
        String str = elements.get(0).attr(attributeKey);
        if ("" == str.trim()){
            return elements.get(0).text();
        }
        return str;
    }
}
