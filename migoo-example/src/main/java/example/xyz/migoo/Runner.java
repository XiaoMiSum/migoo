/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022.  Lorem XiaoMiSum (mi_xiao@qq.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package example.xyz.migoo;

import xyz.migoo.MiGoo;

public class Runner {

    public static void testBySuite(){
        MiGoo.start("classpath:testsuite/testsuite.yaml");
    }

    public static void testByLoginSets(){
        // ⚠️⚠️testsets 中的 httpdefaults 组件没有指定 host，如需单独执行 testsets，需要在对应的 httpdefaults 中添加 host: migoo.xyz
        MiGoo.start("classpath:testsets/login/testsets.yaml");
    }

    public static void testByRegisterSets(){
        // ⚠️⚠️testsets 中的 httpdefaults 组件没有指定 host，如需单独执行 testsets，需要在对应的 httpdefaults 中添加 host: migoo.xyz
        MiGoo.start("classpath:testsets/register/testsets.yaml");
    }

    public static void testByGetScheduleSets(){
        // ⚠️⚠️testsets 中的 httpdefaults 组件没有指定 host，如需单独执行 testsets，需要在对应的 httpdefaults 中添加 host: migoo.xyz
        MiGoo.start("classpath:testsets/get_schedule/testsets.yaml");
    }

    public static void main(String[] args) {
        testBySuite();
    }
}
