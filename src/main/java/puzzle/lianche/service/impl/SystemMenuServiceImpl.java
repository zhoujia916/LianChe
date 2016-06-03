package puzzle.lianche.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.Constants;
import puzzle.lianche.entity.SystemMenu;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.ISystemMenuService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.utils.StringUtil;

@Service("systemMenuService")
public class SystemMenuServiceImpl implements ISystemMenuService {
	
	@Autowired
	private SqlMapper sqlMapper;
    @Autowired
    private SystemMenuActionServiceImpl systemMenuActionService;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemMenu entity){
        try {
            if(sqlMapper.insert("SystemMenuMapper.insert", entity)) {
                if (StringUtil.isNotNullOrEmpty(entity.getActionCode())) {
                    String[] array = entity.getActionCode().split(",");
                    List<SystemMenuAction> list = new ArrayList<SystemMenuAction>();
                    for (int index = 0; index < array.length; index++) {
                        SystemMenuAction menuAction = new SystemMenuAction();
                        menuAction.setActionCode(array[index]);
                        menuAction.setActionName(Constants.MAP_ACTION.get(array[index]).toString());
                        menuAction.setMenuId(entity.getMenuId());
                        menuAction.setSortOrder(index);
                        menuAction.setActionConfig("");
                        list.add(menuAction);
                    }
                    systemMenuActionService.insertBatch(list);
                }
                return true;
            }
        }
        catch (Exception e){
        }
        return false;
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemMenu entity){
        try {
            if(sqlMapper.update("SystemMenuMapper.update", entity)) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("menuId", entity.getMenuId());
                systemMenuActionService.delete(map);


                List<SystemMenuAction> oldList = systemMenuActionService.queryList(map);
                List<SystemMenuAction> addList = new ArrayList<SystemMenuAction>();
                List<Integer> ids = new ArrayList<Integer>();

                if(oldList != null && oldList.size() > 0) {
                    if (StringUtil.isNotNullOrEmpty(entity.getActionCode())) {
                        String[] array = entity.getActionCode().split(",");
                        for (int index = 0; index < array.length; index++) {
                            boolean find = false;
                            for(SystemMenuAction item : oldList){
                                if(item.getActionCode().equalsIgnoreCase(array[index])){
                                    find = true;
                                    break;
                                }
                            }
                            if(!find){
                                SystemMenuAction menuAction = new SystemMenuAction();
                                menuAction.setActionCode(array[index]);
                                menuAction.setActionName(Constants.MAP_ACTION.get(array[index]).toString());
                                menuAction.setMenuId(entity.getMenuId());
                                menuAction.setSortOrder(index);
                                menuAction.setActionConfig("");
                                addList.add(menuAction);
                            }
                        }
                        for(SystemMenuAction item : oldList){
                            boolean find = false;
                            for (int index = 0; index < array.length; index++) {
                                if(item.getActionCode().equalsIgnoreCase(array[index])){
                                    find = true;
                                    break;
                                }
                            }
                            if(!find){
                                ids.add(item.getActionId());
                            }
                        }
                    } else {
                        for(SystemMenuAction item : oldList){
                            ids.add(item.getActionId());
                        }
                    }
                }else{
                    if (StringUtil.isNotNullOrEmpty(entity.getActionCode())) {
                        String[] array = entity.getActionCode().split(",");
                        List<SystemMenuAction> list = new ArrayList<SystemMenuAction>();
                        for (int index = 0; index < array.length; index++) {
                            SystemMenuAction menuAction = new SystemMenuAction();
                            menuAction.setActionCode(array[index]);
                            menuAction.setActionName(Constants.MAP_ACTION.get(array[index]).toString());
                            menuAction.setMenuId(entity.getMenuId());
                            menuAction.setSortOrder(index);
                            menuAction.setActionConfig("");
                            list.add(menuAction);
                        }
                    }
                }
                if(addList.size() > 0){
                    systemMenuActionService.insertBatch(addList);
                }
                if(ids.size() > 0){
                    map.clear();
                    map.put("actionIds", StringUtil.concat(ids, ","));
                    systemMenuActionService.delete(map);
                }

                return true;
            }
        }
        catch (Exception e){

        }
        return false;
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemMenuMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemMenu query(Map<String, Object> map){
    	return sqlMapper.query("SystemMenuMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemMenu> queryList(Object param){
    	return sqlMapper.queryList("SystemMenuMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemMenu> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemMenuMapper.queryList", map, page);
    }
}
