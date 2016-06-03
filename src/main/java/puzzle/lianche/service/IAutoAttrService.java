package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoAttr;

public interface IAutoAttrService{ 

	public boolean insert(AutoAttr entity);

    public boolean update(AutoAttr entity);

    public boolean delete(Map<String, Object> map);

    public AutoAttr query(Map<String, Object> map);
    
    public List<AutoAttr> queryList(Object param);

    public List<AutoAttr> queryList(Map<String, Object> map, Page page);
}
