package com.company.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jbpm.runtime.manager.impl.jpa.EntityManagerFactoryManager;
import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.jbpm.services.api.model.VariableDesc;
import org.kie.api.executor.CommandContext;
import org.kie.api.executor.ExecutorService;
import org.kie.internal.KieInternalServices;
import org.kie.internal.process.CorrelationKey;
import org.kie.internal.process.CorrelationKeyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/custom/processes")
public class ProcessEngineExtension {

    private final static Logger log = LoggerFactory.getLogger(ProcessEngineExtension.class);
    private CorrelationKeyFactory correlationKeyFactory = KieInternalServices.Factory.get().newCorrelationKeyFactory();

    @Autowired
    private ProcessService processService;

    @Autowired
    private RuntimeDataService rDataService;

    @Autowired
    private ExecutorService eService;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext(unitName = "org.jbpm.domain")
    EntityManager jbpmEM;

    @Value("${com.redhat.naps.bpmnexamples.deployment.id}")
    private String deploymentId;

    @Value("${com.redhat.naps.bpmnexamples.process.id}")
    private String processId;

    @Value("${com.redhat.naps.helloExecutorCommand.nextScheduleTimeAdd.millis}")
    private String nextScheduleTimeAdd;

    @PostConstruct
    public void init() {
        log.info("init() HelloExecutorCommand will execute the following # of millis: "+nextScheduleTimeAdd);
        System.setProperty("com.redhat.naps.helloExecutorCommand.nextScheduleTimeAdd.millis", nextScheduleTimeAdd);
        String commandName = "com.company.service.commands.HelloExecutorCommand";
        Calendar commandStartTime = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
        commandStartTime.add(Calendar.SECOND, 15);
        CommandContext ctx = new CommandContext();
        //eService.scheduleRequest(commandName, commandStartTime.getTime(), ctx);

        //EntityManager jbpmEM = EntityManagerFactoryManager.get().getOrCreate("org.jbpm.domain").createEntityManager();
        Query testQuery = jbpmEM.createNativeQuery("select count(status) from requestinfo");
        BigInteger jobCount = (BigInteger)testQuery.getSingleResult();
        log.info("init() current executor job count = "+jobCount.intValue());
    }

    // Example: curl -X POST "localhost:8080/custom/processes/startAndReturnVars?correlationKey=azra&input=HT" | jq .
    @Transactional
    @RequestMapping(value = "/startAndReturnVars", method = RequestMethod.POST, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public Map<String, Object> startProcessAndReturnVars(@RequestParam(name = "correlationKey")  String cKeyString, @RequestParam(name = "input")  String input) {

            CorrelationKey correlationKey = correlationKeyFactory.newCorrelationKey(cKeyString);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("input", input);
            
            TransactionTemplate template = new TransactionTemplate(transactionManager);
            return template.execute((TransactionStatus s) -> {
                Long pi = processService.startProcess(deploymentId, processId, correlationKey, parameters);
                Collection<VariableDesc> pVars = rDataService.getVariablesCurrentState(pi);
                Map<String, Object> pVarsMap = new HashMap<String, Object>();
                pVarsMap.put("pInstanceId", pi);
                pVarsMap.put("processId", processId);
                pVarsMap.put("deploymentId", deploymentId);
                for(VariableDesc pVar : pVars){
                    pVarsMap.put(pVar.getVariableId(), pVar.getNewValue());
                }
                log.debug("Started ProcessInstanceId = " + pi+" : correlationKey = "+correlationKey.getName());
                return pVarsMap;
            });
    }
    
}
