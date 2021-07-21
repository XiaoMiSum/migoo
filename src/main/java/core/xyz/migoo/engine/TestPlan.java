/*
 *
 *  * The MIT License (MIT)
 *  *
 *  * Copyright (c) 2018. Lorem XiaoMiSum (mi_xiao@qq.com)
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
 */

package core.xyz.migoo.engine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import core.xyz.migoo.testelement.AbstractTestElement;
import core.xyz.migoo.testelement.TestElement;
import core.xyz.migoo.testelement.TestElementService;
import core.xyz.migoo.variables.MiGooVariables;

import java.util.Vector;

/**
 * @author xiaomi
 */
public class TestPlan extends AbstractTestElement {

    private final Vector<TestElement> configElements = new Vector<>(10);

    private final Vector<TestElement> preprocessors = new Vector<>(10);

    private final Vector<TestElement> postprocessors = new Vector<>(10);

    private TestElement sampler;

    private final int level;

    public TestPlan(JSONObject json) {
        JSONObject planJson = (JSONObject) json.clone();
        setVariables(new MiGooVariables((JSONObject) planJson.remove(VARIABLES)));
        level = planJson.containsKey(CHILDS) ? 0 : 1;
        for (String key : planJson.keySet()) {
            if (!(planJson.get(key) instanceof JSONArray)) {
                setProperty(key.toLowerCase(), planJson.get(key));
            } else if (key.equalsIgnoreCase(CHILDS)) {
                JSONArray testElements = planJson.getJSONArray(key);
                for (int i = 0; i < testElements.size(); i++) {
                    getChildTestElements().add(new TestPlan(testElements.getJSONObject(i)));
                }
            } else {
                Vector<TestElement> node = TEST_ELEMENTS.equalsIgnoreCase(key) || CONFIG_ELEMENTS.equalsIgnoreCase(key) ? configElements :
                        PREPROCESSORS.equalsIgnoreCase(key) ? preprocessors :
                                POSTPROCESSORS.equalsIgnoreCase(key) ? postprocessors : this.getChildTestElements();
                addTestElements(planJson.getJSONArray(key), node);
            }

        }
    }

    private void addTestElements(JSONArray testElements, Vector<TestElement> node) {
        if (testElements != null && !testElements.isEmpty()) {
            for (int i = 0; i < testElements.size(); i++) {
                JSONObject testElement = testElements.getJSONObject(i);
                String testClass = (String) testElement.get(TEST_CLASS);
                TestElement element = TestElementService.getService(testClass);
                for (String subKey : testElement.keySet()) {
                    element.setProperty(subKey, testElement.get(subKey));
                }
                element.setVariables(getVariables());
                node.add(element);
            }
        }
    }

    public Vector<TestElement> getConfigElements() {
        return this.configElements;
    }

    public Vector<TestElement> getPreprocessors() {
        return this.preprocessors;
    }

    public Vector<TestElement> getPostprocessors() {
        return this.postprocessors;
    }

    public TestPlan traverseInto() {
        sampler = TestElementService.getService(getPropertyAsString(TEST_CLASS));
        sampler.setProperties(getProperty());
        sampler.setVariables(getVariables());
        return this;
    }

    public TestElement getSampler() {
        return sampler;
    }

    public int level() {
        return level;
    }
}