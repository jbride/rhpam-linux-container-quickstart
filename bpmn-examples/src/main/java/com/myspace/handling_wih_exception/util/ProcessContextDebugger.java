package com.myspace.handling_wih_exception.util;

import org.kie.api.runtime.process.ProcessContext;

public class ProcessContextDebugger {

    public static void viewProcessContext(ProcessContext pContext) {
        System.out.println("viewProcessContext() pContext = "+pContext);
    }
    
}
