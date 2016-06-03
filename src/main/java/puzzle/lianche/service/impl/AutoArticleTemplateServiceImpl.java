package puzzle.lianche.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import puzzle.lianche.entity.AutoArticleTemplate;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.service.IAutoArticleTemplateService;
import puzzle.lianche.utils.Page;

import java.util.List;
import java.util.Map;

@Service("autoArticleTemplateService")
public class AutoArticleTemplateServiceImpl implements IAutoArticleTemplateService {
	
	@Autowired
	private SqlMapper sqlMapper;

    private static Logger logger = LoggerFactory.getLogger(AutoArticleTemplateServiceImpl.class);
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoArticleTemplate entity){
		return sqlMapper.insert("AutoArticleTemplateMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoArticleTemplate entity){
    	return sqlMapper.update("AutoArticleTemplateMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
        return sqlMapper.update("AutoArticleTemplateMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoArticleTemplate query(Map<String, Object> map){
    	return sqlMapper.query("AutoArticleTemplateMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoArticleTemplate> queryList(Object param){
    	return sqlMapper.queryList("AutoArticleTemplateMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoArticleTemplate> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoArticleTemplateMapper.queryList", map, page);
    }
}
