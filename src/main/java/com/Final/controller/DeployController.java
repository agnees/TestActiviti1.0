package com.Final.controller;

import com.Final.model.PageBean;
import com.Final.util.ResponseUtil;
import com.sun.tools.internal.xjc.reader.gbind.ElementSets;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.activiti.engine.RepositoryService;

import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * Created by tb on 2016/10/30.
 */

@Controller
@RequestMapping("/deploy")
public class DeployController {

    private static final Logger logger = LoggerFactory.getLogger(DeployController.class);
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 上传流程部署文件
     *
     * @param deployFile
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/deploy")
    public String deploy(@RequestParam("deployFile") MultipartFile deployFile, HttpServletResponse response) throws Exception {

        try {
            repositoryService.createDeployment().
                    name(deployFile.getOriginalFilename()). //部署程序的名字
                    addZipInputStream(new ZipInputStream(deployFile.getInputStream())) //添加zipinputstream  new一个就好
                    .deploy();
            JSONObject result = new JSONObject();
            result.put("success", true);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("部署流程失败" + e.getMessage());
        }

        return null;
    }

    /**
     * 流程部署查询
     *
     * @param page
     * @param rows
     * @param name
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
            List<Deployment> deploymentList = repositoryService.createDeploymentQuery().   //创建流程部署查询
                    orderByDeploymentName().desc().         //根据部署时间降序排列
                    deploymentNameLike("%" + s_name + "%").     //根据流程部署名称模糊查询
                    listPage(pageBean.getStart(), pageBean.getPageSize());   //分页查询

            long total = repositoryService.createDeploymentQuery().deploymentNameLike("%" + s_name + "%").count();  //查询名字像这个总数
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setExcludes(new String[]{"resources"});
            jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor("yyyy-MM-dd hh:mm:ss"));
            JSONObject result = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(deploymentList, jsonConfig);
            result.put("rows", jsonArray);
            result.put("total", total);
            ResponseUtil.write(response, result);
        } catch (Exception e) {
            logger.error("查询流程失败" + e.getMessage());
        }
        return null;
    }

    /**
     * 删除流程部署
     *
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {

        String[] strids = ids.split(",");

        for (String str : strids) {
            if (str != null) {
                repositoryService.deleteDeployment(str, true);   //是否级联删除
            }
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);

        return null;
    }

}