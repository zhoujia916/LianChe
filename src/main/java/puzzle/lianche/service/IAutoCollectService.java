package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoCollect;

public interface IAutoCollectService{ 

	public boolean insert(AutoCollect entity);

    public boolean update(AutoCollect entity);

    public boolean delete(Map<String, Object> map);

    public AutoCollect query(Map<String, Object> map);
    
    public List<AutoCollect> queryList(Object param);

    public List<AutoCollect> queryList(Map<String, Object> map, Page page);
}
