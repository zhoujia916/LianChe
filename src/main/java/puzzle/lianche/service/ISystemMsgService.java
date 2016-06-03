package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemMsg;

public interface ISystemMsgService{ 

	public boolean insert(SystemMsg entity);

    public boolean update(SystemMsg entity);

    public boolean delete(Map<String, Object> map);

    public SystemMsg query(Map<String, Object> map);
    
    public List<SystemMsg> queryList(Object param);

    public List<SystemMsg> queryList(Map<String, Object> map, Page page);
    
}
