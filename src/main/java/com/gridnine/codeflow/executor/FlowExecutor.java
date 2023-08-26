/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.executor;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.DebugHandler;
import com.gridnine.codeflow.api.FlowsRegistry;
import com.gridnine.codeflow.api.Processor;
import com.gridnine.codeflow.api.Route;
import com.gridnine.codeflow.api.DefaultDebugHandler;
import com.gridnine.codeflow.tracer.Tracer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FlowExecutor {

    static final ThreadLocal<Context> contexts = new ThreadLocal<>();

    static final  Map<String, Processor> processors = new ConcurrentHashMap<>();

    private final FlowsRegistry registry;

    public FlowExecutor(FlowsRegistry registry) {
        this.registry = registry;
    }

    public<T extends Route> void execute(Class<T> routeId, Context ctx) {
        contexts.set(ctx);
        Tracer tracer = new Tracer(routeId.getSimpleName());
        contexts.get().setTracer(tracer);
        try {
            Route route = registry.getRoute(routeId.getName());
            Context context = contexts.get();
            List<String> absentKeys = route.getMandatoryInputData().stream().filter(it -> context.getData(it) == null).map(ContextKey::getId).toList();
            if(!absentKeys.isEmpty()){
                throw new IllegalStateException("keys is absent " + absentKeys);
            }
            DebugHandler debugHandler = route.getDebugHandler();
            if(debugHandler == null){
                debugHandler = new DefaultDebugHandler();
            }
            debugHandler.beforeRouteStarts(ctx);
            route.execute(new SubRouteExecutor(registry));
            tracer.endRoute();
            debugHandler.afterRouteEnds(ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            contexts.remove();
        }
    }
}
