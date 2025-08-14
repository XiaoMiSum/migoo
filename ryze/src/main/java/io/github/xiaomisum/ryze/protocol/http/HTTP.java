package io.github.xiaomisum.ryze.protocol.http;

import io.github.xiaomisum.ryze.core.testelement.sampler.DefaultSampleResult;
import xyz.migoo.simplehttp.Request;
import xyz.migoo.simplehttp.Response;

public class HTTP {

    public static Response execute(Request request, DefaultSampleResult result) {
        result.sampleStart();
        try {
            return request.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            result.sampleEnd();
        }
    }
}
