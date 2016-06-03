package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemLog;

public interface ISystemLogService{ 

	public boolean insert(SystemLog entity);

    public boolean update(SystemLog entity);

    public boolean delete(Map<String, Object> map);

    public SystemLog query(Map<String, Object> map);

    public List<SystemLog> queryList(Object param);

    public List<SystemLog> queryList(Map<String, Object> map, Page page);
    
}
