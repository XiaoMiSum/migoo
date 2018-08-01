package xyz.migoo.report.parser;

import xyz.migoo.config.DataStore;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author xiaomi
 */
public abstract class AbstractParser {
	
	public String[] getFileList(String path,String fileSuffixes) {
		String[] fileNameTemp;
		String[] fileName=null;
		String suffixes;
		
		if(DataStore.FILE_LOG.equalsIgnoreCase(fileSuffixes)) {
			suffixes = ".log";
		} else if (DataStore.FILE_HTML.equalsIgnoreCase(fileSuffixes)) {
			suffixes = ".html";
		} else if (DataStore.FILE_TXT.equalsIgnoreCase(fileSuffixes)) {
			suffixes = ".txt";
		} else if (DataStore.FILE_PROPERTIES.equalsIgnoreCase(fileSuffixes)) {
			suffixes = ".properties";
		} else {
			suffixes = null;
		}

		ArrayList<String> list = new ArrayList<>();
		File file = new File(path);
		fileNameTemp = file.list();
		list.clear();
		if (null != fileNameTemp){
			for (int i = 0; i < fileNameTemp.length; i++) {
				if (fileNameTemp[i].contains(suffixes)) {
					if (fileNameTemp[i].contains("sum")) {
						continue;
					}
					list.add(fileNameTemp[i]);
				}
				fileName = list.toArray(new String[0]);
			}
		}
		return fileName;
	}
	
	public int countString(String content,String word){
		Pattern p = Pattern.compile(word);
	    Matcher m = p.matcher(content);
	    int occurrences = 0;
	    while(m.find()){
	    	occurrences ++;
	    }
	    return occurrences;
	 }
}
