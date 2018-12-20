package pl.dawid.HsNewsfeed.json;

/**
 * Created by dawid on 03/06/16.
 */



import pl.dawid.HsNewsfeed.json.parser.JSONParser;
import pl.dawid.HsNewsfeed.json.parser.ParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;


public class JSONValue {
    public JSONValue() {
    }

    public static Object parse(Reader var0) {
        try {
            JSONParser var1 = new JSONParser();
            return var1.parse(var0);
        } catch (Exception var2) {
            return null;
        }
    }

    public static Object parse(String var0) {
        StringReader var1 = new StringReader(var0);
        return parse((Reader)var1);
    }

    public static Object parseWithException(Reader var0) throws IOException, ParseException {
        JSONParser var1 = new JSONParser();
        return var1.parse(var0);
    }

    public static Object parseWithException(String var0) throws ParseException {
        JSONParser var1 = new JSONParser();
        return var1.parse(var0);
    }

    public static void writeJSONString(Object var0, Writer var1) throws IOException {
        if(var0 == null) {
            var1.write("null");
        } else if(var0 instanceof String) {
            var1.write(34);
            var1.write(escape((String)var0));
            var1.write(34);
        } else if(var0 instanceof Double) {
            if(!((Double)var0).isInfinite() && !((Double)var0).isNaN()) {
                var1.write(var0.toString());
            } else {
                var1.write("null");
            }

        } else if(!(var0 instanceof Float)) {
            if(var0 instanceof Number) {
                var1.write(var0.toString());
            } else if(var0 instanceof Boolean) {
                var1.write(var0.toString());
            } else if(var0 instanceof JSONStreamAware) {
                ((JSONStreamAware)var0).writeJSONString(var1);
            } else if(var0 instanceof JSONAware) {
                var1.write(((JSONAware)var0).toJSONString());
            } else if(var0 instanceof Map) {
                JSONObject.writeJSONString((Map)var0, var1);
            } else if(var0 instanceof List) {
                JSONArray.writeJSONString((List)var0, var1);
            } else {
                var1.write(var0.toString());
            }
        } else {
            if(!((Float)var0).isInfinite() && !((Float)var0).isNaN()) {
                var1.write(var0.toString());
            } else {
                var1.write("null");
            }

        }
    }

    public static String toJSONString(Object var0) {
        return var0 == null?"null":(var0 instanceof String?"\"" + escape((String)var0) + "\"":(var0 instanceof Double?(!((Double)var0).isInfinite() && !((Double)var0).isNaN()?var0.toString():"null"):(var0 instanceof Float?(!((Float)var0).isInfinite() && !((Float)var0).isNaN()?var0.toString():"null"):(var0 instanceof Number?var0.toString():(var0 instanceof Boolean?var0.toString():(var0 instanceof JSONAware?((JSONAware)var0).toJSONString():(var0 instanceof Map?JSONObject.toJSONString((Map)var0):(var0 instanceof List?JSONArray.toJSONString((List)var0):var0.toString()))))))));
    }

    public static String escape(String var0) {
        if(var0 == null) {
            return null;
        } else {
            StringBuffer var1 = new StringBuffer();
            escape(var0, var1);
            return var1.toString();
        }
    }

    static void escape(String var0, StringBuffer var1) {
        for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            switch(var3) {
                case '\b':
                    var1.append("\\b");
                    continue;
                case '\t':
                    var1.append("\\t");
                    continue;
                case '\n':
                    var1.append("\\n");
                    continue;
                case '\f':
                    var1.append("\\f");
                    continue;
                case '\r':
                    var1.append("\\r");
                    continue;
                case '\"':
                    var1.append("\\\"");
                    continue;
                case '/':
                    var1.append("\\/");
                    continue;
                case '\\':
                    var1.append("\\\\");
                    continue;
            }

            if(var3 >= 0 && var3 <= 31 || var3 >= 127 && var3 <= 159 || var3 >= 8192 && var3 <= 8447) {
                String var4 = Integer.toHexString(var3);
                var1.append("\\u");

                for(int var5 = 0; var5 < 4 - var4.length(); ++var5) {
                    var1.append('0');
                }

                var1.append(var4.toUpperCase());
            } else {
                var1.append(var3);
            }
        }

    }
}

