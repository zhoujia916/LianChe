package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoRegion;
import puzzle.lianche.service.IAutoRegionService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoRegionService")
public class AutoRegionServiceImpl implements IAutoRegionService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoRegion entity){
		return sqlMapper.insert("AutoRegionMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoRegion entity){
    	return sqlMapper.update("AutoRegionMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoRegionMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoRegion query(Map<String, Object> map){
    	return sqlMapper.query("AutoRegionMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoRegion> queryList(Object param){
    	return sqlMapper.queryList("AutoRegionMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoRegion> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoRegionMapper.queryList", map, page);
    }
    
}
