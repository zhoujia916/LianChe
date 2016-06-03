package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoUserPic;

public interface IAutoUserPicService{ 

	public boolean insert(AutoUserPic entity);

    public boolean update(AutoUserPic entity);

    public boolean delete(Map<String, Object> map);

    public AutoUserPic query(Map<String, Object> map);
    
    public List<AutoUserPic> queryList(Object param);

    public List<AutoUserPic> queryList(Map<String, Object> map, Page page);
    
}
