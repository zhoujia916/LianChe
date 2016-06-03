package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoAttr;
import puzzle.lianche.service.IAutoAttrService;
import puzzle.lianche.service.IAutoCarAttrService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoAttrService")
public class AutoAttrServiceImpl implements IAutoAttrService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    private static Logger logger = LoggerFactory.getLogger(AutoAttrServiceImpl.class);

    public void deleteAttrRelated(Map map){
        autoCarAttrService.delete(map);
    }

	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoAttr entity){
		return sqlMapper.insert("AutoAttrMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoAttr entity){
    	return sqlMapper.update("AutoAttrMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	try{
            if(sqlMapper.delete("AutoAttrMapper.delete", map)){
                deleteAttrRelated(map);
                return true;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoAttr query(Map<String, Object> map){
    	return sqlMapper.query("AutoAttrMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoAttr> queryList(Object param){
    	return sqlMapper.queryList("AutoAttrMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoAttr> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoAttrMapper.queryList", map, page);
    }

}
