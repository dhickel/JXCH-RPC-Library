package io.mindspice.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;


public class TypeRef {
    public static class RecordList extends TypeReference<List<Record>>{};
}
