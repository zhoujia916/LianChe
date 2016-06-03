package puzzle.lianche.service.impl;

import java.util.*;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoBrandCat;
import puzzle.lianche.service.IAutoBrandCatService;
import puzzle.lianche.service.IAutoBrandModelService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoBrandCatService")
public class AutoBrandCatServiceImpl implements IAutoBrandCatService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoBrandModelService autoBrandModelService;

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AutoBrandCatServiceImpl.class);

    /**
     * 删除该车系下面的车型
     */
    public void deleteBrandModel(List<AutoBrandCat> list){
        try{
            Map modelMap=new HashMap();
            if(list.size()>0){
                List<Integer> brandCatId=new ArrayList<Integer>();
                for(AutoBrandCat brandCat:list)
                {
                    brandCatId.add(brandCat.getCatId());
                }
                modelMap.put("brandCatIds", brandCatId);
            }
            autoBrandModelService.delete(modelMap);
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoBrandCat entity){
		return sqlMapper.insert("AutoBrandCatMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoBrandCat entity){
    	return sqlMapper.update("AutoBrandCatMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
        try{
            List<AutoBrandCat> list=queryList(map);
            if(sqlMapper.delete("AutoBrandCatMapper.delete", map)){
                deleteBrandModel(list);
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
    public AutoBrandCat query(Map<String, Object> map){
    	return sqlMapper.query("AutoBrandCatMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoBrandCat> queryList(Object param){
    	return sqlMapper.queryList("AutoBrandCatMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoBrandCat> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoBrandCatMapper.queryList", map, page);
    }
}
