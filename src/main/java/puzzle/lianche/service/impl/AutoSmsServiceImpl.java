package puzzle.lianche.service.impl;


import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.entity.AutoSms;
import puzzle.lianche.service.IAutoSmsService;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;

@Service("autoSmsService")
public class AutoSmsServiceImpl implements IAutoSmsService {

    @Autowired
    private SqlMapper sqlMapper;

    /**
     * 插入单条记录
     */
    @Override
    public boolean insert(AutoSms entity){
        return sqlMapper.insert("AutoSmsMapper.insert", entity);
    }

    /**
     * 批量插入
     * @param list
     * @return
     */
    @Override
    public boolean insertBatch(List<AutoSms> list) {
        return sqlMapper.insert("AutoSmsMapper.insertBatch", list);
    }


    /**
     * 更新单条记录
     */
    @Override
    public boolean update(AutoSms entity){
        return sqlMapper.update("AutoSmsMapper.update", entity);
    }

    /**
     * 删除单条记录
     */
    @Override
    public boolean delete(Map<String, Object> map){
        return sqlMapper.delete("AutoSmsMapper.delete", map);
    }

    /**
     * 查询单条记录
     */
    @Override
    public AutoSms query(Map<String, Object> map){
        return sqlMapper.query("AutoSmsMapper.query", map);
    }

    /**
     * 查询多条记录
     */
    @Override
    public List<AutoSms> queryList(Object param){
        return sqlMapper.queryList("AutoSmsMapper.queryList", param);
    }


    /**
     * 查询分页记录
     */
    @Override
    public List<AutoSms> queryList(Map<String, Object> map, Page page){
        return sqlMapper.queryList("AutoSmsMapper.queryList", map, page);
    }

    @Override
    public boolean send(AutoSms sms) {
        return false;
    }

}

