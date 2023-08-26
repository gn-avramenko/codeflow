/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.SubRoute;
import com.gridnine.codeflow.api.SubRouteBuilder;

public class InjectionSubRoute implements SubRoute {
    @Override
    public void execute(SubRouteBuilder builder) {
        builder.processor(SimpleProcessor2.class);
    }
}
