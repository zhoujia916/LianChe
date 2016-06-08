package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoOrderCar;

public interface IAutoOrderCarService{ 

	public boolean insert(AutoOrderCar entity);

    public boolean insertBatch(List<AutoOrderCar> list);

    public boolean update(AutoOrderCar entity);

    public boolean delete(Map<String, Object> map);

    public AutoOrderCar query(Map<String, Object> map);
    
    public List<AutoOrderCar> queryList(Object param);

    public List<AutoOrderCar> queryList(Map<String, Object> map, Page page);


}
