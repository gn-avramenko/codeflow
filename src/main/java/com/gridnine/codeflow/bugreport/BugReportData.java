/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.bugreport;

import com.gridnine.codeflow.api.ContextKey;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BugReportData implements Serializable {
    private final Map<ContextKey<?>, Serializable> inputData = new HashMap<>();
    private final Map<String, Serializable> executionData = new HashMap<>();

    public Map<ContextKey<?>, Serializable> getInputData() {
        return inputData;
    }

    public Map<String, Serializable> getExecutionData() {
        return executionData;
    }
}
