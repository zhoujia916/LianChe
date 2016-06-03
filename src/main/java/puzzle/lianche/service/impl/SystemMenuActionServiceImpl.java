package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.ISystemMenuActionService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemMenuActionService")
public class SystemMenuActionServiceImpl implements ISystemMenuActionService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemMenuAction entity){
		return sqlMapper.insert("SystemMenuActionMapper.insert", entity);
	}

    /**
     * 批量插入单条记录
     */
    @Override
    public boolean insertBatch(List<SystemMenuAction> list){
        return sqlMapper.insert("SystemMenuActionMapper.insertBatch", list);
    }

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemMenuAction entity){
    	return sqlMapper.update("SystemMenuActionMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemMenuActionMapper.delete", map);
    }



	/**
	* 查询单条记录
	*/
	@Override
    public SystemMenuAction query(Map<String, Object> map){
    	return sqlMapper.query("SystemMenuActionMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemMenuAction> queryList(Object param){
    	return sqlMapper.queryList("SystemMenuActionMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemMenuAction> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemMenuActionMapper.queryList", map, page);
    }
    
}
