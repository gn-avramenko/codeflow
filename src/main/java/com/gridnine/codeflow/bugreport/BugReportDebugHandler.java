/*****************************************************************
 * Gridnine AB http://www.gridnine.com
 * Project: Codeflow
 *****************************************************************/

package com.gridnine.codeflow.bugreport;

import com.gridnine.codeflow.api.Context;
import com.gridnine.codeflow.api.ContextKey;
import com.gridnine.codeflow.api.DebugHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class BugReportDebugHandler implements DebugHandler {
    public final static ContextKey<BugReportData> CONTEXT_KEY = new ContextKey<>("bug-report-data", BugReportData.class);

    @Override
    public void beforeRouteStarts(Context ctx) throws Exception {
        if(ctx.getData(CONTEXT_KEY) == null) {
            BugReportData bugReportData = new BugReportData();
            ctx.setData(CONTEXT_KEY, bugReportData);
            ctx.getAllDataKeys().forEach(it -> bugReportData.getInputData().put(it, (Serializable) ctx.getData(it)));
        }
    }

    @Override
    public void afterRouteEnds(Context ctx) throws Exception {
        BugReportData data = ctx.getData(CONTEXT_KEY);
        File file = new File("temp/bugreport.dat");
        if(file.exists()){
            file.delete();
        }
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        try (FileOutputStream bos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(data);
        }
    }
}
