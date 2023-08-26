/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.validation;

import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.InjectionRef;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.SubRoute;
import com.gridnine.codeflow.api.SubRouteBuilder;

import java.util.List;
import java.util.function.Consumer;

public class ValidationRouteExecutor implements SubRouteBuilder {

    private final FlowsRegistry registry;

    private final ValidationBuilder builder;


    public ValidationRouteExecutor(FlowsRegistry registry, ValidationBuilder builder) {
        this.registry = registry;
        this.builder = builder;
    }

    @Override
    public <T extends Processor> void processor(Class<T> processor) {
        Processor proc = ValidationBuilderExecutor.processors.get(processor.getName());
        try {
            if(proc == null){
                proc = processor.getConstructor().newInstance();
                ValidationBuilderExecutor.processors.put(processor.getName(), proc);
            }
            List<String> absentKeys = proc.getRequiredKeys().stream()
                    .filter(it -> !builder.getKeys().contains(it)).map(ContextKey::getId).toList();
            if(!absentKeys.isEmpty()){
                builder.startElement(processor.getSimpleName(), "processor", "Some keys are absent: "  + absentKeys);
                builder.endBlock();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public<T extends InjectionRef> void injectionPoint(Class<T> clazz) {
        builder.startElement(clazz.getSimpleName(), "injection",  null);
        SubRoute routeInjection = registry.getRoutesInjection(clazz.getName());
        if(routeInjection != null){
            builder.startElement(clazz.getSimpleName(), "injection",  null);
            routeInjection.execute(this);
            builder.endBlock();
        }
        builder.endBlock();
    }
}
