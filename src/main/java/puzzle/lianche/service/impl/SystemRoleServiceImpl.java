package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemRole;
import puzzle.lianche.service.ISystemRoleService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemRoleService")
public class SystemRoleServiceImpl implements ISystemRoleService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemRole entity){
		return sqlMapper.insert("SystemRoleMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemRole entity){
    	return sqlMapper.update("SystemRoleMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemRoleMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemRole query(Map<String, Object> map){
    	return sqlMapper.query("SystemRoleMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemRole> queryList(Object param){
    	return sqlMapper.queryList("SystemRoleMapper.queryList", param);
    }

    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemRole> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemRoleMapper.queryList", map, page);
    }
    
}
