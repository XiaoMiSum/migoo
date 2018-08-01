package xyz.migoo.report.html;

import java.text.NumberFormat;

/**
* @author xiaomi
 */
public class HtmlUtil {
	


	public static String getPercnet(int order, int total) {
		// 返回用例执行成功率
		NumberFormat numberFormat = NumberFormat.getInstance();
		//设置精确到小数点后2位
		numberFormat.setMaximumFractionDigits(2);
		double percnet = (double)order/(double)total * 100;
		return numberFormat.format(percnet);
	}

}
