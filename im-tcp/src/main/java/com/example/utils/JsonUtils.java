package com.example.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将Java对象转换为JSON字符串
     *
     * @param obj 要转换的Java对象
     * @return JSON字符串
     * @throws JsonProcessingException 如果转换过程中发生错误
     */
    public static String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * 将JSON字符串反序列化为指定类型的Java对象
     *
     * @param jsonStr JSON字符串
     * @param clazz   目标对象的类型
     * @param <T>     泛型类型参数
     * @return 反序列化后的Java对象
     * @throws JsonProcessingException 如果解析过程中发生错误
     */
    public static <T> T fromJson(String jsonStr, Class<T> clazz)  {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
