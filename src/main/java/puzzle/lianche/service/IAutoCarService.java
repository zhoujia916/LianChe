package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoCar;

public interface IAutoCarService{ 

	public boolean insert(AutoCar entity);

    public boolean update(AutoCar entity);

    public boolean delete(Map<String, Object> map);

    public AutoCar query(Map<String, Object> map);
    
    public List<AutoCar> queryList(Object param);

    public List<AutoCar> queryList(Map<String, Object> map, Page page);
}
