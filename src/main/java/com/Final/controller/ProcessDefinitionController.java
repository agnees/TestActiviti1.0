package com.Final.controller;

import com.Final.model.PageBean;
import com.Final.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by tb on 2016/10/30.
 */
@Controller
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionController.class);

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    /**
     * 流程定义查询
     *
     * @param page
     * @param rows
     * @param
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, String s_name, HttpServletResponse response) throws Exception {

        try {
            if (s_name == null) {
                s_name = "";
            }

            PageBean pageBean = new PageBean();
            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }
            //  PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
            List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().
                    orderByProcessDefinitionKey().desc().
                    processDefinitionNameLike("%" + s_name + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            long total = repositoryService.createProcessDefinitionQuery().processDefinitionNameLike("%" + s_name + "%").count(); //查询名字像这个总数
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(new String[]{"identityLinks","processDefinition"});
            JSONObject result=new JSONObject();
            JSONArray jsonArray=JSONArray.fromObject(processDefinitionList,jsonConfig);
            result.put("rows", jsonArray);
            result.put("total", total);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("查询流程失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 查看流程图
     *
     * @param deploymentId
     * @param diagramResourceName
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showView")
    public String showView(String deploymentId, String diagramResourceName, HttpServletResponse response) throws Exception {

        try {
            InputStream in = repositoryService.getResourceAsStream(deploymentId, diagramResourceName);
            OutputStream out = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("查询流程图失败" + e.getMessage());
        }
        return null;
    }


    /**
     * 根据任务id显示流程图
     *
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showViewByTaskId")
    public String showViewByTaskId(String taskId, HttpServletResponse response) throws Exception {
        try {
            HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
            ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            InputStream in = repositoryService.getResourceAsStream(pd.getDeploymentId(),pd.getDiagramResourceName());
            OutputStream out = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            logger.error("查询流程图失败" + e.getMessage());
        }

        return null;
    }
}
