package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;

import puzzle.lianche.entity.AutoAd;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoAdPosition;

public interface IAutoAdPositionService{ 

	public boolean insert(AutoAdPosition entity);

    public boolean update(AutoAdPosition entity);

    public boolean delete(Map<String, Object> map);

    public AutoAdPosition query(Map<String, Object> map);
    
    public List<AutoAdPosition> queryList(Object param);

    public List<AutoAdPosition> queryList(Map<String, Object> map, Page page);
}
