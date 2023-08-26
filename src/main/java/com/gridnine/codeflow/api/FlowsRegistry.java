/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

import java.util.HashMap;
import java.util.Map;

public class FlowsRegistry {

    private final Map<String, Route> routes = new HashMap<>();

    private final Map<String, SubRoute> routesInjections = new HashMap<>();

    public SubRoute getRoutesInjection(String id) {
        return routesInjections.get(id);
    }

    public Route getRoute(String id) {
        return routes.get(id);
    }

    public void apply(FlowConfigurator configurator){
        configurator.configure(new FlowBuilder() {
            @Override
            public<T extends Route> void route(Class<T> clazz) {
                try {
                    routes.put(clazz.getName(), clazz.getConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public <T extends InjectionRef, S extends SubRoute> void routeInjection(Class<T> ref, Class<S> subroute) {
                try {
                    routesInjections.put(ref.getName(), subroute.getConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
