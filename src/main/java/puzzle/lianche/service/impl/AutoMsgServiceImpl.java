package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoMsg;
import puzzle.lianche.service.IAutoMsgService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoMsgService")
public class AutoMsgServiceImpl implements IAutoMsgService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoMsg entity){
		return sqlMapper.insert("AutoMsgMapper.insert", entity);
	}

    @Override
    public boolean insertBatch(List<AutoMsg> list) {
        return sqlMapper.insert("AutoMsgMapper.insertBatch", list);
    }

    /**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoMsg entity){
    	return sqlMapper.update("AutoMsgMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoMsgMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoMsg query(Map<String, Object> map){
    	return sqlMapper.query("AutoMsgMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoMsg> queryList(Object param){
    	return sqlMapper.queryList("AutoMsgMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoMsg> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoMsgMapper.queryList", map, page);
    }
}
