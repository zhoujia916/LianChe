package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoRegion;

public interface IAutoRegionService{ 

	public boolean insert(AutoRegion entity);

    public boolean update(AutoRegion entity);

    public boolean delete(Map<String, Object> map);

    public AutoRegion query(Map<String, Object> map);
    
    public List<AutoRegion> queryList(Object param);

    public List<AutoRegion> queryList(Map<String, Object> map, Page page);
    
}
