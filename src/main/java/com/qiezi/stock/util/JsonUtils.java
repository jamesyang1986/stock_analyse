package com.qiezi.stock.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    //each thread has its own ObjectMapper instance
    private static ThreadLocal<ObjectMapper> objMapperLocal = new ThreadLocal<ObjectMapper>() {
        @Override
        public ObjectMapper initialValue() {
            return new ObjectMapper().configure(JsonParser.Feature.INTERN_FIELD_NAMES, false);
        }
    };

    public static String toJSON(Object value) {
        String result = null;
        try {
            result = objMapperLocal.get().writeValueAsString(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Fix null string
        if ("null".equals(result)) {
            result = null;
        }
        return result;
    }

    public static <T> T toT(String jsonString, Class<T> clazz) {
        try {
            return objMapperLocal.get().readValue(jsonString, clazz);
        } catch (Exception e) {
            logger.error("toT  {},error:", jsonString, e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public static <T> T toT(String jsonString, TypeReference valueTypeRef) {
        try {
            return objMapperLocal.get().readValue(jsonString, valueTypeRef);
        } catch (Exception e) {
            logger.error("toT  {},error:", jsonString, e);
        }
        return null;
    }

    public static <T> List<T> toTList(String jsonString, Class<T> clazz) {
        try {
            return objMapperLocal.get().readValue(jsonString, TypeFactory.collectionType(List.class, clazz));
        } catch (Exception e) {
            logger.error("toT  {}, error:", jsonString, e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String jsonString) {
        return toT(jsonString, Map.class);
    }

    public static String prettyPrint(Object value) {
        String result = null;
        try {
            result = objMapperLocal.get().defaultPrettyPrintingWriter().writeValueAsString(value);
        } catch (Exception e) {
            logger.error("prettyPrint error: {}, {}", value, e);
        }
        // Fix null string
        if ("null".equals(result)) {
            result = null;
        }
        return result;
    }


}

