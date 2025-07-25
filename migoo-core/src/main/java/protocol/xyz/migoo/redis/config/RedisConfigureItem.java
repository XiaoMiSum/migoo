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

package protocol.xyz.migoo.redis.config;

import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import core.xyz.migoo.config.ConfigureItem;
import core.xyz.migoo.context.ContextWrapper;
import org.apache.commons.lang3.StringUtils;
import protocol.xyz.migoo.redis.RedisConstantsInterface;

/**
 * @author xiaomi
 */
@JSONType(deserializer = RedisJSONInterceptor.class)
public class RedisConfigureItem implements ConfigureItem<RedisConfigureItem>, RedisConstantsInterface {

    @JSONField(name = DATASOURCE)
    private String datasource;
    @JSONField(name = URL, ordinal = 1)
    private String url;
    @JSONField(name = COMMAND, ordinal = 6)
    private String command;
    @JSONField(name = SEND, ordinal = 7)
    private String send;
    @JSONField(name = TIMEOUT, ordinal = 2)
    private int timeout;
    @JSONField(name = MAX_TOTAL, ordinal = 3)
    private int maxTotal;
    @JSONField(name = MAX_IDLE, ordinal = 4)
    private int maxIdle;
    @JSONField(name = MIN_IDLE, ordinal = 5)
    private int minIdle;


    @Override
    public RedisConfigureItem merge(RedisConfigureItem other) {
        if (other == null) {
            return copy();
        }
        var localOther = other.copy();
        var self = copy();
        self.datasource = StringUtils.isBlank(self.datasource) ? localOther.datasource : self.datasource;
        self.url = StringUtils.isBlank(self.url) ? localOther.url : self.url;
        self.command = StringUtils.isBlank(self.command) ? localOther.command : self.command;
        self.send = StringUtils.isBlank(self.send) ? localOther.send : self.send;
        self.timeout = (self.timeout = self.timeout > 0 ? localOther.timeout : self.timeout) > 0 ? self.timeout : 10000;
        self.maxTotal = (self.maxTotal = self.maxTotal > 0 ? localOther.maxTotal : self.maxTotal) > 0 ? self.maxTotal : 10;
        self.maxIdle = (self.maxIdle = self.maxIdle > 0 ? localOther.maxIdle : self.maxIdle) > 0 ? self.maxIdle : 5;
        self.minIdle = (self.minIdle = self.minIdle > 0 ? localOther.minIdle : self.minIdle) > 0 ? self.minIdle : 1;
        return self;
    }

    @Override
    public RedisConfigureItem calc(ContextWrapper context) {
        datasource = (String) context.eval(datasource);
        url = (String) context.eval(url);
        command = (String) context.eval(command);
        send = (String) context.eval(send);
        return this;
    }

    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSend() {
        return send;
    }

    public void setSend(String send) {
        this.send = send;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
}
