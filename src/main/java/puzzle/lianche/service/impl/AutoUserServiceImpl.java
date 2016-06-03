package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoUser;
import puzzle.lianche.service.IAutoCollectService;
import puzzle.lianche.service.IAutoUserPicService;
import puzzle.lianche.service.IAutoUserProfileService;
import puzzle.lianche.service.IAutoUserService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoUserService")
public class AutoUserServiceImpl implements IAutoUserService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoUserPicService autoUserPicService;

    @Autowired
    private IAutoUserProfileService autoUserProfileService;

    @Autowired
    private IAutoCollectService autoCollectService;

    private static Logger logger = LoggerFactory.getLogger(AutoUserServiceImpl.class);

    /**
     * 删除会员相关信息
     * @param map
     */
    public void deleteAutoUserRelated(Map map){
        //删除该会员上传的图片
        autoUserPicService.delete(map);
        //删除该会员的简介
        autoUserProfileService.delete(map);
        //删除会员收藏
        autoCollectService.delete(map);
    }
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoUser entity){
		return sqlMapper.insert("AutoUserMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoUser entity){
    	return sqlMapper.update("AutoUserMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
        try{
            if(sqlMapper.delete("AutoUserMapper.delete", map)){
                deleteAutoUserRelated(map);
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
    public AutoUser query(Map<String, Object> map){
    	return sqlMapper.query("AutoUserMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoUser> queryList(Object param){
    	return sqlMapper.queryList("AutoUserMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoUser> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoUserMapper.queryList", map, page);
    }
}
