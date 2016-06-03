package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemAuthority;

public interface ISystemAuthorityService{ 

	public boolean insert(SystemAuthority entity);

    public boolean insertBatch(List<SystemAuthority> list);

    public boolean update(SystemAuthority entity);

    public boolean delete(Map<String, Object> map);

    public boolean save(SystemAuthority entity);

    public SystemAuthority query(Map<String, Object> map);
    
    public List<SystemAuthority> queryList(Object param);

    public List<SystemAuthority> queryList(Map<String, Object> map, Page page);
    
}
