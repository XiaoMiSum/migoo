package protocol.xyz.migoo.http;

import core.xyz.migoo.sampler.SampleResult;
import org.apache.hc.core5.http.Header;

import java.util.List;

public abstract class HTTP extends SampleResult.Real {


    protected String version;

    protected List<Header> headers;

    public HTTP(byte[] bytes) {
        super(bytes);
    }

    public List<Header> headers() {
        return headers;
    }

    protected void header(StringBuilder buf) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        headers.forEach(header -> buf.append(header.getName()).append(": ").append(header.getValue()).append("\n"));
        buf.append("\n");
    }
}
