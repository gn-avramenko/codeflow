/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.info;

import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.InjectionRef;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.SubRoute;
import com.gridnine.codeflow.api.SubRouteBuilder;

import java.util.function.Consumer;

public class InfoSubRouteExecutor implements SubRouteBuilder {

    private final FlowsRegistry registry;

    private final InfoBuilder builder;


    public InfoSubRouteExecutor(FlowsRegistry registry, InfoBuilder builder) {
        this.registry = registry;
        this.builder = builder;
    }

    @Override
    public <T extends Processor> void processor(Class<T> processor) {
        Processor proc = InfoBuilderExecutor.processors.get(processor.getName());
        try {
            if(proc == null){
                proc = processor.getConstructor().newInstance();
                InfoBuilderExecutor.processors.put(processor.getName(), proc);
            }
            builder.startElement(processor.getSimpleName(), "processor", null);
            builder.endBlock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public<T extends InjectionRef> void injectionPoint(Class<T> clazz) {
        SubRoute routeInjection = registry.getRoutesInjection(clazz.getName());
        builder.startElement(clazz.getSimpleName(), "injection", null);
        if(routeInjection != null){
            builder.startElement(routeInjection.getClass().getSimpleName(), "subroute", null);
            routeInjection.execute(this);
            builder.endBlock();
        }
        builder.endBlock();
    }
}
