package puzzle.lianche.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.Constants;
import puzzle.lianche.entity.AutoOrder;
import puzzle.lianche.entity.AutoOrderCar;
import puzzle.lianche.service.IAutoOrderCarService;
import puzzle.lianche.service.IAutoOrderService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.utils.StringUtil;

@Service("autoOrderService")
public class AutoOrderServiceImpl implements IAutoOrderService {
	
	@Autowired
	private SqlMapper sqlMapper;

    @Autowired
    private IAutoOrderCarService autoOrderCarService;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoOrder entity){
        try{
            if(sqlMapper.insert("AutoOrderMapper.insert", entity)){
                if(entity.getCars() != null && entity.getCars().size() > 0){
                    for(int i = 0; i < entity.getCars().size(); i++){
                        entity.getCars().get(i).setOrderId(entity.getOrderId());
                    }
                    autoOrderCarService.insertBatch(entity.getCars());
                }
            }
            return true;
        }
        catch (Exception e){

        }
        return false;

	}

	/**
	* 更新单条记录
	*/
	@Override
    public boolean update(AutoOrder entity){
    	return sqlMapper.update("AutoOrderMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("AutoOrderMapper.delete", map);
    }

	/**
	* 查询单条记录
	*/
	@Override
    public AutoOrder query(Map<String, Object> map){
    	return sqlMapper.query("AutoOrderMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<AutoOrder> queryList(Object param){
    	return sqlMapper.queryList("AutoOrderMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<AutoOrder> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("AutoOrderMapper.queryList", map, page);
    }

    /**
     * 创建订单序号(根据用户ID生成)
     */
    @Override
    public String createSn(String key) {
        String orderSn = "";
        Random random = new Random();
        int[] seeks = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e', 'f'};
        String sn = ConvertUtil.toString(new Date(), "yyMMdd");
        String input = ConvertUtil.toString(new Date(), "HHmmssSSS");

        boolean exists = true;

        while (exists) {

            input += StringUtil.padLeft(key, 11, "0") + UUID.randomUUID();
            input = EncryptUtil.MD5(input, 16);

            for (int i = 0; i < chars.length; i++) {
                random.setSeed(random.nextInt(seeks.length));
                input = input.replace(String.valueOf(chars[i]), String.valueOf(random.nextInt(10)));
            }
            orderSn = sn + input;

            exists = query(0, orderSn) != null;
        }

        return orderSn;
    }


    /**
     * 根据关键值查询订单
     * @param orderId
     * @param orderSn
     * @return
     */
    public AutoOrder query(int orderId, String orderSn){
        Map<String, Object> map = new HashMap<String, Object>();
        if(orderId > 0){
            map.put("orderId", orderId);
        }
        if(StringUtil.isNotNullOrEmpty(orderSn)){
            map.put("orderSn", orderSn);
        }
        return query(map);
    }

    /**
     * 取消订单
     * @param order
     * @return
     */
    public boolean cancel(AutoOrder order){
        try {
            if (order.getOrderStatus() == Constants.OS_SUBMIT) {
                order.setOrderStatus(Constants.OS_CANCEL);
                
            }
            else if(order.getOrderStatus() == Constants.OS_REJECT){
                order.setOrderStatus(Constants.OS_CANCEL);
            }
            else if(order.getOrderStatus() == Constants.OS_EXECUTE){
                order.setOrderStatus(Constants.OS_CANCEL);
            }
            return true;
        }
        catch (Exception e){

        }
        return false;
    }
}
