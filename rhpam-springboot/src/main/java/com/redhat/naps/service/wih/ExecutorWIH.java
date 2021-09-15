package com.redhat.naps.service.wih;

import java.util.Calendar;

import com.redhat.naps.service.commands.AsyncSignalEventCommand;

//import org.jbpm.process.core.async.AsyncSignalEventCommand;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutorService;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component("ExecutorWIH")
public class ExecutorWIH implements WorkItemHandler {

    private final static Logger log = LoggerFactory.getLogger(ExecutorWIH.class);
    private static final String SIGNAL = "signal";
    private static final String DEPLOYMENT_ID = "deploymentId";

    private String defaultSignal = "testSignal";

    @Value("${com.redhat.naps.helloExecutorCommand.deploymentId}")
    private String defaultDeploymentId;

    @Autowired
    private ExecutorService eService;

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        String signal = defaultSignal;
        if(workItem.getParameter(SIGNAL) != null){
            signal = (String)workItem.getParameter(SIGNAL);
        }

        String deploymentId = defaultDeploymentId;
        if(workItem.getParameter(DEPLOYMENT_ID) != null){
            deploymentId = (String)workItem.getParameter(DEPLOYMENT_ID);
        }
        long pInstanceId = workItem.getProcessInstanceId();

        CommandContext ctx = new CommandContext();
        ctx.setData("DeploymentId", deploymentId);
        ctx.setData("ProcessInstanceId", workItem.getProcessInstanceId());
        ctx.setData("Signal", signal);
        ctx.setData("Event", null);
        Calendar commandStartTime = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        commandStartTime.add(Calendar.SECOND, 5);
        log.info("executeWorkItem() deploymentId = "+deploymentId+" pInstanceId = "+pInstanceId+" : signal = "+signal +" : trigger date = "+commandStartTime.getTime());

        eService.scheduleRequest(AsyncSignalEventCommand.class.getName(), commandStartTime.getTime(), ctx);
        manager.completeWorkItem(workItem.getId(), workItem.getParameters());
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        log.info("abortWorkItem()");
    }
    
}
