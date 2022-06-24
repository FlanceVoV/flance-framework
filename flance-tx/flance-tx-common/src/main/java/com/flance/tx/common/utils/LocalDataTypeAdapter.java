package com.flance.tx.common.utils;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDataTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(jsonElement instanceof JsonPrimitive)){
            throw new JsonParseException("The date should be a string value");
        }
        return null;
    }

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss");
        String createTime = dateTimeFormatter.format(localDateTime);
        return new JsonPrimitive(createTime);
    }
}