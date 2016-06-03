package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemLog;
import puzzle.lianche.service.ISystemLogService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemLogService")
public class SystemLogServiceImpl implements ISystemLogService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemLog entity){
		return sqlMapper.insert("SystemLogMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemLog entity){
    	return sqlMapper.update("SystemLogMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemLogMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemLog query(Map<String, Object> map){
    	return sqlMapper.query("SystemLogMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemLog> queryList(Object param){
    	return sqlMapper.queryList("SystemLogMapper.queryAll", param);
    }

    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemLog> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemLogMapper.queryList", map, page);
    }

}
