package puzzle.lianche.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.Constants;
import puzzle.lianche.entity.*;
import puzzle.lianche.init.InitConfig;
import puzzle.lianche.init.InitTask;
import puzzle.lianche.push.SmsPush;
import puzzle.lianche.service.*;
import puzzle.lianche.task.TimeoutTask;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.utils.StringUtil;

@Service("autoOrderService")
public class AutoOrderServiceImpl extends BaseServiceImpl implements IAutoOrderService {

    @Autowired
    private IAutoOrderCarService autoOrderCarService;

    @Autowired
    private IAutoCarAttrService autoCarAttrService;

    @Autowired
    private IAutoUserService autoUserService;

    @Autowired
    private InitTask initTask;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(AutoOrder entity){
        try{
            //插入订单信息
            if(sqlMapper.insert("AutoOrderMapper.insert", entity)){
                if(entity.getCar() != null){
                    //插入预定车源
                    AutoOrderCar orderCar = entity.getCar();
                    orderCar.setOrderId(entity.getOrderId());
                    autoOrderCarService.insert(orderCar);

                    //更新锁定数量
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("carAttrId", orderCar.getCarAttrId());
                    AutoCarAttr attr = autoCarAttrService.query(map);
                    attr.setLockNumber(attr.getLockNumber() + orderCar.getCarNumber());
                    attr.setSurplusNumber(attr.getTotalNumber() - attr.getLockNumber());
                    autoCarAttrService.update(attr);
                }

            }
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
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
        try{
            if(sqlMapper.delete("AutoOrderMapper.delete", map)){
                autoOrderCarService.delete(map);
                return true;
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    	return false;
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
     * 释放锁定的订单
     * @param list
     * @return
     */
    @Override
    public boolean realeseLock(List<AutoOrder> list) {

        return false;
    }

    /**
     * 查询订单可执行操作
     * @param order
     * @return
     */
    @Override
    public List<String> queryOperate(AutoOrder order, int userType) {
        List<String> operates = new ArrayList<String>();
        if(order != null) {
            if (order.getOrderStatus() == Constants.OS_SUBMIT
                    && order.getPayStatus() == Constants.PS_WAIT_BUYER_DEPOSIT
                    && order.getShipStatus() == Constants.SS_UNSHIP) {
                if (userType == Constants.ORDER_USER_BUYER) {
                    operates.add(Constants.OO_CANCEL);
                    operates.add(Constants.OO_PAYMENT);
                } else if (userType == Constants.ORDER_USER_SELLER) {

                } else if (userType == Constants.ORDER_USER_ADMIN) {
                    operates.add(Constants.OO_CANCEL);
                    operates.add(Constants.OO_PAYMENT);
                }
            }
            if (order.getOrderStatus() == Constants.OS_SUBMIT
                    && order.getPayStatus() == Constants.PS_BUYER_PAY_DEPOSIT
                    && order.getShipStatus() == Constants.SS_UNSHIP) {
                if (userType == Constants.ORDER_USER_BUYER) {
                    operates.add(Constants.OO_CANCEL);
                } else if (userType == Constants.ORDER_USER_SELLER) {
                    operates.add(Constants.OO_ACCEPT);
                    operates.add(Constants.OO_REJECT);
                } else if (userType == Constants.ORDER_USER_ADMIN) {
                    operates.add(Constants.OO_CANCEL);
                    operates.add(Constants.OO_ACCEPT);
                    operates.add(Constants.OO_REJECT);
                    operates.add(Constants.OO_UNPAYMENT);
                }
            } else if (order.getOrderStatus() == Constants.OS_EXECUTE
                    && order.getPayStatus() == Constants.PS_SELLER_PAY_DEPOSIT
                    && order.getShipStatus() == Constants.SS_UNSHIP) {
                if (userType == Constants.ORDER_USER_BUYER) {
                    operates.add(Constants.OO_REQUEST_CANCEL);
                    operates.add(Constants.OO_RECEIVE);
                    operates.add(Constants.OO_CONTACT_SELLER);
                } else if (userType == Constants.ORDER_USER_SELLER) {
                    operates.add(Constants.OO_REQUEST_CANCEL);
                    operates.add(Constants.OO_CONTACT_BUYER);
                    operates.add(Constants.OO_NOTIFY_RECEIVE);
                } else if (userType == Constants.ORDER_USER_ADMIN) {
                    operates.add(Constants.OO_CANCEL);
                    operates.add(Constants.OO_UNACCEPT);
                    operates.add(Constants.OO_RECEIVE);
                    operates.add(Constants.OO_NOTIFY_RECEIVE);
                }
            } else if (order.getOrderStatus() == Constants.OS_SUCCESS
                    && order.getPayStatus() == Constants.PS_WAIT_SYSTEM_DEPOSIT
                    && order.getShipStatus() == Constants.SS_SHIPED) {
                if (userType == Constants.ORDER_USER_BUYER) {
                    operates.add(Constants.OO_CONTACT_SELLER);
                } else if (userType == Constants.ORDER_USER_SELLER) {
                    operates.add(Constants.OO_CONTACT_BUYER);
                } else if (userType == Constants.ORDER_USER_ADMIN) {
                    if (order.getSellerDeposit() > 0) {
                        operates.add(Constants.OO_RETURN_SELLER_DEPOSIT);
                    }
                    if (order.getBuyerDeposit() > 0) {
                        operates.add(Constants.OO_RETURN_BUYER_DEPOSIT);
                    }
                }
            } else if (order.getOrderStatus() == Constants.OS_CANCEL) {
                if (order.getSellerDeposit() > 0) {
                    operates.add(Constants.OO_RETURN_SELLER_DEPOSIT);
                }
                if (order.getBuyerDeposit() > 0) {
                    operates.add(Constants.OO_RETURN_BUYER_DEPOSIT);
                }
            }
        }
        return operates;
    }


    /**
     * 根据关键值查询订单
     * @param orderId
     * @param orderSn
     * @return
     */
    public AutoOrder query(Integer orderId, String orderSn){
        Map<String, Object> map = new HashMap<String, Object>();
        if(orderId != null && orderId > 0){
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
    public boolean doCancel(AutoOrder order){
        try {
            // 更新订单状态
            order.setOrderStatus(Constants.OS_CANCEL);
            sqlMapper.update("AutoOrderMapper.update", order);

            //短信消息通知买家和卖家
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 申请取消订单
     * @param order
     * @return
     */
    @Override
    public boolean doRequestCancel(AutoOrder order) {
        try {
            // 更新订单状态
            order.setOrderStatus(Constants.OS_REQUEST_CANCEL);
            sqlMapper.update("AutoOrderMapper.update", order);

            //短信消息通知买家和卖家
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 卖家拒绝订单
     * @param order
     * @return
     */
    @Override
    public boolean doReject(AutoOrder order) {
        try {
            order.setOrderStatus(Constants.OS_CANCEL);

            sqlMapper.update("AutoOrderMapper.update", order);

            //短信消息通知买家
            AutoUser buyer = autoUserService.query(order.getBuyerId(), null);
            SmsPush.send(SmsPush.CODE_SENDMSG, buyer.getUserName(), "您有一笔订单卖家已拒绝交易，订单号为-" + order.getOrderSn());
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 卖家同意订单
     * @param order
     * @return
     */
    @Override
    public boolean doAccept(AutoOrder order) {
        try {
            // 更新订单状态
            order.setOrderStatus(Constants.OS_EXECUTE);
            order.setPayStatus(Constants.PS_SELLER_PAY_DEPOSIT);
            order.setSellerPayTime(ConvertUtil.toLong(new Date()));
            sqlMapper.update("AutoOrderMapper.update", order);

            // 短信消息通知买家
            AutoUser buyer = autoUserService.query(order.getBuyerId(), null);

            SmsPush.send(SmsPush.CODE_SENDMSG, buyer.getUserName(), "您有一笔订单卖家已接受交易，订单号为-" + order.getOrderSn());

            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 确认收货
     * @param order
     * @return
     */
    @Override
    public boolean doReceive(AutoOrder order) {
        try {
            // 更新订单状态
            order.setOrderStatus(Constants.OS_SUCCESS);
            order.setPayStatus(Constants.PS_WAIT_SYSTEM_DEPOSIT);
            order.setShipStatus(Constants.SS_SHIPED);
            sqlMapper.update("AutoOrderMapper.update", order);

            //短信消息通知卖家
            AutoUser seller = autoUserService.query(order.getSellerId(), null);
            SmsPush.send(SmsPush.CODE_SENDMSG, seller.getUserName(), "您有一笔订单买家已确认收货，订单号为-" + order.getOrderSn());

            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 通知确认收货
     * @param order
     * @return
     */
    @Override
    public boolean doNotify(AutoOrder order) {
        //短信通知买家
        AutoUser buyer = autoUserService.query(order.getBuyerId(), null);

        String response = SmsPush.send(SmsPush.CODE_SENDMSG, buyer.getUserName(), "您有一笔订单等待收货确认，订单号为-" + order.getOrderSn() + "，请尽快处理");

        return SmsPush.isSuccess(response);
    }

    /**
     * 买家支付订金
     * @param order
     * @return
     */
    @Override
    public boolean doDeposit(AutoOrder order) {
        try{
            // 更新订单状态
            order.setPayStatus(Constants.PS_BUYER_PAY_DEPOSIT);
            order.setBuyerPayTime(ConvertUtil.toLong(new Date()));

            //定时任务


            sqlMapper.update("AutoOrderMapper.update", order);
            // 短信通知卖家
            AutoUser seller = autoUserService.query(order.getSellerId(), null);
            SmsPush.send(SmsPush.CODE_SENDMSG, seller.getUserName(), "您有一笔订单买家已支付订金，订单号为-" + order.getOrderSn() + "，请尽快处理。");
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 退还支付订金
     * @param order
     * @return
     */
    public boolean doReturnDeposit(AutoOrder order, Integer type){
        try {
            if (type == Constants.ORDER_USER_ALL) {
                order.setBuyerDeposit(0);
                order.setSellerDeposit(0);
                sqlMapper.update("AutoOrderMapper.update", order);
            } else{
                if (type == Constants.ORDER_USER_BUYER && order.getBuyerDeposit() > 0) {
                    order.setBuyerDeposit(0);
                    sqlMapper.update("AutoOrderMapper.update", order);
                }
                else if (type == Constants.ORDER_USER_SELLER && order.getSellerDeposit() > 0) {
                    order.setSellerDeposit(0);
                    sqlMapper.update("AutoOrderMapper.update", order);
                }
            }
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 统计订单总数
     * @param map
     * @return
     */
    public Integer queryCount(Map<String, Object> map){
        return (Integer)sqlMapper.query("AutoOrderMapper.queryCount", map);
    }
}
