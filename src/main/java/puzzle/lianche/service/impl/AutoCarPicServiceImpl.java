package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoCarPic;
import puzzle.lianche.service.IAutoCarPicService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoCarPicService")
public class AutoCarPicServiceImpl implements IAutoCarPicService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoCarPic entity){
		return sqlMapper.insert("AutoCarPicMapper.insert", entity);
	}

    /**
     * 批量插入记录
     */
    @Override
    public boolean insertBatch(List<AutoCarPic> list){
        return sqlMapper.insert("AutoCarPicMapper.insertBatch", list);
    }


    /**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoCarPic entity){
    	return sqlMapper.update("AutoCarPicMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoCarPicMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoCarPic query(Map<String, Object> map){
    	return sqlMapper.query("AutoCarPicMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoCarPic> queryList(Object param){
    	return sqlMapper.queryList("AutoCarPicMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoCarPic> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoCarPicMapper.queryList", map, page);
    }
    
}
