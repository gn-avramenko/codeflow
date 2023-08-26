/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.info;

import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.Route;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InfoBuilderExecutor {

    static final  Map<String, Processor> processors = new ConcurrentHashMap<>();

    private final FlowsRegistry registry;

    public InfoBuilderExecutor(FlowsRegistry registry) {
        this.registry = registry;
    }

    public<T extends Route> String buildInformation(Class<T> clazz) {
        Route route = registry.getRoute(clazz.getName());
        InfoBuilder builder = new InfoBuilder(clazz.getSimpleName());
        builder.getRootNode().information = "Mandatory parameters: " +
                route.getMandatoryInputData().stream().map(ContextKey::getId).toList();
        route.execute(new InfoSubRouteExecutor(registry, builder));
        return builder.buildInformation();
    }
}
