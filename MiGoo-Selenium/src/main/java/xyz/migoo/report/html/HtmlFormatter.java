package xyz.migoo.report.html;

import xyz.migoo.utils.DateUtil;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
* @author xiaomi
 */
public class HtmlFormatter extends Formatter {

	private int id = 0;
	private long startTime;
	private long endTime;
	RecordStore recordStore = RecordStore.getInstance();

	private static final String JS_PAGE ="<script>" +
			"    var theTable = document.getElementById(\"tablelsw\");\n" +
			"    var totalPage = document.getElementById(\"spanTotalPage\");\n" +
			"    var pageNum = document.getElementById(\"spanPageNum\");\n" +
			"\n" +
			"    var spanPre = document.getElementById(\"spanPre\");\n" +
			"    var spanNext = document.getElementById(\"spanNext\");\n" +
			"    var spanFirst = document.getElementById(\"spanFirst\");\n" +
			"    var spanLast = document.getElementById(\"spanLast\");\n" +
			"\n" +
			"    var numberRowsInTable = theTable.rows.length;\n" +
			"    var pageSize = 10;\n" +
			"    var page = 1;\n" +
			"\n" +
			"    // 下一页\n" +
			"    function next() {\n" +
			"        hideTable();\n" +
			"        currentRow = pageSize * page;\n" +
			"        maxRow = currentRow + pageSize;\n" +
			"        if (maxRow > numberRowsInTable) maxRow = numberRowsInTable;\n" +
			"        for (var i = currentRow; i < maxRow; i++) {\n" +
			"\n" +
			"            theTable.rows[i].style.display = '';\n" +
			"        }\n" +
			"        page++;\n" +
			"\n" +
			"        if (maxRow == numberRowsInTable) {\n" +
			"            nextText();\n" +
			"            lastText();\n" +
			"        }\n" +
			"        showPage();\n" +
			"        preLink();\n" +
			"        firstLink();\n" +
			"    }\n" +
			"\n" +
			"    // 上一页\n" +
			"    function pre() {\n" +
			"        hideTable();\n" +
			"        page--;\n" +
			"        currentRow = pageSize * page;\n" +
			"        maxRow = currentRow - pageSize;\n" +
			"        if (currentRow > numberRowsInTable) currentRow = numberRowsInTable;\n" +
			"        for (var i = maxRow; i < currentRow; i++) {\n" +
			"            theTable.rows[i].style.display = '';\n" +
			"        }\n" +
			"\n" +
			"        if (maxRow == 0) {\n" +
			"            preText();\n" +
			"            firstText();\n" +
			"        }\n" +
			"        showPage();\n" +
			"        nextLink();\n" +
			"        lastLink();\n" +
			"    }\n" +
			"\n" +
			"    // 首页\n" +
			"    function first() {\n" +
			"        hideTable();\n" +
			"        page = 1;\n" +
			"        for (var i = 0; i < pageSize; i++) {\n" +
			"            theTable.rows[i].style.display = '';\n" +
			"        }\n" +
			"        showPage();\n" +
			"        preText();\n" +
			"        nextLink();\n" +
			"        lastLink();\n" +
			"    }\n" +
			"\n" +
			"    // 尾页\n" +
			"    function last() {\n" +
			"        hideTable();\n" +
			"        page = pageCount();\n" +
			"        currentRow = pageSize * (page - 1);\n" +
			"        for (var i = currentRow; i < numberRowsInTable; i++) {\n" +
			"            theTable.rows[i].style.display = '';\n" +
			"        }\n" +
			"        showPage();\n" +
			"        preLink();\n" +
			"        nextText();\n" +
			"        firstLink();\n" +
			"    }\n" +
			"\n" +
			"    function hideTable() {\n" +
			"        for (var i = 0; i < numberRowsInTable; i++) {\n" +
			"            theTable.rows[i].style.display = 'none';\n" +
			"        }\n" +
			"    }\n" +
			"\n" +
			"    function showPage() {\n" +
			"        pageNum.innerHTML = page;\n" +
			"    }\n" +
			"\n" +
			"    // 总共页数\n" +
			"    function pageCount() {\n" +
			"        var count = 0;\n" +
			"        if (numberRowsInTable % pageSize != 0) count = 1;\n" +
			"        return parseInt(numberRowsInTable / pageSize) + count;\n" +
			"    }\n" +
			"\n" +
			"    //显示链接\n" +
			"    function preLink() {\n" +
			"        spanPre.innerHTML = \"<a href='javascript:pre();'>&nbsp&nbsp<&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function preText() {\n" +
			"        spanPre.innerHTML = \"<a >&nbsp&nbsp<&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function nextLink() {\n" +
			"        spanNext.innerHTML = \"<a href='javascript:next();'>&nbsp&nbsp>&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function nextText() {\n" +
			"        spanNext.innerHTML = \"<a >&nbsp&nbsp>&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"\n" +
			"    function firstLink() {\n" +
			"        spanFirst.innerHTML = \"<a href='javascript:first();'>&nbsp&nbsp<<&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function firstText() {\n" +
			"        spanFirst.innerHTML = \"<a >&nbsp&nbsp<<&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function lastLink() {\n" +
			"        spanLast.innerHTML = \"<a href='javascript:last();'>&nbsp&nbsp>>&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    function lastText() {\n" +
			"        spanLast.innerHTML = \"<a >&nbsp&nbsp>>&nbsp&nbsp</a>\";\n" +
			"    }\n" +
			"\n" +
			"    // 隐藏表格\n" +
			"    function hide() {\n" +
			"        for (var i = pageSize; i < numberRowsInTable; i++) {\n" +
			"            theTable.rows[i].style.display = 'none';\n" +
			"        }\n" +
			"\n" +
			"        totalPage.innerHTML = pageCount();\n" +
			"        pageNum.innerHTML = '1';\n" +
			"\n" +
			"        nextLink();\n" +
			"        lastLink();\n" +
			"    }\n" +
			"\n" +
			"    hide();\n" +
			"</script>";

