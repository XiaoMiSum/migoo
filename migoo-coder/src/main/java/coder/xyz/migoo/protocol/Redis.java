package coder.xyz.migoo.protocol;

import coder.xyz.migoo.El;

public class Redis extends El {

    Redis() {
    }

    Redis(String name) {
        p("variable_name", name);
    }

    public Redis host(String host) {
        p("host", host);
        return this;
    }

    public Redis port(String port) {
        p("port", port);
        return this;
    }

    public Redis port(Integer port) {
        p("port", port);
        return this;
    }

    public Redis database(int database) {
        p("database", database);
        return this;
    }

    public Redis password(String password) {
        p("password", password);
        return this;
    }

    public Redis timeout(int timeout) {
        p("timeout", timeout);
        return this;
    }

    public Redis minIdle(int minIdle) {
        p("minIdle", minIdle);
        return this;
    }

    public Redis maxTotal(int maxTotal) {
        p("maxTotal", maxTotal);
        return this;
    }

    public Redis datasource(String datasource) {
        p("datasource", datasource);
        return this;
    }

    public Redis command(String command) {
        p("command", command);
        return this;
    }

    public Redis send(String send) {
        p("send", send);
        return this;
    }
}
