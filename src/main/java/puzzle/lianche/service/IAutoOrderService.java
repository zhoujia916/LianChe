package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoOrder;

public interface IAutoOrderService{ 

	public boolean insert(AutoOrder entity);

    public boolean update(AutoOrder entity);

    public boolean delete(Map<String, Object> map);

    public AutoOrder query(Map<String, Object> map);
    
    public List<AutoOrder> queryList(Object param);

    public List<AutoOrder> queryList(Map<String, Object> map, Page page);

    public String createSn(String key);

    public AutoOrder query(int orderId, String orderSn);

    public boolean cancel(AutoOrder order);
}