	private static final String HTML_HEAD = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//ZH\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "\n <html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"
			+ "\n <title>Test Report</title><link rel=\"stylesheet\" href=\"css/style.css\"/>"
			+ "\n </head><body><div id=\"hor-title-a\"><h2>Test Report</h2></div> "
			+ "\n <table id=\"hor-minimalist-b\" summary=\"Employee Pay Sheet\">"
			+ "\n <tr><th scope=\"col\"><b>序号</b></th>"
			+ "\n <th><b>用例描述</b></th>"
			+ "\n <th scope=\"col\"><b>期待结果</b></th>"
			+ "\n <th scope=\"col\"><b>实际结果</b></th>"
			+ "\n <th scope=\"col\"><b>执行时间</b></th>"
			+ "\n <th scope=\"col\"><b>状态</b></th></tr>"
			+ "\n <tbody id=\"tablelsw\">\n ";

	private int recordStep() {
		// 返回用例序号
		id += 1;
		return id;
	}

	@Override
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer();
		buf.append("<tr> \n ");
		buf.append("<td>");
		buf.append(recordStep());
		buf.append("</td> \n ");
		buf.append("<td>");
		buf.append(formatMessage(rec));
		buf.append("</td> \n ");
		buf.append("<td>");
		buf.append(recordStore.getExpected());
		buf.append("</td> \n ");
		buf.append("<td>");
		buf.append(recordStore.getActual());
		buf.append("</td> \n ");
		buf.append("<td>");
		buf.append(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS,rec.getMillis()));
		buf.append("</td> \n ");
		buf.append("<td>");
		String result = recordStore.getResult();
		// 根据执行结果设置字体颜色
		if (result.matches(HtmlReport.PASS)) {
			recordStore.setPass(1);
			buf.append("<span class=\"green\">");
			buf.append("<b>");
			buf.append(result);
			buf.append("</b>");
		} else if (result.matches(HtmlReport.FAIL)) {
			recordStore.setFail(1);
			buf.append("<span class=\"red\">");
			buf.append("<b>");
			buf.append(result);
			buf.append("</b>");
			/*
			//在report中加截图后，定义的html文件格式
			buf.append("<tr>");
			buf.append("<td>"); buf.append("</td>"); buf.append("<td>");
			buf.append(
			"<a href=getScreenShotPath()><img src=getScreenShotPath() height=\"150\" /></a>"
			); buf.append("</td>"); buf.append("<td>"); buf.append("</td>");
			buf.append("<td>"); buf.append("</td>"); buf.append("<td>");
			buf.append("</td>"); buf.append("<td>"); buf.append("</td>");
			buf.append("</tr>");
			 */
		} else {
			buf.append("<span class=\"red\">");
			buf.append("<b>");
			buf.append(result);
			buf.append("</b>");
		}
		buf.append("</span>");
		buf.append("</td> \n ");
		buf.append("</tr> \n ");
		return buf.toString();
	}

	@Override
	public String getHead(Handler h) {
		// 获取脚本开始运行的时间点，并返回 html_Head
		startTime = System.currentTimeMillis();
		return HTML_HEAD;
	}

	@Override
	public String getTail(Handler h) {
		// 获取脚本结束运行的时间点并返回html_Tail
		endTime = System.currentTimeMillis();
		int total = recordStore.getPass() + recordStore.getFail();
		String htmlTail;
		if (total > 0) {
			htmlTail = "</tbody></table>\n" + "<br /><br />"
					+ "\n <div class=\"bottom\">"
					+ "\n <div class=\"pager\">\n"
					+ "\n <span id=\"spanFirst\" class=\"pagerSpan\">&nbsp&nbsp<<&nbsp&nbsp</span>\n"
					+ "\n <span id=\"spanPre\" class=\"pagerSpan\">&nbsp&nbsp<&nbsp&nbsp</span>\n"
					+ "\n <span id=\"spanNext\" class=\"pagerSpan\">&nbsp&nbsp>&nbsp&nbsp</span>\n"
					+ "\n <span id=\"spanLast\" class=\"pagerSpan\">&nbsp&nbsp>>&nbsp&nbsp</span>\n"
					+ "\n 第<span id=\"spanPageNum\" class=\"pagerSpan\"></span>页\n"
					+ "\n /"
					+ "\n 共<span id=\"spanTotalPage\" class=\"pagerSpan\"></span>页"
					+ "\n <table id=\"hor-minimalist-a\">"
					+ "\n <tr><th scope=\"col\"><b>开始时间</b></th>"
					+ "\n <th scope=\"col\"><b>结束时间</b></th><th scope=\"col\"><b>运行时长</b></th>"
					+ "\n <th scope=\"col\"><b>执行总数</b></th><th scope=\"col\"><b>成功数量</b></th>"
					+ "\n <th scope=\"col\"><b>失败数量</b></th><th scope=\"col\"><b>成功率</b></th>"
					+ "\n <th scope=\"col\"><b>失败率</b></th></tr><tbody>"
					+ "\n <tr><td>" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS,startTime) + "</td>"
					+ "\n <td>" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS,endTime) + "</td>"
					+ "\n <td>" + DateUtil.format(startTime, endTime) + "</td>"
					+ "\n <td>"+ total + "</td>"
					+ "\n <td><span class=\"green\">" + recordStore.getPass() + "</span></td>"
					+ "\n <td><span class=\"red\">" + recordStore.getFail() + "</span></td>"
					+ "\n <td><span class=\"green\">"+ HtmlUtil.getPercnet(recordStore.getPass(), total) + "%</span></td>"
					+ "\n <td><span class=\"red\">" + HtmlUtil.getPercnet(recordStore.getFail(), total) + "%</span></td>"
					+ "\n </tr></tbody></table></div></div></body></html>" + JS_PAGE;

		} else {
			htmlTail = "</table><br>&nbsp;用例执行异常！<br><br></div></body></html>";
		}
		return htmlTail;
	}
}
