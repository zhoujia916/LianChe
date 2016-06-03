package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoAdPosition;
import puzzle.lianche.service.IAutoAdPositionService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoAdPositionService")
public class AutoAdPositionServiceImpl implements IAutoAdPositionService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoAdPosition entity){
		return sqlMapper.insert("AutoAdPositionMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoAdPosition entity){
    	return sqlMapper.update("AutoAdPositionMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoAdPositionMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoAdPosition query(Map<String, Object> map){
    	return sqlMapper.query("AutoAdPositionMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoAdPosition> queryList(Object param){
    	return sqlMapper.queryList("AutoAdPositionMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoAdPosition> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoAdPositionMapper.queryList", map, page);
    }
}
