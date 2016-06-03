package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoArticle;

public interface IAutoArticleService{ 

	public boolean insert(AutoArticle entity);

    public boolean update(AutoArticle entity);

    public boolean delete(Map<String, Object> map);

    public AutoArticle query(Map<String, Object> map);
    
    public List<AutoArticle> queryList(Object param);

    public List<AutoArticle> queryList(Map<String, Object> map, Page page);
}
