package com.Final.controller;

import com.Final.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by tb on 2016/10/30.
 */
@Controller
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessInstanceController.class);
    @Autowired
    private HistoryService historyService;

    /**
     * 根据任务id查询改流程实例的执行过程
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAction")
    public String listAction(String taskId,HttpServletResponse response)throws Exception{
        if(taskId==null){
            return null;
        }
        try {
            HistoricTaskInstance historicTaskInstance=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            String pdId=historicTaskInstance.getProcessDefinitionId();
            List<HistoricActivityInstance> hlist = historyService.createHistoricActivityInstanceQuery().processDefinitionId(pdId).list();
            JsonConfig jsonConfig=new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result=new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(hlist,jsonConfig);
            result.put("rows",jsonArray);
            ResponseUtil.write(response,result);
        }catch (Exception e){
            logger.error("查询实例过程失败"+e.getMessage());
        }
        return null;
    }


}
