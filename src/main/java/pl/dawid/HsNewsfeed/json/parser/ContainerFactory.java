package pl.dawid.HsNewsfeed.json.parser;

/**
 * Created by dawid on 03/06/16.
 */
import java.util.List;
import java.util.Map;

public interface ContainerFactory {
    Map createObjectContainer();

    List creatArrayContainer();
}

