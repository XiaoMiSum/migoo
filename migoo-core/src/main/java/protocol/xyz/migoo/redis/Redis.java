package protocol.xyz.migoo.redis;

import java.util.List;

public class Redis {

    public static byte[] toBytes(Object result) {
        return switch (result) {
            case null -> new byte[0];
            case List<?> results -> {
                var sb = new StringBuilder("[");
                results.forEach(item -> sb.append(sb.length() > 1 ? "," : "").append(item instanceof String ? "\"" + item + "\"" : item));
                yield sb.append("]").toString().getBytes();
            }
            case byte[] bytes -> bytes;
            default -> result.toString().getBytes();
        };
    }
}
