package com.pp1.salve.configs.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.web.multipart.MultipartFile;

public class MultipartFileConverter implements Converter<String, MultipartFile> {
    @Override
    public MultipartFile convert(String source) {
        return null;
    }
}