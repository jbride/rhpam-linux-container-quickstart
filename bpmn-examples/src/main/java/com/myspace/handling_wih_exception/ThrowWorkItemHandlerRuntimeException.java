package com.myspace.handling_wih_exception;

import org.jbpm.process.workitem.core.AbstractLogOrThrowWorkItemHandler;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemManager;


public class ThrowWorkItemHandlerRuntimeException extends AbstractLogOrThrowWorkItemHandler  {

    public static final String THROW_EXCEPTION = "throwException";
    
    
    public ThrowWorkItemHandlerRuntimeException() {
        super();
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        
        Object throwException = workItem.getParameter(THROW_EXCEPTION);
        System.out.println("throwException = "+throwException);
        if(throwException != null && Boolean.parseBoolean((String)throwException))
          throw new TestRuntimeException("throwing exception from workItem = "+workItem.getName());
        
        manager.completeWorkItem(workItem.getId(), workItem.getParameters());
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        System.out.println("abortingWorkItem name = "+workItem.getName());

    }

}
