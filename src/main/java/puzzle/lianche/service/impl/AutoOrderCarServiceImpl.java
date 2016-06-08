package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoOrderCar;
import puzzle.lianche.service.IAutoOrderCarService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoOrderCarService")
public class AutoOrderCarServiceImpl implements IAutoOrderCarService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoOrderCar entity){
		return sqlMapper.insert("AutoOrderCarMapper.insert", entity);
	}

    /**
     * 批量插入记录
     */
    @Override
    public boolean insertBatch(List<AutoOrderCar> list) {
        return sqlMapper.insert("AutoOrderCarMapper.insertBatch", list);
    }

    /**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoOrderCar entity){
    	return sqlMapper.update("AutoOrderCarMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoOrderCarMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoOrderCar query(Map<String, Object> map){
    	return sqlMapper.query("AutoOrderCarMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoOrderCar> queryList(Object param){
    	return sqlMapper.queryList("AutoOrderCarMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoOrderCar> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoOrderCarMapper.queryList", map, page);
    }
    
}
