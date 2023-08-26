/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.Processor;

import java.util.Collections;
import java.util.Set;

public class SimpleProcessor3 implements Processor {
    @Override
    public void execute(Context ctx) throws Exception {
        System.out.println(2);
    }

    @Override
    public Set<ContextKey<?>> getRequiredKeys() {
        return Collections.singleton(SimpleRouteKeys.SIMPLE_KEY_2);
    }
}
