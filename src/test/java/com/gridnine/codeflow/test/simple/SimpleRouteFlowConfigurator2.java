/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.FlowBuilder;
import com.gridnine.codeflow.api.FlowConfigurator;

public class SimpleRouteFlowConfigurator2 implements FlowConfigurator {

    @Override
    public void configure(FlowBuilder fb) {
        fb.route(SimpleRoute2.class);
    }
}
