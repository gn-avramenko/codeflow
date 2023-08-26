/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Lunda.ru 2
 *****************************************************************/

package com.gridnine.codeflow.api;

import java.util.Collections;
import java.util.Set;

public interface Processor{

    void execute(Context ctx) throws Exception;

    default Set<ContextKey<?>> getRequiredKeys(){
        return Collections.emptySet();
    }
}
