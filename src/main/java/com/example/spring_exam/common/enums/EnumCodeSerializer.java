package com.example.spring_exam.common.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnumCodeSerializer<T extends EnumCode> extends JsonSerializer<T> {

    @Override
    public void serialize(T code, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("code", code.getValue());
        map.put("name", code.getDisplayName());
        gen.writeObject(map);
    }

}
