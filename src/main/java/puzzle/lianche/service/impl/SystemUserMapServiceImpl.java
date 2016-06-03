package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.SystemUserMap;
import puzzle.lianche.service.ISystemUserMapService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("systemUserMapService")
public class SystemUserMapServiceImpl implements ISystemUserMapService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemUserMap entity){
		return sqlMapper.insert("SystemUserMapMapper.insert", entity);
	}

    /**
    * 批量插入记录
    */
    @Override
    public boolean insertBatch(List<SystemUserMap> list){
        return sqlMapper.insert("SystemUserMapMapper.insertBatch", list);
    }

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(SystemUserMap entity){
    	return sqlMapper.update("SystemUserMapMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemUserMapMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemUserMap query(Map<String, Object> map){
    	return sqlMapper.query("SystemUserMapMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemUserMap> queryList(Object param){
    	return sqlMapper.queryList("SystemUserMapMapper.queryList", param);
    }



    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemUserMap> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemUserMapMapper.queryList", map, page);
    }
    
}
