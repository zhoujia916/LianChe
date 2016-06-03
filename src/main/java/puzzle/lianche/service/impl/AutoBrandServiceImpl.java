package puzzle.lianche.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.AutoBrandCat;
import puzzle.lianche.service.IAutoBrandCatService;
import puzzle.lianche.service.IAutoBrandModelService;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoBrandService")
public class AutoBrandServiceImpl implements IAutoBrandService {

    @Autowired
    private SqlMapper sqlMapper;

    @Autowired
    private IAutoBrandCatService autoBrandCatService;

    @Autowired
    private IAutoBrandModelService autoBrandModelService;

    private static Logger logger = LoggerFactory.getLogger(AutoBrandServiceImpl.class);

    /**
     * 删除该品牌下的车系和车型
     * @param map
     */
    public void deleteBrandCat(Map map){
        try {
                autoBrandCatService.delete(map);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * 插入单条记录
     */
    @Override
    public boolean insert(AutoBrand entity) {
        return sqlMapper.insert("AutoBrandMapper.insert", entity);
    }

    /**
     * 更新单条记录
     */
    @Override
    public boolean update(AutoBrand entity) {
        return sqlMapper.update("AutoBrandMapper.update", entity);
    }

    /**
     * 删除单条记录
     */
    @Override
    public boolean delete(Map<String, Object> map) {
        try{
            if(sqlMapper.delete("AutoBrandMapper.delete", map)){
                deleteBrandCat(map);
                return true;
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    /**
     * 查询单条记录
     */
    @Override
    public AutoBrand query(Map<String, Object> map) {
        return sqlMapper.query("AutoBrandMapper.query", map);
    }

    /**
     * 查询多条记录
     */
    @Override
    public List<AutoBrand> queryList(Object param) {
        return sqlMapper.queryList("AutoBrandMapper.queryList", param);
    }


    /**
     * 查询分页记录
     */
    @Override
    public List<AutoBrand> queryList(Map<String, Object> map, Page page) {
        return sqlMapper.queryList("AutoBrandMapper.queryList", map, page);
    }

    /**
     * 查询分页记录
     */
    @Override
    public List<AutoBrand> queryList(AutoBrand entity, Page page) {
        Map<String, Object> map = new HashMap<String, Object>();
        return sqlMapper.queryList("AutoBrandMapper.queryList", map, page);
    }
}
