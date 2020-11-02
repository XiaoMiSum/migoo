/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018 XiaoMiSum (mi_xiao@qq.com)
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
 *
 */


package components.xyz.migoo.readers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author xiaomi
 */
public abstract class AbstractReader {

    protected static final String CLASSPATH = "classpath://";
    protected InputStream inputStream = null;

    /**
     * 文件合法性检查，传入的文件路径不能为 null "" 文件夹
     * @param file 文件
     */
    protected void validation(File file) throws ReaderException {
        if (!file.exists()){
            throw new ReaderException(String.format("file not found : %s", file.getPath()));
        }
        if (file.length() == 0){
            throw new ReaderException("file length == 0");
        }
    }

    protected boolean isClassPath(String path){
        return path.toLowerCase().startsWith(CLASSPATH);
    }

    protected void stream(String path) throws ReaderException {
        try {
            if (!isClassPath(path)){
                stream(new File(path));
            }else {
                path = path.substring(CLASSPATH.length());
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                inputStream = classLoader.getResourceAsStream(path);
            }
        }catch (Exception e){
            throw new ReaderException("file read exception", e);
        }
    }

    protected void stream(File file) throws ReaderException {
        try {
            validation(file);
            inputStream = new BufferedInputStream(new FileInputStream(file));
        }catch (Exception e){
            throw new ReaderException("file read exception: ", e);
        }
    }

    public boolean isNull(){
        return inputStream == null;
    }
}
