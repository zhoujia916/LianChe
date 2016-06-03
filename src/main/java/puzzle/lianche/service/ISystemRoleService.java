package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemRole;

public interface ISystemRoleService{ 

	public boolean insert(SystemRole entity);

    public boolean update(SystemRole entity);

    public boolean delete(Map<String, Object> map);

    public SystemRole query(Map<String, Object> map);

    public List<SystemRole> queryList(Object param);

    public List<SystemRole> queryList(Map<String, Object> map, Page page);
    
}
