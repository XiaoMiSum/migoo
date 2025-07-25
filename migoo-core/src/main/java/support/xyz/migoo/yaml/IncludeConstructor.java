/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2025.  Lorem XiaoMiSum (mi_xiao@qq.com)
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

package support.xyz.migoo.yaml;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import support.xyz.migoo.TestDataLoader;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author xiaomi
 */
public class IncludeConstructor extends Constructor {

    private final File basePath;

    public IncludeConstructor() {
        this(null);
    }

    public IncludeConstructor(File basePath) {
        super(new LoaderOptions());
        this.basePath = basePath;
        this.yamlConstructors.put(new Tag("!include"), new IncludeConstruct());
        this.yamlConstructors.put(new Tag("!import"), new IncludeConstruct());
    }

    private class IncludeConstruct extends AbstractConstruct {
        @Override
        public Object construct(Node node) {
            if (node instanceof ScalarNode scalarNode) {
                String filename = constructScalar(scalarNode);
                String path = Paths.get(filename).isAbsolute() ? filename : basePath == null ? filename : new File(basePath, filename).getAbsolutePath();
                try {
                    return TestDataLoader.readFile(path);
                } catch (Exception e) {
                    throw new RuntimeException("加载 !include YAML文件错误: " + path, e);
                }
            }
            if (node instanceof SequenceNode sequenceNode) {
                var resp = new ArrayList<>();
                sequenceNode.getValue().forEach(item -> resp.add(construct(item)));
                return resp;
            }
            throw new RuntimeException("节点内容格式错误，请使用: !include file 或 !include [ file1, file2 ]");
        }
    }
}