package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoBrand;

public interface IAutoBrandService{ 

	public boolean insert(AutoBrand entity);

    public boolean update(AutoBrand entity);

    public boolean delete(Map<String, Object> map);

    public AutoBrand query(Map<String, Object> map);
    
    public List<AutoBrand> queryList(Object param);

    public List<AutoBrand> queryList(Map<String, Object> map, Page page);

    public List<AutoBrand> queryList(AutoBrand entity, Page page);
}
