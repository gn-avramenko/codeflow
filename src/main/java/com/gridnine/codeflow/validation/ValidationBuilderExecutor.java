/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.validation;

import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.Route;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ValidationBuilderExecutor {

    static final  Map<String, Processor> processors = new ConcurrentHashMap<>();

    private final FlowsRegistry registry;

    public ValidationBuilderExecutor(FlowsRegistry registry) {
        this.registry = registry;
    }

    public<T extends Route> String buildValidationErrors(Class<T> cls) {
        ValidationBuilder builder = new ValidationBuilder(cls.getSimpleName());
        Route route = registry.getRoute(cls.getName());
        builder.getKeys().addAll(route.getMandatoryInputData());
        route.execute(new ValidationRouteExecutor(registry, builder));
        return builder.isHasError()? builder.buildValidation(): null;
    }
}
