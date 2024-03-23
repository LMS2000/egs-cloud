package com.lms.lmscommon.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.lms.contants.HttpCode;
import com.lms.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public JsonUtil() {
    }

    public static <T> String toJson(T value) {
        try {
            return getInstance().writeValueAsString(value);
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static byte[] toJsonAsBytes(Object object) {
        try {
            return getInstance().writeValueAsBytes(object);
        } catch (JsonProcessingException var2) {
         throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转数组失败");
        }
    }

    public static <T> T parse(String content, Class<T> valueType) {
        try {
            return getInstance().readValue(content, valueType);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            return null;
        }
    }

    public static <T> T parse(String content, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(content, typeReference);
        } catch (IOException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static <T> T parse(byte[] bytes, Class<T> valueType) {
        try {
            return getInstance().readValue(bytes, valueType);
        } catch (IOException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static <T> T parse(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(bytes, typeReference);
        } catch (IOException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static <T> T parse(InputStream in, Class<T> valueType) {
        try {
            return getInstance().readValue(in, valueType);
        } catch (IOException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static <T> T parse(InputStream in, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(in, typeReference);
        } catch (IOException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static <T> List<T> parseArray(String content, Class<T> valueTypeRef) {
        try {
            if (!StrUtil.startWithIgnoreCase(content, "[")) {
                content = "[" + content + "]";
            }

            List<Map<String, Object>> list = (List)getInstance().readValue(content, new TypeReference<List<Map<String, Object>>>() {
            });
            List<T> result = new ArrayList();
            Iterator var4 = list.iterator();

            while(var4.hasNext()) {
                Map<String, Object> map = (Map)var4.next();
                result.add(toPojo(map, valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static JsonNode readTree(String jsonString) {
        Objects.requireNonNull(jsonString, "jsonString is null");

        try {
            return getInstance().readTree(jsonString);
        } catch (IOException var2) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static JsonNode readTree(InputStream in) {
        Objects.requireNonNull(in, "InputStream in is null");

        try {
            return getInstance().readTree(in);
        } catch (IOException var2) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static JsonNode readTree(byte[] content) {
        Objects.requireNonNull(content, "byte[] content is null");

        try {
            return getInstance().readTree(content);
        } catch (IOException var2) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static JsonNode readTree(JsonParser jsonParser) {
        Objects.requireNonNull(jsonParser, "jsonParser is null");

        try {
            return (JsonNode)getInstance().readTree(jsonParser);
        } catch (IOException var2) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable byte[] content, Class<T> valueType) {
        if (ObjectUtil.isEmpty(content)) {
            return null;
        } else {
            try {
                return getInstance().readValue(content, valueType);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable String jsonString, Class<T> valueType) {
        if (StrUtil.isBlank(jsonString)) {
            return null;
        } else {
            try {
                return getInstance().readValue(jsonString, valueType);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable InputStream in, Class<T> valueType) {
        if (in == null) {
            return null;
        } else {
            try {
                return getInstance().readValue(in, valueType);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable byte[] content, TypeReference<T> typeReference) {
        if (ObjectUtil.isEmpty(content)) {
            return null;
        } else {
            try {
                return getInstance().readValue(content, typeReference);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable String jsonString, TypeReference<T> typeReference) {
        if (StrUtil.isBlank(jsonString)) {
            return null;
        } else {
            try {
                return getInstance().readValue(jsonString, typeReference);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    @Nullable
    public static <T> T readValue(@Nullable InputStream in, TypeReference<T> typeReference) {
        if (in == null) {
            return null;
        } else {
            try {
                return getInstance().readValue(in, typeReference);
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static MapType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return getInstance().getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    public static CollectionLikeType getListType(Class<?> elementClass) {
        return getInstance().getTypeFactory().constructCollectionLikeType(List.class, elementClass);
    }

    public static <T> List<T> readList(@Nullable byte[] content, Class<T> elementClass) {
        if (ObjectUtil.isEmpty(content)) {
            return Collections.emptyList();
        } else {
            try {
                return (List)getInstance().readValue(content, getListType(elementClass));
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <T> List<T> readList(@Nullable InputStream content, Class<T> elementClass) {
        if (content == null) {
            return Collections.emptyList();
        } else {
            try {
                return (List)getInstance().readValue(content, getListType(elementClass));
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <T> List<T> readList(@Nullable String content, Class<T> elementClass) {
        if (ObjectUtil.isEmpty(content)) {
            return Collections.emptyList();
        } else {
            try {
                return (List)getInstance().readValue(content, getListType(elementClass));
            } catch (IOException var3) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <K, V> Map<K, V> readMap(@Nullable byte[] content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtil.isEmpty(content)) {
            return Collections.emptyMap();
        } else {
            try {
                return (Map)getInstance().readValue(content, getMapType(keyClass, valueClass));
            } catch (IOException var4) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <K, V> Map<K, V> readMap(@Nullable InputStream content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtil.isEmpty(content)) {
            return Collections.emptyMap();
        } else {
            try {
                return (Map)getInstance().readValue(content, getMapType(keyClass, valueClass));
            } catch (IOException var4) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <K, V> Map<K, V> readMap(@Nullable String content, Class<?> keyClass, Class<?> valueClass) {
        if (ObjectUtil.isEmpty(content)) {
            return Collections.emptyMap();
        } else {
            try {
                return (Map)getInstance().readValue(content, getMapType(keyClass, valueClass));
            } catch (IOException var4) {
                throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
            }
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return getInstance().convertValue(fromValue, toValueTypeRef);
    }

    public static <T> T treeToValue(TreeNode treeNode, Class<T> valueType) {
        try {
            return getInstance().treeToValue(treeNode, valueType);
        } catch (JsonProcessingException var3) {
            throw new BusinessException(HttpCode.OPERATION_ERROR,"JSON转对象失败");
        }
    }

    public static JsonNode valueToTree(@Nullable Object value) {
        return getInstance().valueToTree(value);
    }

    public static boolean canSerialize(@Nullable Object value) {
        return value == null ? true : getInstance().canSerialize(value.getClass());
    }

    public static Map<String, Object> toMap(String content) {
        try {
            return (Map)getInstance().readValue(content, Map.class);
        } catch (IOException var2) {
            log.error(var2.getMessage(), var2);
            return null;
        }
    }

    public static <T> Map<String, T> toMap(String content, Class<T> valueTypeRef) {
        try {
            Map<String, Map<String, Object>> map = (Map)getInstance().readValue(content, new TypeReference<Map<String, Map<String, Object>>>() {
            });
            Map<String, T> result = new HashMap(16);
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, Map<String, Object>> entry = (Map.Entry)var4.next();
                result.put(entry.getKey(), toPojo((Map)entry.getValue(), valueTypeRef));
            }

            return result;
        } catch (IOException var6) {
            log.error(var6.getMessage(), var6);
            return null;
        }
    }

    public static <T> T toPojo(Map fromValue, Class<T> toValueType) {
        return getInstance().convertValue(fromValue, toValueType);
    }

    public static ObjectMapper getInstance() {
        return JsonUtil.JacksonHolder.INSTANCE;
    }

    private static class JacksonObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 4288193147502386170L;
        private static final Locale CHINA;

        public JacksonObjectMapper(ObjectMapper src) {
            super(src);
        }

        public JacksonObjectMapper() {
            super.setLocale(CHINA);
            super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
            super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            super.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            super.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
            super.findAndRegisterModules();
            super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            super.configure(JsonReadFeature.ALLOW_SINGLE_QUOTES.mappedFeature(), true);
            super.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
            super.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, false);
//            super.registerModule(AppJavaTimeModule.INSTANCE);
            super.configure(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS, true);
            super.findAndRegisterModules();
        }

        public ObjectMapper copy() {
            return new JacksonObjectMapper(this);
        }

        static {
            CHINA = Locale.CHINA;
        }
    }

    private static class JacksonHolder {
        private static final ObjectMapper INSTANCE = new JacksonObjectMapper();

        private JacksonHolder() {
        }
    }
}
