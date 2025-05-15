package com.example.spring_exam.common.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class EnumCodeDeserializer<T extends  Enum<T> & EnumCode> extends JsonDeserializer<T> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public EnumCodeDeserializer() {
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext context) throws IOException, JsonProcessingException {

        String code = getCode(p);

        if (!StringUtils.hasText(code)) {
            return type.getEnumConstants()[0];
        }

        return Arrays.stream(type.getEnumConstants())
                .filter(e -> e.getValue().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    private String getCode(JsonParser p) throws IOException {
        String code = null;

        if (JsonToken.START_OBJECT.equals(p.currentToken())) {
            while (!JsonToken.END_OBJECT.equals(p.nextToken())) {
                if (JsonToken.FIELD_NAME.equals(p.getCurrentToken())) {
                    if (org.apache.commons.codec.binary.StringUtils.equals(p.getValueAsString(), "code")) {
                        code = p.nextTextValue();
                    }
                }
            }
        } else {
            code = getCode(p.getValueAsString());
        }

        return code;
    }

    private String getCode(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, String> map = objectMapper.readValue(json, new TypeReference<>() {});

            return map != null ? map.get("code") : null;
        } catch (JsonProcessingException e) {
            return json;
        }
    }

}