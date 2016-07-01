package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoCarPic;

public interface IAutoCarPicService{ 

	public boolean insert(AutoCarPic entity);

    public boolean insertBatch(List<AutoCarPic> list);

    public boolean update(AutoCarPic entity);

    public boolean delete(Map<String, Object> map);

    public AutoCarPic query(Map<String, Object> map);

    public List<AutoCarPic> queryList(Integer carId);

    public List<AutoCarPic> queryList(Object param);

    public List<AutoCarPic> queryList(Map<String, Object> map, Page page);
    
}
