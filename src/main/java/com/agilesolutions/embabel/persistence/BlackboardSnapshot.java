package com.agilesolutions.embabel.persistence;

import java.util.List;
import java.util.Map;

public record BlackboardSnapshot(
        Map<String, Object> bindings,
        List<Object> objects
) {
}