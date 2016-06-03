package puzzle.lianche.service;

import java.util.Map;
import java.util.List;

import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoArticleCat;

public interface IAutoArticleCatService{ 

	public boolean insert(AutoArticleCat entity);

    public boolean update(AutoArticleCat entity);

    public boolean delete(Map<String, Object> map);

    public AutoArticleCat query(Map<String, Object> map);
    
    public List<AutoArticleCat> queryList(Object param);

    public List<AutoArticleCat> queryList(Map<String, Object> map, Page page);

    //新增方法
    public List<AutoArticleCat> queryByArticleCat(AutoArticleCat autoArticleCat);
}
