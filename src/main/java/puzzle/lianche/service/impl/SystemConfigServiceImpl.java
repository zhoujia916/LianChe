package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemConfig;
import puzzle.lianche.service.ISystemConfigService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemConfigService")
public class SystemConfigServiceImpl implements ISystemConfigService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemConfig entity){
		return sqlMapper.insert("SystemConfigMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemConfig entity){
    	return sqlMapper.update("SystemConfigMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemConfigMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemConfig query(Map<String, Object> map){
    	return sqlMapper.query("SystemConfigMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemConfig> queryList(Object param){
    	return sqlMapper.queryList("SystemConfigMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemConfig> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemConfigMapper.queryList", map, page);
    }
    
}
