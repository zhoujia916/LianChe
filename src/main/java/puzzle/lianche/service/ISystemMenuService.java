package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemMenu;

public interface ISystemMenuService{ 

	public boolean insert(SystemMenu entity);

    public boolean update(SystemMenu entity);

    public boolean delete(Map<String, Object> map);

    public SystemMenu query(Map<String, Object> map);
    
    public List<SystemMenu> queryList(Object param);

    public List<SystemMenu> queryList(Map<String, Object> map, Page page);
    
}
