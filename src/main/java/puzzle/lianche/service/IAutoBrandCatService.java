package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoBrandCat;

public interface IAutoBrandCatService{ 

	public boolean insert(AutoBrandCat entity);

    public boolean update(AutoBrandCat entity);

    public boolean delete(Map<String, Object> map);

    public AutoBrandCat query(Map<String, Object> map);
    
    public List<AutoBrandCat> queryList(Object param);

    public List<AutoBrandCat> queryList(Map<String, Object> map, Page page);
}
