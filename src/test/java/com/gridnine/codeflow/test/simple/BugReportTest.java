/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.bugreport.BugReportHelper;
import com.gridnine.codeflow.executor.FlowExecutor;
import org.junit.jupiter.api.Test;

import java.io.File;

public class BugReportTest {

    @Test
    public void testCreateBugReport(){
        FlowsRegistry registry = new FlowsRegistry();
        registry.apply(new BugReportRouteFlowConfigurator());
        FlowExecutor executor = new FlowExecutor(registry);
        Context ctx = new Context();
        ctx.setData(SimpleRouteKeys.SIMPLE_KEY, "Hello world");
        executor.execute(BugReportRoute.class, ctx);
    }

    @Test
    public void testUseBugReport(){
        FlowsRegistry registry = new FlowsRegistry();
        registry.apply(new BugReportRouteFlowConfigurator());
        FlowExecutor executor = new FlowExecutor(registry);
        BugReportHelper.replay(BugReportRoute.class, executor, new File("temp/bugreport.dat"));
    }
}
