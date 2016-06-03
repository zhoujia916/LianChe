package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoCollect;
import puzzle.lianche.service.IAutoCollectService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoCollectService")
public class AutoCollectServiceImpl implements IAutoCollectService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoCollect entity){
		return sqlMapper.insert("AutoCollectMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoCollect entity){
    	return sqlMapper.update("AutoCollectMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoCollectMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoCollect query(Map<String, Object> map){
    	return sqlMapper.query("AutoCollectMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoCollect> queryList(Object param){
    	return sqlMapper.queryList("AutoCollectMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoCollect> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoCollectMapper.queryList", map, page);
    }
}
