package puzzle.lianche.service;

import puzzle.lianche.entity.AutoArticleTemplate;
import puzzle.lianche.utils.Page;

import java.util.List;
import java.util.Map;

public interface IAutoArticleTemplateService {

	public boolean insert(AutoArticleTemplate entity);

    public boolean update(AutoArticleTemplate entity);

    public boolean delete(Map<String, Object> map);

    public AutoArticleTemplate query(Map<String, Object> map);
    
    public List<AutoArticleTemplate> queryList(Object param);

    public List<AutoArticleTemplate> queryList(Map<String, Object> map, Page page);
}
