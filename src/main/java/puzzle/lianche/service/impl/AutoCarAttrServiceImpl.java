package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoCarAttr;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoCarAttrService")
public class AutoCarAttrServiceImpl implements IAutoCarAttrService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoCarAttr entity){
		return sqlMapper.insert("AutoCarAttrMapper.insert", entity);
	}

    @Override
    public boolean insertBatch(List<AutoCarAttr> list) {
        return sqlMapper.insert("AutoCarAttrMapper.insertBatch", list);
    }

    /**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoCarAttr entity){
    	return sqlMapper.update("AutoCarAttrMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoCarAttrMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoCarAttr query(Map<String, Object> map){
    	return sqlMapper.query("AutoCarAttrMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoCarAttr> queryList(Object param){
    	return sqlMapper.queryList("AutoCarAttrMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoCarAttr> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoCarAttrMapper.queryList", map, page);
    }
    
}
