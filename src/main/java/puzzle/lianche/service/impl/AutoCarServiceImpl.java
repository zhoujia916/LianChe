package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoCarService")
public class AutoCarServiceImpl implements IAutoCarService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoCarPicService autoCarPicService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoCollectService autoCollectService;

    private static Logger logger = LoggerFactory.getLogger(AutoCarServiceImpl.class);

	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoCar entity){
        try {
            if (sqlMapper.insert("AutoCarMapper.insert", entity)) {
                if (entity.getAttr() != null && entity.getAttr().size() > 0) {
                    for (int i = 0; i < entity.getAttr().size(); i++) {
                        entity.getAttr().get(i).setCarId(entity.getCarId());
                    }
                    autoCarAttrService.insertBatch(entity.getAttr());
                }
                if(entity.getPics() != null && entity.getPics().size() > 0){
                    for (int i = 0; i < entity.getPics().size(); i++) {
                        entity.getPics().get(i).setCarId(entity.getCarId());
                    }
                    autoCarPicService.insertBatch(entity.getPics());
                }
            }
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoCar entity){
    	return sqlMapper.update("AutoCarMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	try{
            if(sqlMapper.delete("AutoCarMapper.delete", map)){
                autoCarAttrService.delete(map);
                autoCarPicService.delete(map);
                autoCarAttrService.delete(map);
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
    public AutoCar query(Map<String, Object> map){
    	return sqlMapper.query("AutoCarMapper.query", map);
    }

    /**
     * 查询单条记录
     */
    @Override
    public AutoCar query(int carId) {
        Map<String, Object> map = new HashMap<String, Object>();
        return query(map);
    }

    /**
	* 查询多条记录
	*/
	@Override
    public List<AutoCar> queryList(Object param){
    	return sqlMapper.queryList("AutoCarMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoCar> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoCarMapper.queryList", map, page);
    }

    @Override
    public List<AutoCar> queryUserCollect(Map<String, Object> map,Page page) {
        return sqlMapper.queryList("AutoCarMapper.queryUserCollect", map,page);
    }

    @Override
    public List<AutoCar> queryOrderList(Map<String, Object> map,Page page) {
        return sqlMapper.queryList("AutoCarMapper.queryOrderList", map,page);
    }
}
