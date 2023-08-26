/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.Processor;

public class SimpleProcessor2 implements Processor {
    @Override
    public void execute(Context ctx) throws Exception {
        System.out.println(2);
    }
}
