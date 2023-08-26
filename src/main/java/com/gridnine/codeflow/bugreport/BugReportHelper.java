/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.bugreport;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.Route;
import com.gridnine.codeflow.executor.FlowExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class BugReportHelper {
    public static <T extends Route> Context replay(Class<T> route, FlowExecutor executor, File bugReport) {
        Context context = new Context();
        BugReportData data;
        try (FileInputStream bis = new FileInputStream(bugReport);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            data = (BugReportData) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        context.setData(BugReportDebugHandler.CONTEXT_KEY, data);
        data.getInputData().forEach((key, value) -> context.setData((ContextKey) key, value));
        executor.execute(route, context);
        return context;
    }
}
