package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoArticle;
import puzzle.lianche.service.IAutoArticleService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoArticleService")
public class AutoArticleServiceImpl implements IAutoArticleService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoArticle entity){
		return sqlMapper.insert("AutoArticleMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoArticle entity){
    	return sqlMapper.update("AutoArticleMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoArticleMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoArticle query(Map<String, Object> map){
    	return sqlMapper.query("AutoArticleMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoArticle> queryList(Object param){
    	return sqlMapper.queryList("AutoArticleMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoArticle> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoArticleMapper.queryList", map, page);
    }
}
