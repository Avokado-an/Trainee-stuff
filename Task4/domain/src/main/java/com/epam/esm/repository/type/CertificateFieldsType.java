package com.epam.esm.repository.type;

import com.epam.esm.repository.consumer.*;
import com.epam.esm.repository.mapper.StringToIntegerMapper;
import com.epam.esm.repository.mapper.StringToLongMapper;
import com.epam.esm.repository.mapper.StringToStringMapper;

import java.util.function.Function;

public enum CertificateFieldsType {
    NAME(new StringToStringMapper(), new NameEditor()),
    DESCRIPTION(new StringToStringMapper(), new DescriptionEditor()),
    PRICE(new StringToLongMapper(), new PriceEditor()),
    DURATION(new StringToIntegerMapper(), new DurationEditor());

    private Function valueMapper;
    private FieldEditor fieldEditor;

    CertificateFieldsType(Function valueMapper, FieldEditor fieldEditor) {
        this.valueMapper = valueMapper;
        this.fieldEditor = fieldEditor;
    }

    public Function getValueMapper() {
        return valueMapper;
    }

    public FieldEditor getFieldEditor() {
        return fieldEditor;
    }
}
