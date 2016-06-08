package puzzle.lianche.service;

import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoBrandModel;

public interface IAutoBrandModelService{ 

	public boolean insert(AutoBrandModel entity);

    public boolean update(AutoBrandModel entity);

    public boolean delete(Map<String, Object> map);

    public AutoBrandModel query(Map<String, Object> map);
    
    public List<AutoBrandModel> queryList(Object param);

    public List<AutoBrandModel> queryList(Map<String, Object> map, Page page);
    
}
