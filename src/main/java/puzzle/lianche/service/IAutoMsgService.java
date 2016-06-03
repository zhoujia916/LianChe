package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoMsg;

public interface IAutoMsgService{ 

	public boolean insert(AutoMsg entity);

    //批量插入
    public boolean insertBatch(List<AutoMsg> list);

    public boolean update(AutoMsg entity);

    public boolean delete(Map<String, Object> map);

    public AutoMsg query(Map<String, Object> map);
    
    public List<AutoMsg> queryList(Object param);

    public List<AutoMsg> queryList(Map<String, Object> map, Page page);
    
}
