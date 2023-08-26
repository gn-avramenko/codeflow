/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.executor.FlowExecutor;
import org.junit.jupiter.api.Test;

public class SimpleFlowTest {
    @Test
    public void testSimpleFlow(){
        FlowsRegistry registry = new FlowsRegistry();
        registry.apply(new SimpleRouteFlowConfigurator());
        FlowExecutor executor = new FlowExecutor(registry);
        Context context = new Context();
        context.setData(SimpleRouteKeys.SIMPLE_KEY, "Hello world");
        executor.execute(SimpleRoute.class, context);
    }
}
