package com.example.examsystem.model.content;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class QuestionContentConverter implements AttributeConverter<QuestionContent, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(QuestionContent content) {
        if (content == null) return null;
        try {
            return mapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("题目内容序列化失败", e);
        }
    }

    @Override
    public QuestionContent convertToEntityAttribute(String json) {
        if (json == null || json.isBlank()) return null;
        try {
            return mapper.readValue(json, QuestionContent.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("题目内容反序列化失败", e);
        }
    }
}