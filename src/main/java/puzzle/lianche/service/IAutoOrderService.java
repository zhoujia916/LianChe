package puzzle.lianche.service;

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

    public boolean realeseLock(List<AutoOrder> list);

    public List<String> queryOperate(AutoOrder order, int userType  );

    public AutoOrder query(Integer orderId, String orderSn);

    public boolean doCancel(AutoOrder order);

    public boolean doAccept(AutoOrder order);

    public boolean doReject(AutoOrder order);

    public boolean doReceive(AutoOrder order);

    public boolean doNotify(AutoOrder order);

    public boolean doDeposit(AutoOrder order);

    public boolean doReturnDeposit(AutoOrder order,Integer type);

    public List<AutoOrder> queryOrder(Map<String, Object> map,Page page);

    public Integer queryCount(Map<String, Object> map);
}
