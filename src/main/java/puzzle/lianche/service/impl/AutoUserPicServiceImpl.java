package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoUserPic;
import puzzle.lianche.service.IAutoUserPicService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoUserPicService")
public class AutoUserPicServiceImpl implements IAutoUserPicService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoUserPic entity){
		return sqlMapper.insert("AutoUserPicMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoUserPic entity){
    	return sqlMapper.update("AutoUserPicMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoUserPicMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoUserPic query(Map<String, Object> map){
    	return sqlMapper.query("AutoUserPicMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoUserPic> queryList(Object param){
    	return sqlMapper.queryList("AutoUserPicMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoUserPic> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoUserPicMapper.queryList", map, page);
    }
    
}
