package puzzle.lianche.service.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoCar;
import puzzle.lianche.entity.AutoCarAttr;
import puzzle.lianche.entity.AutoCarPic;
import puzzle.lianche.service.*;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.utils.StringUtil;

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
                if (entity.getAttrs() != null && entity.getAttrs().size() > 0) {
                    for (int i = 0; i < entity.getAttrs().size(); i++) {
                        entity.getAttrs().get(i).setCarId(entity.getCarId());
                    }
                    autoCarAttrService.insertBatch(entity.getAttrs());
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
        try{
            Map<String, Object> map = new HashMap<String, Object>();
            if(sqlMapper.update("AutoCarMapper.update", entity)){
                if (entity.getAttrs() != null && entity.getAttrs().size() > 0) {
                    //region Update Attr
                    map.clear();
                    map.put("carId",entity.getCarId());
                    List<AutoCarAttr> oldList = autoCarAttrService.queryList(map);
                    List<AutoCarAttr> insertList = new ArrayList<AutoCarAttr>();
                    List<AutoCarAttr> updateList = new ArrayList<AutoCarAttr>();
                    List<Integer> deleteList = new ArrayList<Integer>();

                    for(AutoCarAttr oldItem : oldList){
                        boolean find = false;
                        for(AutoCarAttr newItem : entity.getAttrs()){
                            if(oldItem.getCarAttrId().equals(newItem.getCarAttrId())){
                                find = true;
                                updateList.add(newItem);
                                break;
                            }
                        }
                        if(!find){
                            deleteList.add(oldItem.getCarAttrId());
                        }
                    }

                    for(AutoCarAttr newItem : entity.getAttrs()){
                        boolean find = false;
                        for(AutoCarAttr oldItem : oldList) {
                            if(newItem.getCarAttrId().equals(oldItem.getCarAttrId())){
                                find = true;
                                break;
                            }
                        }
                        if(!find){
                            insertList.add(newItem);
                        }
                    }

                    if(insertList.size() > 0){
                        autoCarAttrService.insertBatch(insertList);
                    }
                    if(updateList.size() > 0){
                        for(AutoCarAttr attr : updateList){
                            autoCarAttrService.update(attr);
                        }
                    }
                    if(deleteList.size() > 0){
                        map.clear();
                        map.put("carAttrIds", StringUtil.concat(deleteList, ","));
                        autoCarAttrService.delete(map);
                    }
                    //endregion
                }
                if(entity.getPics() != null && entity.getPics().size() > 0){
                    //region Update Pic
                    map.put("carId", entity.getCarId());
                    List<AutoCarPic> oldList = autoCarPicService.queryList(map);
                    List<AutoCarPic> insertList = new ArrayList<AutoCarPic>();
                    List<Integer> deleteList = new ArrayList<Integer>();

                    for(AutoCarPic oldItem : oldList){
                        boolean find = false;
                        for(AutoCarPic newItem : entity.getPics()){
                            if(oldItem.getPath().equals(newItem.getPath())){
                                find = true;
                                break;
                            }
                        }
                        if(!find){
                            deleteList.add(oldItem.getPicId());
                        }
                    }

                    for(AutoCarPic newItem : entity.getPics()){
                        boolean find = false;
                        for(AutoCarPic oldItem : oldList) {
                            if(newItem.getPath().equals(oldItem.getPath())){
                                find = true;
                                break;
                            }
                        }
                        if(!find){
                            insertList.add(newItem);
                        }
                    }

                    if(insertList.size() > 0){
                        autoCarPicService.insertBatch(insertList);
                    }
                    if(deleteList.size() > 0){
                        map.clear();
                        map.put("picIds", StringUtil.concat(deleteList, ","));
                        autoCarPicService.delete(map);
                    }
                    //endregion
                }
            }
            return true;
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return false;
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
                autoCollectService.delete(map);
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
        map.put("carId", carId);
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

    /**
     * 统计车源总数
     * @param map
     * @return
     */
    public Integer queryCount(Map<String, Object> map){
        return (Integer)sqlMapper.query("AutoCarMapper.queryCount", map);
    }
}
