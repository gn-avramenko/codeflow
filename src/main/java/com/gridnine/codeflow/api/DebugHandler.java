/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

public interface DebugHandler {
    void beforeRouteStarts(Context ctx) throws Exception;
    void afterRouteEnds(Context ctx) throws Exception;
}
