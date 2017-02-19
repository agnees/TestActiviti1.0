package com.Final.controller;


import com.Final.model.Group;
import com.Final.model.PageBean;
import com.Final.service.GroupService;
import com.Final.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/29.
 */
@Controller
@RequestMapping("/group")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);
    @Resource
    private GroupService groupService;

    /**
     * 查询所有角色  下拉框用到
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/groupComboList")
    public String groupComboList(HttpServletResponse response) {

        JSONObject object = new JSONObject();

        object.put("id", -1);
        object.put("name", "请选择角色..");
        JSONArray array = new JSONArray();
        array.add(object);
        try {
            List<Group> list = groupService.find(null);
            JSONArray jsonArray = JSONArray.fromObject(list);
            array.addAll(jsonArray);

            ResponseUtil.write(response, array);
        } catch (Exception e) {
            logger.error("对不起 发生未知错误" + e.getMessage());
        }
        return null;
    }


    /**
     * 分页条件查询角色
     *
     * @param page
     * @param rows
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "rows", required = false) String rows, HttpServletResponse response) throws Exception {
        try {
            PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("start", pageBean.getStart());
            map.put("size", pageBean.getPageSize());
            List<Group> result = groupService.find(map);
            Long total = groupService.getTotal(map);
            JSONObject object = new JSONObject();
            JSONArray jsonArray = JSONArray.fromObject(result);
            object.put("total", total);
            object.put("rows", jsonArray);
            ResponseUtil.write(response, object);
        } catch (Exception e) {
            logger.error("分页查询失败" + e.getMessage());
        }

        return null;
    }


    /**
     * 添加或者修角色
     *
     * @param group
     * @param response
     * @param flag     1 添加  2修改
     * @return
     * @throws Exception
     */
    @RequestMapping("/save")
    public String save(Group group, HttpServletResponse response, Integer flag) throws Exception {

        int resultTotal = 0;//记载操作数

        try {
            if (flag == 1) {
                resultTotal = groupService.add(group);
            } else {
                resultTotal = groupService.update(group);
            }

            JSONObject object = new JSONObject();
            if (resultTotal > 0) {
                object.put("success", true);
            } else {
                object.put("success", false);
            }
            ResponseUtil.write(response, object);
        } catch (Exception e) {
            logger.error("添加或修改失败" + e.getMessage());
        }

        return null;
    }


    /**
     * 删除角色
     *
     * @param ids
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            groupService.delete(idsStr[i]);
        }
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }


    /**
     * 判断是否存在指定角色名
     *
     * @param groupName
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/existGroupName")
    public String existUserName(String groupName, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        if (groupService.findById(groupName) == null) {
            result.put("exist", false);
        } else {
            result.put("exist", true);
        }
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 查询所有角色
     *
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/listAllGroups")
    public String listAllGroups(HttpServletResponse response) throws Exception {
        List<Group> groupList = groupService.find(null);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(groupList);
        result.put("groupList", jsonArray);
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 通过用户Id查询角色集合
     *
     * @param userId
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/findGroupsByUserId")
    public String findGroupsByUserId(String userId, HttpServletResponse response) throws Exception {

        try {
            List<Group> groups = groupService.findByUserId(userId);
            StringBuffer sb = new StringBuffer();
            for (Group group : groups) {
                if (group != null) {
                    sb.append(group.getId() + ",");
                }
            }
            JSONObject result=new JSONObject();
            if(sb.length()>0){
                result.put("groups", sb.deleteCharAt(sb.length()-1).toString());
            }else{
                result.put("groups", groups.toString());
            }
            ResponseUtil.write(response, result);
        }catch (Exception e){
            logger.error("通过用户Id查询角色集合"+e.getMessage());
        }
        return null;
    }



}