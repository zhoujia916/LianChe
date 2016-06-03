package puzzle.lianche.service.impl;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoFeedback;
import puzzle.lianche.service.IAutoFeedbackService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoFeedbackService")
public class AutoFeedbackServiceImpl implements IAutoFeedbackService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoFeedback entity){
		return sqlMapper.insert("AutoFeedbackMapper.insert", entity);
	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoFeedback entity){
    	return sqlMapper.update("AutoFeedbackMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoFeedbackMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoFeedback query(Map<String, Object> map){
    	return sqlMapper.query("AutoFeedbackMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoFeedback> queryList(Object param){
    	return sqlMapper.queryList("AutoFeedbackMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoFeedback> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoFeedbackMapper.queryList", map, page);
    }
}
