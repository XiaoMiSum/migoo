package xyz.migoo.utils;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;

import java.io.*;

/**
 * @author xiaomi
 * @date 2018/9/9 10:52
 */
public class ZipUtil {

    private ZipUtil(){

    }

    public static File zipFile(String path, String fileName) throws IOException{
        File source = new File(path);
        if (!source.exists()) {
            throw new IOException(path + "不存在！");
        }
        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setExcludes("*.zip");
        if (source.isDirectory()){
            fileSet.setDir(source);
            path = source.getPath() + File.separator;
        }else {
            fileSet.setFile(source);
            path = source.getParent() + File.separator;
        }
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(  path + fileName + ".zip"));
        zip.addFileset(fileSet);
        zip.execute();
        File zipFile = new File(path + fileName + ".zip");
        return zipFile;
    }
}
