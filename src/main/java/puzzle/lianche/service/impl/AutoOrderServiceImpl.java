package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoOrderService")
public class AutoOrderServiceImpl implements IAutoOrderService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoOrder entity){
		return sqlMapper.insert("AutoOrderMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoOrder entity){
    	return sqlMapper.update("AutoOrderMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoOrderMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoOrder query(Map<String, Object> map){
    	return sqlMapper.query("AutoOrderMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoOrder> queryList(Object param){
    	return sqlMapper.queryList("AutoOrderMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoOrder> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoOrderMapper.queryList", map, page);
    }
    
}
