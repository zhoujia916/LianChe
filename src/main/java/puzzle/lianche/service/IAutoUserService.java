package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoUser;

public interface IAutoUserService{ 

	public boolean insert(AutoUser entity);

    public boolean update(AutoUser entity);

    public boolean delete(Map<String, Object> map);

    public AutoUser query(Map<String, Object> map);

    public AutoUser query(Integer userId, String userName);
    
    public List<AutoUser> queryList(Object param);

    public List<AutoUser> queryList(Map<String, Object> map, Page page);

    public Integer queryCount(Map<String, Object> map);
}
