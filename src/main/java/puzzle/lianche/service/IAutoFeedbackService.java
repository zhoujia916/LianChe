package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoFeedback;

public interface IAutoFeedbackService{ 

	public boolean insert(AutoFeedback entity);

    public boolean update(AutoFeedback entity);

    public boolean delete(Map<String, Object> map);

    public AutoFeedback query(Map<String, Object> map);
    
    public List<AutoFeedback> queryList(Object param);

    public List<AutoFeedback> queryList(Map<String, Object> map, Page page);
}
