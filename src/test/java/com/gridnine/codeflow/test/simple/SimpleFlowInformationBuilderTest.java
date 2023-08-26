/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.info.InfoBuilderExecutor;
import org.junit.jupiter.api.Test;

public class SimpleFlowInformationBuilderTest {
    @Test
    public void testSimpleFlow(){
        FlowsRegistry registry = new FlowsRegistry();
        registry.apply(new SimpleRouteFlowConfigurator());
        InfoBuilderExecutor executor = new InfoBuilderExecutor(registry);
        System.out.println(executor.buildInformation(SimpleRoute.class));
    }
}
