package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoUserProfile;

public interface IAutoUserProfileService{ 

	public boolean insert(AutoUserProfile entity);

    public boolean update(AutoUserProfile entity);

    public boolean delete(Map<String, Object> map);

    public AutoUserProfile query(Map<String, Object> map);
    
    public List<AutoUserProfile> queryList(Object param);

    public List<AutoUserProfile> queryList(Map<String, Object> map, Page page);
    
}
