/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.api;

import java.util.Set;

public interface Route extends SubRoute{
     Set<ContextKey<?>> getMandatoryInputData();

     default DebugHandler getDebugHandler(){
          return null;
     }
}
