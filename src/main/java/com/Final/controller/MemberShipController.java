package com.Final.controller;

import com.Final.model.Group;
import com.Final.model.MemberShip;
import com.Final.model.User;
import com.Final.service.MemberShipService;

import com.Final.util.ResponseUtil;
import com.Final.util.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tb on 2016/10/30.
 */
@Controller
@RequestMapping("/memberShip")
public class MemberShipController {

    private static final Logger logger = LoggerFactory.getLogger(MemberShipController.class);
    @Resource
    private MemberShipService memberShipService;


    /**
     * 更新用户权限（先删除，再批量添加）
     * @param userId
     * @param
     * @param response
     * @return
     * @throws Exception
     */

    @RequestMapping("/update")
    public String updateMemberShip( String userId, String groupIds, HttpServletResponse response){
        try{
            memberShipService.deleteAllGroupsByUserId(userId);
            if(StringUtil.isNotEmpty(groupIds)){
                String idsArr[]=groupIds.split(",");
                for(String groupId:idsArr){
                    User user=new User();user.setId(userId);
                    Group group=new Group();group.setId(groupId);
                    MemberShip memberShip=new MemberShip();
                    memberShip.setUser(user);
                    memberShip.setGroup(group);
                    memberShipService.add(memberShip);
                }
            }
            JSONObject result=new JSONObject();
            result.put("success", true);
            ResponseUtil.write(response, result);

        }catch (Exception e){
            logger.error("修改失败"+e.getMessage());
        }
        return null;
    }

}
