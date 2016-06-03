package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemMsg;
import puzzle.lianche.service.ISystemMsgService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemMsgService")
public class SystemMsgServiceImpl implements ISystemMsgService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemMsg entity){
		return sqlMapper.insert("SystemMsgMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemMsg entity){
    	return sqlMapper.update("SystemMsgMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemMsgMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemMsg query(Map<String, Object> map){
    	return sqlMapper.query("SystemMsgMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemMsg> queryList(Object param){
    	return sqlMapper.queryList("SystemMsgMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemMsg> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemMsgMapper.queryList", map, page);
    }
    
}
