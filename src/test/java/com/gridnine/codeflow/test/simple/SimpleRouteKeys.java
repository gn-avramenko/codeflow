/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.test.simple;

import com.gridnine.codeflow.api.ContextKey;

public class SimpleRouteKeys {
    public static ContextKey<String> SIMPLE_KEY = new ContextKey<>("simple-key", String.class);

    public static ContextKey<String> SIMPLE_KEY_2 = new ContextKey<>("simple-key-2", String.class);
}
