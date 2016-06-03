package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemUserGroup;
import puzzle.lianche.service.ISystemUserGroupService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemUserGroupService")
public class SystemUserGroupServiceImpl implements ISystemUserGroupService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemUserGroup entity){
		return sqlMapper.insert("SystemUserGroupMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemUserGroup entity){
    	return sqlMapper.update("SystemUserGroupMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemUserGroupMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemUserGroup query(Map<String, Object> map){
    	return sqlMapper.query("SystemUserGroupMapper.query", map);
    }

    /**
	* 查询多条记录
	*/
	@Override
    public List<SystemUserGroup> queryList(Object param){
    	return sqlMapper.queryList("SystemUserGroupMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemUserGroup> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemUserGroupMapper.queryList", map, page);
    }
    
}
