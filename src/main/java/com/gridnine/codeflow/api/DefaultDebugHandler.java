/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.DebugHandler;

public class DefaultDebugHandler implements DebugHandler {

    @Override
    public void beforeRouteStarts(Context ctx) throws Exception {
        //noops
    }

    @Override
    public void afterRouteEnds(Context ctx) throws Exception {
        System.out.println(ctx.getTracer().buildDebugInfo());
    }
}
