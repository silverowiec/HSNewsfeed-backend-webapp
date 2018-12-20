package pl.dawid.HsNewsfeed.json;

/**
 * Created by dawid on 03/06/16.
 */
import java.io.IOException;
import java.io.Writer;

public interface JSONStreamAware {
    void writeJSONString(Writer var1) throws IOException;
}
