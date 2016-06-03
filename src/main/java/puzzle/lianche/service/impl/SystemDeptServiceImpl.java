package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemDept;
import puzzle.lianche.service.ISystemDeptService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemDeptService")
public class SystemDeptServiceImpl implements ISystemDeptService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemDept entity){
		return sqlMapper.insert("SystemDeptMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemDept entity){
    	return sqlMapper.update("SystemDeptMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemDeptMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemDept query(Map<String, Object> map){
    	return sqlMapper.query("SystemDeptMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemDept> queryList(Object param){
    	return sqlMapper.queryList("SystemDeptMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemDept> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemDeptMapper.queryList", map, page);
    }
    
}
