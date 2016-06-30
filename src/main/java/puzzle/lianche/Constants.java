package puzzle.lianche;


import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final String SESSION_USER = "CURRENT_USER";
    public static final String SESSION_ADMIN = "CURRENT_ADMIN";

    public static final String COOKIE_USER = "USERINFO";
    public static final String COOKIE_ADMIN = "ADMININFO";

    public static final String CONTEXT_PATH = "contextPath";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    public static final Integer DEFAULT_USER_TYPE = 2;

    public static final Map<Integer,Object> MAP_ACTION = new HashMap<Integer, Object>() {
        {
            put(1, "有效");
            put(2, "无效");
        }
    };

    public static final Map<Integer, String> MAP_USER_SEX = new HashMap<Integer, String>(){
        {
            put(1,"男");
            put(2,"女");
        }
    };

    public static final Map<Integer, String> MAP_LOG_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"查看");
            put(2,"增加");
            put(3,"删除");
            put(4,"修改");
            put(5,"导入");
            put(6,"导出");
        }
    };

    public static final Map<Integer, String> MAP_ROLE_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"系统角色");
            put(2,"自定义角色");
        }
    };

    public static final Integer SMS_TYPE_CODE = 1;
    public static final Integer SMS_TYPE_CONTENT = 2;
    public static final Integer SMS_STATUS_TRUE=1;
    public static final Integer SMS_STATUS_FALSE=2;
    public static final Integer SMS_TYPE_REGISTER = 1;
    public static final Integer SMS_TYPE_RETRIEVE = 2;
    public static final Integer SMS_TYPE_MODIFY=3;
    public static final Integer SMS_TYPE_NOTICE=4;

    //SystemUser Type
    public static final Integer USER_MAP_TARGET_TYPE_ROLE = 1;
    public static final Integer USER_MAP_TARGET_TYPE_GROUP = 2;
    public static final Integer USER_MAP_TARGET_TYPE_DEPT = 3;

    public static final Map<String, Integer> MAP_ACTION_LOG_MAPPING = new HashMap<String, Integer>(){
        {
            put("SELECT",1);
            put("CREATE",2);
            put("DELETE",3);
            put("UPDATE",4);
        }
    };

    /**
     * 1=无效  2=注册或添加   3=等待审核  4=审核通过  5=审核未通过
     */
    public static final Integer AUTO_USER_STATUS_DISABLED = 1;
    public static final Integer AUTO_USER_STATUS_NORMAL = 2;
    public static final Integer AUTO_USER_STATUS_AUTH_WAITCHECK = 3;
    public static final Integer AUTO_USER_STATUS_AUTH_SUCCESS = 4;
    public static final Integer AUTO_USER_STATUS_AUTH_FAIL = 5;

    public static final Integer SYSTEM_AUTHORITY_TARGET_MENU = 1;
    public static final Integer SYSTEM_AUTHORITY_TARGET_ACTION = 2;

    public static final  Map<Integer, String> MAP_AUTO_USER_STATUS = new HashMap<Integer, String>(){
        {
            put(1,"账户禁用");
            put(2,"注册或添加");
            put(3,"等待实名认证");
            put(4,"实名认证通过");
            put(5,"实名认证未通过");
        }
    };

    public static final Map<Integer, String> MAP_AUTO_USER_SHOP_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"4s店");
            put(2,"有实体店二网");
            put(3,"无实体店二网");
            put(4,"个人");
        }
    };

    public static final Map<Integer, String> MAP_AUTO_ARTICLE_STATUS = new HashMap<Integer, String>(){
        {
            put(1,"已保存");
            put(2,"已提交");
            put(3,"已审核");
        }
    };

    public static final Map<Integer, String> MAP_AUTO_ARTICLE_USER_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"前台用户");
            put(2,"后台用户");
        }
    };

    public static final Integer AUTO_COLLECT_TYPE_CAR = 1;

    public static final Map<Integer, String> MAP_AUTO_COLLECT_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"销车资源");
        }
    };

    //region AUTO_CAR_TYPE
    public static final Integer AUTO_CAR_TYPE_COMMON = 1;
    public static final Integer AUTO_CAR_TYPE_SALE = 2;
    public static final Map<Integer, String> MAP_AUTO_CAR_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"销车资源");
            put(2,"特卖会");
        }
    };
    //endregion

    //region AUTO_CAR_TYPE
    public static final Integer AUTO_CAR_HAS_PARTS_NO = 1;
    public static final Integer AUTO_CAR_HAS_PARTS_YES = 2;
    public static final Map<Integer, String> MAP_AUTO_CAR_HAS_PARTS = new HashMap<Integer, String>(){
        {
            put(1,"没有配件");
            put(2,"有配件");
        }
    };
    //endregion

    //region AUTO_CAR_STATUS
    public static final Integer AUTO_CAR_STATUS_OFF = 1;
    public static final Integer AUTO_CAR_STATUS_ON = 2;

    //region AUTO_COLLECT_STATUS
    public static final Integer AUTO_COLLECT_STATUS_NORMAL = 1;
    public static final Integer AUTO_COLLECT_STATUS_DELETE = 2;

    //region AUTO_CAR_QUOTE_TYPE
    public static final Integer AUTO_CAR_QUOTE_TYPE_UP = 1;
    public static final Integer AUTO_CAR_QUOTE_TYPE_DOWN = 2;
    public static final Map<Integer, String> MAP_AUTO_CAR_QUOTE_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"高于指导价");
            put(2,"低于指导价");
        }
    };
    //endregion

    //region AUTO_CAR_SALE_PRICE_TYPE
    public static final Integer AUTO_CAR_SALE_PRICE_TYPE_MONEY = 1;
    public static final Integer AUTO_CAR_SALE_PRICE_TYPE_PERCENT = 2;
    public static final Map<Integer, String> MAP_AUTO_CAR_SALE_PRICE_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"价格优惠");
            put(2,"点数优惠");
        }
    };
    //endregion

    //region AUTO_CAR_TYPE
    public static final Integer AUTO_CAR_INVOICE_TYPE_COMMON = 1;
    public static final Integer AUTO_CAR_INVOICE_TYPE_VAT = 2;
    public static final Map<Integer, String> MAP_AUTO_CAR_INVOICE_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"普通发票");
            put(2,"增值税发票");
        }
    };
    //endregion

    public static final Map<Integer, String> MAP_AUTO_ATTR_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"单选框");
            put(2,"复选框");
            put(3,"多选菜单");
            put(4,"单行文本");
            put(5,"多行文本");
        }
    };

    public static final Map<Integer, String> MAP_AUTO_SMS_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"注册验证码");
            put(2,"找回密码");
            put(3,"修改密码");
            put(4,"通知短信");
        }
    };

    public static final Integer DEFAULT_ROLE_TYPE = 2;

    public static final Integer DEFAULT_SMS_TYPE = 4;


    //region 订单
    /*
        item_name   		order_status     		pay_status    		shipping_status           buyer actions                                   seller_actions                          admin
    1. 	提交订单    			买家已提交订单			等待买家付订金        未提车                     cancel  payment                                                                         cancel payment

    2. 	买家支付     			买家已提交订单			买家已支付订金	    未提车                     cancel                                          accept reject                           cancel accept reject unpayment

    3A. 卖家同意并支付      	交易中                   卖家已支付订金   	    未提车                     request_cancel receive contact_seller           notify_receive contact_buyer            cancel unaccept notify_receive receive

    3B.	买家拒绝订单         	交易拒绝                 买家已支付订金        未提车

    4. 	买家确认收货         	交易成功                 等待系统退还定金       已提车                     contact_seller                                 contact_buyer                           return_buyer_deposit return_buyer_deposit

    5. 	系统退还订金         	交易成功                 定金已经退还          已提车                     contact_seller                                  contact_buyer

    6. 	管理员取消订单       	交易取消                 定金已经退还          未提车                                                                                                             return_buyer_deposit return_buyer_deposit
    */

    /**
     * 订单操作(取消订单,设为订金已支付,设为订金未支付,同意交易,拒绝交易,请求取消订单,联系买家,联系卖家,确认收货,通知买家收货,退还买家订金,退还卖家订金)
     */
    public static final String OO_CANCEL = "cancel";
    public static final String OO_PAYMENT = "payment";
    public static final String OO_UNPAYMENT = "unpayment";
    public static final String OO_ACCEPT = "accept";
    public static final String OO_UNACCEPT = "unaccept";
    public static final String OO_REJECT = "reject";
    public static final String OO_REQUEST_CANCEL = "request_cancel";
    public static final String OO_CONTACT_BUYER = "contact_buyer";
    public static final String OO_CONTACT_SELLER = "contact_seller";
    public static final String OO_RECEIVE = "receive";
    public static final String OO_NOTIFY_RECEIVE = "notify_receive";
    public static final String OO_RETURN_BUYER_DEPOSIT = "return_buyer_deposit";
    public static final String OO_RETURN_SELLER_DEPOSIT = "return_seller_deposit";

    public static final Map<String, String> OO_OPERATE = new HashMap<String, String>(){
        {
            put("cancel","取消订单");
            put("payment","设为买家订金已支付");
            put("unpayment","设为买家订金未支付");
            put("accept","设为卖家同意交易");
            put("unaccept","设为卖家取消交易");
            put("reject","设为卖家拒绝交易");
            put("request_cancel","请求取消订单");
            put("contact_buyer","联系买家");
            put("contact_seller","联系卖家");
            put("receive","确认收货");
            put("notify_receive","通知买家收货");
            put("return_buyer_deposit","退还买家订金");
            put("return_seller_deposit","退还卖家订金");
        }
    };

    public static final Map<String, String> OO_ACTIONS = new HashMap<String, String>(){
        {
            put("取消订单","cancel");
            put("设为买家订金已支付","payment");
            put("设为买家订金未支付","unpayment");
            put("设为卖家同意交易","accept");
            put("设为卖家取消交易","unaccept");
            put("设为卖家拒绝交易","reject");
            put("请求取消订单","request_cancel");
            put("联系买家","contact_buyer");
            put("联系卖家","contact_seller");
            put("确认收货","receive");
            put("通知买家收货","notify_receive");
            put("退还买家订金","return_buyer_deposit");
            put("退还卖家订金","return_seller_deposit");
        }
    };

    /**
     * 订单状态(买家已提交,卖家已同意,卖家不同意,交易中,交易成功,交易取消)
     */
    public static final Integer OS_SUBMIT = 1;
    public static final Integer OS_EXECUTE = 2;
    public static final Integer OS_SUCCESS = 3;
    public static final Integer OS_CANCEL = 0;

    public static final  Map<Integer,String> ORDER_STATUS=new HashMap<Integer, String>(){
        {
            put(1,"买家已提交");
            put(2,"交易中");
            put(3,"交易成功");
            put(0,"交易取消");
        }
    };

    /**
     * 支付状态(等待买家支付订金,买家已支付订金,等待卖家支付订金,卖家已支付订金,等待系统退还订金,系统已退还订金)
     */
    public static final Integer PS_WAIT_BUYER_DEPOSIT = 1;
    public static final Integer PS_BUYER_PAY_DEPOSIT = 2;
    public static final Integer PS_WAIT_SELLER_DEPOSIT = 3;
    public static final Integer PS_SELLER_PAY_DEPOSIT = 4;
    public static final Integer PS_WAIT_SYSTEM_DEPOSIT = 5;
    public static final Integer PS_SYSTEM_RETURN_DEPOSIT = 6;

    public static final Map<Integer, String> PAY_STATUS=new HashMap<Integer, String>(){
        {
            put(1,"等待买家支付订金");
            put(2,"买家已支付订金");
            put(3,"等待卖家支付订金");
            put(4,"卖家已支付订金");
            put(5,"等待系统退还订金");
            put(6,"系统已退还订金");
        }
    };

    /**
     * 物流状态(未提车,已提车)
     */
    public static final Integer SS_UNSHIP = 1;
    public static final Integer SS_SHIPED = 2;

    public static final Map<Integer, String> SHIP_STATUS=new HashMap<Integer, String>(){
        {
            put(1,"未提车");
            put(2,"已提车");
        }
    };

    /**
     * 客户端状态(未支付订金,已支付订金,已成交,已取消)
     */
    public static final Integer CS_ALL = 0;
    public static final Integer CS_UNDEPOSIT = 1;
    public static final Integer CS_DEPOSIT = 2;
    public static final Integer CS_SUCCESS = 3;
    public static final Integer CS_CANCEL = 4;

    /**
     * 交易用户(双方，买家，卖家，平台)
     */
    public static final Integer ORDER_USER_ALL = 0;
    public static final Integer ORDER_USER_BUYER = 1;
    public static final Integer ORDER_USER_SELLER = 2;
    public static final Integer ORDER_USER_ADMIN = 3;

    /**
     * 支付方式(微信支付，支付宝支付)
     */
    public static final Integer ORDER_PAYMENT_WXPAY = 1;
    public static final Integer ORDER_PAYMENT_ALIPAY = 2;

    //endregion


    //region System User Status
    public static final Integer SYSTEM_USER_STATUS_VALID = 1;
    public static final Integer SYSTEM_USER_STATUS_INVALID = 2;
    public static final Map<Integer, String> MAP_SYSTEM_USER_STATUS = new HashMap<Integer, String>(){
        {
            put(1,"有效");
            put(2,"无效");
        }
    };
    //endregion

    public static final Integer AUTO_AUTHORITY_TARGET_TYPE_MENU = 1;
    public static final Integer AUTO_AUTHORITY_TARGET_TYPE_ACTION = 2;

    //上拉刷新
    public static final Integer PULLREFRESH_UP = 1;

    //下拉加载
    public static final Integer PULLREFRESH_DOWN = 2;

    public class UrlHelper{
        public static final String PARAM_RETURN_URL = "ReturnUrl";

        /**
         *  系统管理模块
         */

        public static final String ADMIN_SYSTEM_LOGIN = "admin/login";

        public static final String ADMIN_SYSTEM_MAIN = "admin/main";

        public static final String ADMIN_SYSTEM_INDEX = "admin/index";

        public static final String ADMIN_SYSTEM_MENU = "admin/system/menu/index";

        public static final String ADMIN_SYSTEM_LOG = "admin/system/log/index";

        public static final String ADMIN_SYSTEM_ROLE = "admin/system/role/index";

        public static final String ADMIN_SYSTEM_USER = "admin/system/user/index";

        public static final String ADMIN_SYSTEM_USER_GROUP = "admin/system/usergroup/index";

        public static final String ADMIN_SYSTEM_CONFIG = "admin/system/config/index";

        public static final String ADMIN_SYSTEM_DEPT = "admin/system/dept/index";

        public static final String ADMIN_SYSTEM_AUTHORITY = "admin/system/authority/index";

        public static final String ADMIN_SYSTEM_MENU_ACTION = "admin/system/menuaction/index";

        /**
         *  业务管理模块
         */

        public static final String ADMIN_AUTO_USER = "admin/auto/user/index";

        public static final String ADMIN_AUTO_USER_ADD = "admin/auto/user/add";

        public static final String ADMIN_AUTO_USER_EDIT = "admin/auto/user/edit";

        public static final String ADMIN_AUTO_USER_SHOW = "admin/auto/user/show";

        public static final String ADMIN_AUTO_ARTICLE = "admin/auto/article/index";

        public static final String ADMIN_AUTO_ARTICLE_ADD = "admin/auto/article/add";

        public static final String ADMIN_AUTO_ARTICLE_CAT = "admin/auto/articlecat/index";

        public static final String ADMIN_AUTO_ARTICLE_TEMPLATE = "admin/auto/articletemplate/index";

        public static final String ADMIN_AUTO_COLLECT = "admin/auto/collect/index";

        public static final String ADMIN_AUTO_FEEDBACK = "admin/auto/feedback/index";

        public static final String ADMIN_AUTO_MSG = "admin/auto/msg/index";

        public static final String ADMIN_AUTO_BRAND = "admin/auto/brand/index";

        public static final String ADMIN_AUTO_BRAND_CAT = "admin/auto/brandcat/index";

        public static final String ADMIN_AUTO_BRAND_MODEL = "admin/auto/brandmodel/index";

        public static final String ADMIN_AUTO_AD = "admin/auto/ad/index";

        public static final String ADMIN_AUTO_CAR = "admin/auto/car/index";

        public static final String ADMIN_AUTO_CAR_SHOW = "admin/auto/car/show";

        public static final String ADMIN_AUTO_CAR_ADD = "admin/auto/car/add";

        public static final String ADMIN_AUTO_AD_POSITION = "admin/auto/adposition/index";

        public static final String ADMIN_AUTO_SMS = "admin/auto/sms/index";

        public static final String ADMIN_AUTO_ORDER_SHOW = "admin/auto/order/show";

        public static final String ADMIN_AUTO_ORDER = "admin/auto/order/index";
    }

    public class PageHelper{
        public static final String PAGE_NAME = "PAGE";

        public static final int PAGE_SIZE_COMMON = 20;

        public static final int PAGE_INDEX_FIRST = 1;

        public static final int PAGE_SIZE_MAX = Integer.MAX_VALUE;

        public static final String PAGE_ACTION_CREATE = "CREATE";

        public static final String PAGE_ACTION_UPDATE = "UPDATE";

        public static final String PAGE_ACTION_DELETE = "DELETE";

        public static final String PAGE_ACTION_SELECT = "SELECT";

        public static final String PAGE_ACTION_IMPORT = "IMPORT";

        public static final String PAGE_ACTION_EXPORT = "EXPORT";

        public static final String PAGE_ACTION_SEARCH = "SEARCH";

        public static final String PAGE_ACTION_SAVE = "save";
    }
}
