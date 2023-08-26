/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

import java.io.Serializable;

public class ContextKey<T> implements Serializable {

    private final String id;

    private final Class<T> valueClass;

    public ContextKey(String id, Class<T> valueClass) {
        this.id = id;
        this.valueClass = valueClass;
    }

    public String getId() {
        return id;
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof  ContextKey) && ((ContextKey<?>) obj).getId().equals(getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
