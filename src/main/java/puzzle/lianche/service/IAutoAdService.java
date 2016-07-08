package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoAd;

public interface IAutoAdService{ 

	public boolean insert(AutoAd entity);

    public boolean update(AutoAd entity);

    public boolean delete(Map<String, Object> map);

    public AutoAd query(Map<String, Object> map);

    public AutoAd query(Integer adId);
    
    public List<AutoAd> queryList(Object param);

    public List<AutoAd> queryList(Map<String, Object> map, Page page);
}
