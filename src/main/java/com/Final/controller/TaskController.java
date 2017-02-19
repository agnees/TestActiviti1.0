package com.Final.controller;

import com.Final.model.*;
import com.Final.service.LeaveService;
import com.Final.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.*;
import org.activiti.engine.delegate.event.impl.ActivitiEventImpl;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by tb on 2016/10/30.
 */
@Controller
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private TaskService taskService;

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private FormService formService;

    @Autowired
    private HistoryService historyService;

    /**
     * 根据用户id分页查询任务
     *
     * @param page
     * @param rows
     * @param userId   用户id
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, String s_taskName, String userId, HttpServletResponse response) throws Exception {
        if (s_taskName == null) {
            s_taskName = "";
        }
        try {


            PageBean pageBean = new PageBean();
            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }

            //创建任务查询
            //根据用户Id查询
            //根据任务名称模糊查询
            //分页
            List<Task> taskList = taskService.createTaskQuery().
                    taskCandidateUser(userId).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            List<MyTask> tasks = new ArrayList<MyTask>(30);
            for (Task task : taskList) {
                MyTask t = new MyTask();
                t.setId(task.getId());
                t.setName(task.getName());
                t.setCreateTime(task.getCreateTime());
                tasks.add(t);
            }
            long total = taskService.createTaskQuery().taskCandidateUser(userId).taskNameLike("%" + s_taskName + "%").count();
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(tasks, jsonConfig);
            result.put("rows", jsonArray);
            result.put("total", total);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("任务查询失败" + e.getMessage());
        }

        return null;
    }


    /**
     * 流程实例已经走完的历史任务
     *
     * @param page
     * @param rows
     * @param s_taskName
     * @param groupId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/finishedList")
    public String finishedList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, String s_taskName, String groupId, HttpServletResponse response) throws Exception {
        if (s_taskName == null) {
            s_taskName = "";
        }

        PageBean pageBean = new PageBean();

        try {


            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }

            // 创建历史任务实例查询
            // 根据角色查询
            // 根据任务名称模糊查询
            // 返回带分页的结果集合
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateGroup(groupId).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            List<MyTask> myTasks = new ArrayList<MyTask>(30);
            int t = 0;
            for (HistoricTaskInstance hit : historicTaskInstances) {
                if (taskService.createTaskQuery().processInstanceId(hit.getProcessInstanceId()).singleResult() == null) {
                    MyTask myTask = new MyTask();
                    myTask.setId(hit.getId());
                    myTask.setName(hit.getName());
                    myTask.setEndTime(hit.getEndTime());
                    myTask.setCreateTime(hit.getCreateTime());
                    myTasks.add(myTask);
                } else {
                    t++;
                }

            }
            long total = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateGroup(groupId).
                    taskNameLike("%" + s_taskName + "%").count();
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(myTasks, jsonConfig);
            result.put("rows", jsonArray);
            result.put("total", total - t);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("查询完成任务失败" + e.getMessage());
        }
        return null;
    }


    /**
     * 流程实例已经走完的历史任务
     *
     * @param page
     * @param rows
     * @param s_taskName
     * @param groupId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/unFinishedList")
    public String unFinishedList(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, String s_taskName, String groupId, String userId, HttpServletResponse response) throws Exception {
        if (s_taskName == null) {
            s_taskName = "";
        }

        PageBean pageBean = new PageBean();

        try {


            if (page != null) {
                pageBean.setPage(Integer.parseInt(page));
            }

            if (rows != null) {
                pageBean.setPageSize(Integer.parseInt(rows));
            }

            // 创建历史任务实例查询
            // 根据角色查询
            // 根据任务名称模糊查询
            // 返回带分页的结果集合
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateGroup(groupId).
                    taskNameLike("%" + s_taskName + "%").
                    listPage(pageBean.getStart(), pageBean.getPageSize());
            List<MyTask> myTasks = new ArrayList<MyTask>(30);
            int t = 0;
            for (HistoricTaskInstance hit : historicTaskInstances) {
                // 如果该任务对应的流程实例在运行时任务表里查询到，说明就是这个流程实例未走完  并且用用户id以及任务id在运行时候任务表里查询不到结果  才算是已办任务
                //因为可能是任务刚分配给了别人
                if ( (taskService.createTaskQuery().processInstanceId(hit.getProcessInstanceId()).singleResult() != null)
                        && (taskService.createTaskQuery().taskCandidateUser(userId).taskId(hit.getId()).list().size() == 0)) {
                    MyTask myTask = new MyTask();
                    myTask.setId(hit.getId());
                    myTask.setName(hit.getName());
                    myTask.setEndTime(hit.getEndTime());
                    myTask.setCreateTime(hit.getCreateTime());
                    myTasks.add(myTask);
                } else {
                    t++;
                }

            }
            long total = historyService.createHistoricTaskInstanceQuery().
                    taskCandidateGroup(groupId).
                    taskNameLike("%" + s_taskName + "%").count();
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(myTasks, jsonConfig);
            result.put("rows", jsonArray);
            result.put("total", total - t);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("查询完成任务失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 查询当前流程图
     *
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showCurrentView")
    public ModelAndView showCurrentView(String taskId, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // 通过任务id查询流程定义
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        String processDefinitionId = task.getProcessDefinitionId(); //获取流程定义Id
        // 创建流程定义查询
        // 根据流程定义id查询
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        mav.addObject("deploymentId", processDefinition.getDeploymentId()); // 部署id
        mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称
        // 查看当前活动坐标
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
        // 根据流程实例id查询活动实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().
                processInstanceId(processInstanceId).singleResult();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(processInstance.getActivityId());
        mav.addObject("x", activityImpl.getX());
        mav.addObject("y", activityImpl.getY());
        mav.addObject("width", activityImpl.getWidth());
        mav.addObject("height", activityImpl.getHeight());
        mav.setViewName("page/currentView");
        return mav;
    }

    /**
     * 查询当前流程图 待办用到
     *
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/showHisCurrentView")
    public ModelAndView showHisCurrentView(String taskId, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String processDefinitionId = historicTaskInstance.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        mav.addObject("deploymentId", processDefinition.getDeploymentId()); // 部署id
        mav.addObject("diagramResourceName", processDefinition.getDiagramResourceName()); // 图片资源文件名称

        // 查看当前活动坐标
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        String processInstanceId = historicTaskInstance.getProcessInstanceId(); // 获取流程实例id
        // 根据流程实例id查询活动实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(processInstance.getActivityId());
        mav.addObject("x", activityImpl.getX());
        mav.addObject("y", activityImpl.getY());
        mav.addObject("width", activityImpl.getWidth());
        mav.addObject("height", activityImpl.getHeight());
        mav.setViewName("page/currentView");
        return mav;
    }


    /**
     * 重定向审核处理页面
     *
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/redirectPage")
    public String redirectPage(String taskId, HttpServletResponse response) throws Exception {
        TaskFormData formData = formService.getTaskFormData(taskId);
        String url = formData.getFormKey();
        JSONObject result = new JSONObject();
        result.put("url", url);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 班长审批
     *
     * @param taskId    任务id
     * @param leaveDays 请假天数
     * @param comment   批注信息
     * @param state     审核状态 1 通过 or 2 驳回
     * @param response
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/audit_bz")
    public String audit_bz(String taskId, Integer leaveDays, String comment, Integer state, HttpServletResponse response, HttpSession session) throws Exception {

        // 通过任务id查询流程定义
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        if (state == 1) {
            variables.put("msg", "通过");
        } else if (state == 2) {
            Integer leaveId = (Integer) taskService.getVariable(taskId, "leaveId");
            Leave leave = leaveService.findById(leaveId);
            leave.setState("审核未通过");
            leaveService.update(leave);
            variables.put("msg", "未通过");
        }

        variables.put("days", leaveDays);   //设置请假时间
        // 获取流程实例id
        String processInstacnId = task.getProcessInstanceId();
        MemberShip currentMemberShip = (MemberShip) session.getAttribute("currentMemberShip");
        User currentUser = currentMemberShip.getUser();
        Group currentGroup = currentMemberShip.getGroup();
        Authentication.setAuthenticatedUserId(currentUser.getFirstName() + currentUser.getLastName() + "【" + currentGroup.getName() + "】");// 设置用户id
        taskService.addComment(taskId, processInstacnId, comment);

        taskService.complete(taskId,variables);     //记得把流程变量也带上 要不然绝对报错
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }


    /**
     * 领导审批
     *
     * @param taskId    任务id
     * @param leaveDays 请假天数
     * @param comment   批注信息
     * @param state     审核状态 1 通过 or 2 驳回
     * @param response
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping("/audit_ld")
    public String audit_ld(String taskId, Integer leaveDays, String comment, Integer state, HttpServletResponse response, HttpSession session) throws Exception {
        // 通过任务id查询流程定义
        Task task=taskService.createTaskQuery() // 创建任务查询
                .taskId(taskId) // 根据任务id查询
                .singleResult();
        Map<String, Object> variables=new HashMap<String,Object>();
        Integer leaveId=(Integer) taskService.getVariable(taskId, "leaveId"); // 通过任务id获取请假单id
        Leave leave=leaveService.findById(leaveId);
        if(state == 1){
            leave.setState("审核通过");
            leaveService.update(leave); // 更新审核信息
        }else if(state==2){
            leave.setState("审核未通过");
            leaveService.update(leave); // 更新审核信息
        }
        String processInstanceId = task.getProcessInstanceId(); // 获取流程实例id
        MemberShip currentMemberShip=(MemberShip)session.getAttribute("currentMemberShip");
        User currentUser=currentMemberShip.getUser();
        Group currentGroup=currentMemberShip.getGroup();
        Authentication.setAuthenticatedUserId(currentUser.getFirstName()+currentUser.getLastName()+"【"+currentGroup.getName()+"】");// 设置用户id
        taskService.addComment(taskId, processInstanceId, comment); // 添加批注信息
        taskService.complete(taskId, variables); // 完成任务
        JSONObject result=new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 历史批注
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/listHistoryComment")
    public String listHistoryComment(String taskId,HttpServletResponse response)throws Exception {
        if (taskId == null) {
            return null;
        }
        HistoricTaskInstance historicTaskInstance =historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
        String processInstanceId =historicTaskInstance.getProcessInstanceId();
        List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId);
        //元素反转
        Collections.reverse(comments);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result=new JSONObject();
        JSONArray jsonArray=JSONArray.fromObject(comments,jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 历史批注 通过流程实例id
     * @param taskId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/listHistoryCommentWithProcessInstanceId")
    public String listHistoryCommentWithProcessInstanceId(String processInstanceId,HttpServletResponse response)throws Exception {
        if (processInstanceId == null) {
            return null;
        }
        List<Comment> comments =taskService.getProcessInstanceComments(processInstanceId);
        //元素反转
        Collections.reverse(comments);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
        JSONObject result=new JSONObject();
        JSONArray jsonArray=JSONArray.fromObject(comments,jsonConfig);
        result.put("rows", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }
}