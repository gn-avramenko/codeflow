/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.info.InfoBuilderExecutor;
import com.gridnine.codeflow.validation.ValidationBuilderExecutor;
import org.junit.jupiter.api.Test;

public class SimpleFlowValidationBuilderTest {
    @Test
    public void testSimpleFlow(){
        FlowsRegistry registry = new FlowsRegistry();
        registry.apply(new SimpleRouteFlowConfigurator2());
        ValidationBuilderExecutor executor = new ValidationBuilderExecutor(registry);
        System.out.println(executor.buildValidationErrors(SimpleRoute2.class));
    }
}
