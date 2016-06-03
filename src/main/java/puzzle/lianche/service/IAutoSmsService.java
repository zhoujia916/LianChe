package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoSms;

public interface IAutoSmsService{ 

	public boolean insert(AutoSms entity);

    //批量插入
    public boolean insertBatch(List<AutoSms> list);

    public boolean update(AutoSms entity);

    public boolean delete(Map<String, Object> map);

    public AutoSms query(Map<String, Object> map);
    
    public List<AutoSms> queryList(Object param);

    public List<AutoSms> queryList(Map<String, Object> map, Page page);
    
}
