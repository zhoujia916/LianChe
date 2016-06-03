package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoArticleCat;
import puzzle.lianche.service.IAutoArticleCatService;
import puzzle.lianche.service.IAutoArticleService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoArticleCatService")
public class AutoArticleCatServiceImpl implements IAutoArticleCatService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoArticleService autoArticleService;

    private static Logger logger = LoggerFactory.getLogger(AutoArticleServiceImpl.class);

    /**
     * 删除该文章类型相关的文章
     * @param map
     */
    public void deleteaAricle(Map map){
       autoArticleService.delete(map);
    }
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoArticleCat entity){
		return sqlMapper.insert("AutoArticleCatMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoArticleCat entity){
    	return sqlMapper.update("AutoArticleCatMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
        try{
            if(sqlMapper.delete("AutoArticleCatMapper.delete", map)){
                deleteaAricle(map);
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
    public AutoArticleCat query(Map<String, Object> map){
    	return sqlMapper.query("AutoArticleCatMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoArticleCat> queryList(Object param){
    	return sqlMapper.queryList("AutoArticleCatMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoArticleCat> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoArticleCatMapper.queryList", map, page);
    }

    @Override
    public List<AutoArticleCat> queryByArticleCat(AutoArticleCat autoArticleCat) {
        return sqlMapper.queryList("AutoArticleCatMapper.queryByArticleCat",autoArticleCat);
    }
}
