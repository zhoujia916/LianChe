package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoAd;
import puzzle.lianche.service.IAutoAdService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoAdService")
public class AutoAdServiceImpl implements IAutoAdService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoAd entity){
		return sqlMapper.insert("AutoAdMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoAd entity){
    	return sqlMapper.update("AutoAdMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoAdMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoAd query(Map<String, Object> map){
    	return sqlMapper.query("AutoAdMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoAd> queryList(Object param){
    	return sqlMapper.queryList("AutoAdMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoAd> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoAdMapper.queryList", map, page);
    }
}
