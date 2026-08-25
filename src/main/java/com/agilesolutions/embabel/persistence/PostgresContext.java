package com.agilesolutions.embabel.persistence;

import com.embabel.agent.core.Blackboard;
import com.embabel.agent.core.Context;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PostgresContext implements Context {

    private final String id;

    private final Map<String, Object> bindings =
            new LinkedHashMap<>();

    private final List<Object> objects =
            new ArrayList<>();

    public PostgresContext(String id) {
        this.id = id;
    }

    public PostgresContext(
            String id,
            Map<String, Object> bindings,
            List<Object> objects) {

        this.id = id;

        if (bindings != null) {
            this.bindings.putAll(bindings);
        }

        if (objects != null) {
            this.objects.addAll(objects);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void bind(
            String key,
            Object value) {

        bindings.put(key, value);
        objects.add(value);
    }

    @Override
    public void addObject(Object value) {
        objects.add(value);
    }

    @Override
    public List<Object> getObjects() {
        return List.copyOf(objects);
    }

    @Override
    public void populate(Blackboard blackboard) {

        bindings.forEach(blackboard::bind);

        for (Object object : objects) {

            /*
             * Don't add an object twice when it is already represented
             * by a named binding.
             */
            if (!bindings.containsValue(object)) {
                blackboard.addObject(object);
            }
        }
    }

    public Map<String, Object> getBindings() {
        return Map.copyOf(bindings);
    }

    @Override
    public @NonNull String infoString(@Nullable Boolean verbose, int indent) {
        return "";
    }
}