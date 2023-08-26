/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

public interface SubRouteBuilder {
    <T extends Processor>void processor(Class<T> processor);

    <T extends InjectionRef> void injectionPoint(Class<T> clazz);
}
