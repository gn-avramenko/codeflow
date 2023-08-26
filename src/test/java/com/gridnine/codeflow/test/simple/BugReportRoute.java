/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.DebugHandler;
import com.gridnine.codeflow.api.Route;
import com.gridnine.codeflow.api.SubRouteBuilder;
import com.gridnine.codeflow.bugreport.BugReportDebugHandler;

import java.util.Set;

public class BugReportRoute implements Route {
    @Override
    public Set<ContextKey<?>> getMandatoryInputData() {
        return Set.of(SimpleRouteKeys.SIMPLE_KEY);
    }

    @Override
    public void execute(SubRouteBuilder rb) {
        rb.processor(SimpleProcessor.class);
    }

    @Override
    public DebugHandler getDebugHandler() {
        return new BugReportDebugHandler();
    }
}
