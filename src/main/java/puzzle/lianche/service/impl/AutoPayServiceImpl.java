package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoPay;
import puzzle.lianche.service.IAutoPayService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoPayService")
public class AutoPayServiceImpl implements IAutoPayService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoPay entity){
		return sqlMapper.insert("AutoPayMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoPay entity){
    	return sqlMapper.update("AutoPayMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoPayMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoPay query(Map<String, Object> map){
    	return sqlMapper.query("AutoPayMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoPay> queryList(Object param){
    	return sqlMapper.queryList("AutoPayMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoPay> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoPayMapper.queryList", map, page);
    }
    
}
