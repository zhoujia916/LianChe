package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoBrandModel;
import puzzle.lianche.service.IAutoBrandModelService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoBrandModelService")
public class AutoBrandModelServiceImpl implements IAutoBrandModelService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoBrandModel entity){
		return sqlMapper.insert("AutoBrandModelMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoBrandModel entity){
    	return sqlMapper.update("AutoBrandModelMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoBrandModelMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoBrandModel query(Map<String, Object> map){
    	return sqlMapper.query("AutoBrandModelMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoBrandModel> queryList(Object param){
    	return sqlMapper.queryList("AutoBrandModelMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoBrandModel> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoBrandModelMapper.queryList", map, page);
    }
}
