package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoUserProfile;
import puzzle.lianche.service.IAutoUserProfileService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoUserProfileService")
public class AutoUserProfileServiceImpl implements IAutoUserProfileService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoUserProfile entity){
		return sqlMapper.insert("AutoUserProfileMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoUserProfile entity){
    	return sqlMapper.update("AutoUserProfileMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoUserProfileMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoUserProfile query(Map<String, Object> map){
    	return sqlMapper.query("AutoUserProfileMapper.query", map);
    }

    @Override
    public AutoUserProfile query(Integer userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        return sqlMapper.query("AutoUserProfileMapper.query", map);
    }

    /**
	* 查询多条记录
	*/
	@Override
    public List<AutoUserProfile> queryList(Object param){
    	return sqlMapper.queryList("AutoUserProfileMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoUserProfile> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoUserProfileMapper.queryList", map, page);
    }
    
}
