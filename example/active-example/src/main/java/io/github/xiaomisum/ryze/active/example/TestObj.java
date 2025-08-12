package io.github.xiaomisum.ryze.active.example;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

public class TestObj implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
