/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

public interface FlowBuilder {

    <T extends Route> void route(Class<T> route);

    <T extends InjectionRef, S extends SubRoute> void routeInjection(Class<T> ref, Class<S> subroute);
}
