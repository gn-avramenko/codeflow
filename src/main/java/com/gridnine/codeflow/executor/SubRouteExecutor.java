/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.executor;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.InjectionRef;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.SubRoute;
import com.gridnine.codeflow.api.SubRouteBuilder;
import com.gridnine.codeflow.tracer.Tracer;

import java.util.function.Consumer;

public class SubRouteExecutor implements SubRouteBuilder {

    private final FlowsRegistry registry;

    public SubRouteExecutor(FlowsRegistry registry) {
        this.registry = registry;
    }

    @Override
    public <T extends Processor> void processor(Class<T> processor) {
        Processor proc = FlowExecutor.processors.get(processor.getName());
        Context ctx = FlowExecutor.contexts.get();
        try {
            if(proc == null){
                proc = processor.getConstructor().newInstance();
                FlowExecutor.processors.put(processor.getName(), proc);
            }
            ctx.getTracer().startElement(processor.getSimpleName(), "processor");
            proc.execute(ctx);
            ctx.getTracer().endBlock();
        } catch (Exception e) {
            ctx.getTracer().message("error occurred", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public<T extends InjectionRef> void injectionPoint(Class<T> clazz) {
        Context ctx = FlowExecutor.contexts.get();
        ctx.getTracer().startElement(clazz.getSimpleName(), "injection");
        SubRoute routeInjection = registry.getRoutesInjection(clazz.getName());
        if(routeInjection != null){
            ctx.getTracer().startElement(routeInjection.getClass().getSimpleName(), "subroute");
            routeInjection.execute(this);
            ctx.getTracer().endBlock();
        }
        ctx.getTracer().endBlock();
    }
}
