package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoCarAttr;

public interface IAutoCarAttrService{ 

	public boolean insert(AutoCarAttr entity);

    public boolean update(AutoCarAttr entity);

    public boolean delete(Map<String, Object> map);

    public AutoCarAttr query(Map<String, Object> map);
    
    public List<AutoCarAttr> queryList(Object param);

    public List<AutoCarAttr> queryList(Map<String, Object> map, Page page);
    
}
