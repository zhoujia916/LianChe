package puzzle.lianche.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.Constants;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemUser;
import puzzle.lianche.entity.SystemUserMap;
import puzzle.lianche.service.ISystemAuthorityService;
import puzzle.lianche.service.ISystemLogService;
import puzzle.lianche.service.ISystemUserMapService;
import puzzle.lianche.service.ISystemUserService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;

@Service("systemUserService")
public class SystemUserServiceImpl extends BaseServiceImpl implements ISystemUserService {

    @Autowired
    private ISystemUserMapService systemUserMapService;

    @Autowired
    private ISystemLogService systemLogService;

    @Autowired
    private ISystemAuthorityService systemAuthorityService;

    private static Logger logger = LoggerFactory.getLogger(SystemUserServiceImpl.class);

    /**
     * 修改用户信息时比较查看role、group、dept
     * @param userId
     * @param targetIds
     * @param targetType
     */
    public void updateSystemUserMap(Integer userId,String[] targetIds,Integer targetType) {
        try {
            List<SystemUserMap> addList = new ArrayList<SystemUserMap>();
            List<Integer> delList = new ArrayList<Integer>();
            Map<String, Object> map = new HashMap<String, Object>();
            Integer i=0;
            Integer j=0;
            //region handle role
            map.put("userId",userId);
            map.put("targetType",targetType);
            List<SystemUserMap> oldList = systemUserMapService.queryList(map);
            for (String targetId : targetIds) {
                boolean find = false;
                //比较要改变的和原来的数据。如果存在不执行操作，反之新增
                for (SystemUserMap systemUserMap : oldList) {
                    if (targetId.equalsIgnoreCase(systemUserMap.getTargetId().toString())) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    if(i!=ConvertUtil.toInt(targetId)){
                        SystemUserMap systemUserMap = new SystemUserMap();
                        systemUserMap.setTargetId(ConvertUtil.toInt(targetId));
                        systemUserMap.setTargetType(targetType);
                        systemUserMap.setUserId(userId);
                        addList.add(systemUserMap);
                        i=ConvertUtil.toInt(targetId);
                    }
                }
            }
            //比较原来的和现在的，如果原来的跟现在的不相符就将原来的删除
            for (SystemUserMap systemUserMap : oldList) {
                boolean find = false;
                for (String role : targetIds) {
                    if (role.equalsIgnoreCase(systemUserMap.getTargetId().toString())) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    if(j!=systemUserMap.getMapId()){
                        delList.add(systemUserMap.getMapId());
                        j=systemUserMap.getMapId();
                    }
                }
            }
            //endregion
            //region 更新用户映射表
            if (delList.size() > 0) {
                map.clear();
                map.put("mapIds",delList);
                systemUserMapService.delete(map);
            }
            if(addList.size()>0){
                systemUserMapService.insertBatch(addList);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 给用户授角色、用户组、部门
     * @param systemUser
     */
    public boolean insertUserMap(SystemUser systemUser){
        try{
            List<SystemUserMap> addList = new ArrayList<SystemUserMap>();
            SystemUserMap userMap;
            //给用户授角色
            if(systemUser.getRoleId()!=null && systemUser.getRoleId()!=""){
                String[] roles=systemUser.getRoleId().split(",");
                for(String roleId:roles){
                    userMap=new SystemUserMap();
                    userMap.setTargetId(ConvertUtil.toInt(roleId));
                    userMap.setTargetType(Constants.USER_MAP_TARGET_TYPE_ROLE);
                    userMap.setUserId(systemUser.getUserId());
                    addList.add(userMap);
                }
            }
            if(systemUser.getGroupId()!=null && systemUser.getGroupId()!=""){
                //给用户授用户组
                String[] groups=systemUser.getGroupId().split(",");
                for(String groupId:groups){
                    userMap=new SystemUserMap();
                    userMap.setTargetId(ConvertUtil.toInt(groupId));
                    userMap.setTargetType(Constants.USER_MAP_TARGET_TYPE_GROUP);
                    userMap.setUserId(systemUser.getUserId());
                    addList.add(userMap);
                }
            }
            if(systemUser.getDeptId()!=null && systemUser.getDeptId()!=""){
                //给用户授部门
                String[] depts=systemUser.getDeptId().split(",");
                for(String deptId:depts){
                    userMap=new SystemUserMap();
                    userMap.setTargetId(ConvertUtil.toInt(deptId));
                    userMap.setTargetType(Constants.USER_MAP_TARGET_TYPE_DEPT);
                    userMap.setUserId(systemUser.getUserId());
                    addList.add(userMap);
                }
            }
            if(addList.size()>0){
                systemUserMapService.insertBatch(addList);
            }
            return true;
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * 删除用户相关信息
     * @param map
     */
    public void deleteUserRelated(Map map){
        //删除system_log表中该用户所产生的日志
        systemLogService.delete(map);
        //删除system_user_map该用户的相关信息
        systemUserMapService.delete(map);
    }

    /**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemUser entity){
        try{
            if(sqlMapper.insert("SystemUserMapper.insert", entity)){
                return insertUserMap(entity);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return  false;
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemUser entity){
        try{
            if(sqlMapper.update("SystemUserMapper.update", entity)){
                if(entity.getRoleId()!=null && entity.getRoleId()!=""){
                    String[] roles=entity.getRoleId().split(",");
                    updateSystemUserMap(entity.getUserId(),roles,Constants.USER_MAP_TARGET_TYPE_ROLE);
                }
                if(entity.getGroupId()!=null && entity.getGroupId()!=""){
                    String[] groups=entity.getGroupId().split(",");
                    updateSystemUserMap(entity.getUserId(),groups,Constants.USER_MAP_TARGET_TYPE_GROUP);
                }
                if(entity.getDeptId()!=null && entity.getDeptId()!="") {
                    String[] depts=entity.getDeptId().split(",");
                    updateSystemUserMap(entity.getUserId(),depts,Constants.USER_MAP_TARGET_TYPE_DEPT);
                }
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
        try{
            if(sqlMapper.delete("SystemUserMapper.delete", map)){
                //删除用户相关信息
                deleteUserRelated(map);
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    	return false;
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemUser query(Map<String, Object> map){
    	return sqlMapper.query("SystemUserMapper.query", map);
    }

    /**
	* 查询多条记录
	*/
	@Override
    public List<SystemUser> queryList(Object param){
    	return sqlMapper.queryList("SystemUserMapper.queryList", param);
    }

    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemUser> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemUserMapper.queryList", map, page);
    }

    /**
     * 查询授权信息
     */
    @Override
    public List<SystemAuthority> queryAuthority(int userId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("status", Constants.SYSTEM_MENU_STATUS_VISIBLE);
        return systemAuthorityService.queryList(map);
    }
}
