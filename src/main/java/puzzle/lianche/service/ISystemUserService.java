package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;

import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.SystemUser;

public interface ISystemUserService{ 

	public boolean insert(SystemUser entity);

    public boolean update(SystemUser entity);

    public boolean delete(Map<String, Object> map);

    public SystemUser query(Map<String, Object> map);
    
    public List<SystemUser> queryList(Object param);

    public List<SystemUser> queryList(Map<String, Object> map, Page page);

    public List<SystemAuthority> queryAuthority(int userId);
}
