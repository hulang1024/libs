package util;

import java.util.HashMap;

/**
 * 参数DSL
 * @author hulang
 * @sice 2017年5月5日
 * @vesion 1.0
 */
public class Params extends HashMap {
    public Params add(String key, Object value) {
        this.put(key, value);
        return this;
    }
}
