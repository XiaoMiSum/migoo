package xyz.migoo.report;

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

    public static File zipFile(String path, String fileName){
        File source = new File(path);
        if (!source.exists()) {
            source.mkdir();
        }
        Project project = new Project();
        FileSet fileSet = new FileSet();
        fileSet.setExcludes("*.zip");
        if (source.isDirectory()){
            fileSet.setDir(source);
            path = source.getPath() + File.separator;
        }
        if(source.isFile()){
            fileSet.setFile(source);
            path = source.getParent() + File.separator;
        }
        Zip zip = new Zip();
        zip.setProject(project);
        zip.setDestFile(new File(  path + fileName + ".zip"));
        zip.addFileset(fileSet);
        zip.execute();
        return new File(path + fileName + ".zip");
    }
}
