/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.Route;
import com.gridnine.codeflow.api.SubRouteBuilder;

import java.util.Set;

public class SimpleRoute2 implements Route {
    @Override
    public Set<ContextKey<?>> getMandatoryInputData() {
        return Set.of();
    }

    @Override
    public void execute(SubRouteBuilder rb) {
        rb.processor(SimpleProcessor3.class);
    }
}
