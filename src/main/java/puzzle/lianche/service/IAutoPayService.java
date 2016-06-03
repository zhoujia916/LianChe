package puzzle.lianche.service;

import java.util.Date;
import java.util.Map;
import java.util.List;
import puzzle.lianche.utils.Page;
import puzzle.lianche.entity.AutoPay;

public interface IAutoPayService{ 

	public boolean insert(AutoPay entity);

    public boolean update(AutoPay entity);

    public boolean delete(Map<String, Object> map);

    public AutoPay query(Map<String, Object> map);
    
    public List<AutoPay> queryList(Object param);

    public List<AutoPay> queryList(Map<String, Object> map, Page page);
    
}
