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

    public static final Map<Integer, String> USER_SEX = new HashMap<Integer, String>(){
        {
            put(1,"男");
            put(2,"女");
        }
    };

    public static final Map<Integer, String> LOG_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"查看");
            put(2,"增加");
            put(3,"删除");
            put(4,"修改");
            put(5,"导入");
            put(6,"导出");
        }
    };

    public static final Map<Integer, String> ROLE_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"系统角色");
            put(2,"自定义角色");
        }
    };

    public static final Integer SMS_TYPE_CODE = 1;
    public static final Integer SMS_TYPE_CONTENT = 2;
    public static final Integer SMS_STATUS_TRUE=1;
    public static final Integer SMS_STATUS_FALSE=2;
    public static final Integer USER_MAP_TARGET_TYPE_ROLE = 1;
    public static final Integer USER_MAP_TARGET_TYPE_GROUP = 2;
    public static final Integer USER_MAP_TARGET_TYPE_DEPT = 3;

    public static final Map<String, Integer> MAP_LOG_TYPE = new HashMap<String, Integer>(){
        {
            put("SELECT",1);
            put("CREATE",2);
            put("DELETE",3);
            put("UPDATE",4);
        }
    };

    public static final Integer AUTO_USER_STATUS_DISABLED = 0;
    public static final Integer AUTO_USER_STATUS_REGISTERORADD = 1;
    public static final Integer AUTO_USER_STATUS_WAITAUTHENTICATION = 2;
    public static final Integer AUTO_USER_STATUS_AUTHENTICATIONADOPT = 3;
    public static final Integer AUTO_USER_STATUS_AUTHENTICATIONNOADOPT = 4;

    public static final Integer SYSTEM_AUTHORITY_TARGET_MENU = 1;
    public static final Integer SYSTEM_AUTHORITY_TARGET_ACTION = 2;

    public static final  Map<Integer, String> AUTO_USER_STATUS = new HashMap<Integer, String>(){
        {
            put(0,"账户禁用");
            put(1,"注册或添加");
            put(2,"等待实名认证");
            put(3,"实名认证通过");
            put(4,"实名认证未通过");
        }
    };

    public static final Map<Integer, String> AUTO_USER_SHOP_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"4s店");
            put(2,"有实体店二网");
            put(3,"无实体店二网");
            put(4,"个人");
        }
    };

    public static final Map<Integer, String> AUTO_ARTICLE_STATUS = new HashMap<Integer, String>(){
        {
            put(0,"已保存");
            put(1,"已提交");
            put(2,"已审核");
        }
    };

    public static final Map<Integer, String> AUTO_ARTICLE_USER_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"前台用户");
            put(2,"后台用户");
        }
    };

    public static final Map<Integer, String> AUTO_MSG_TYPE = new HashMap<Integer, String>(){
        {
            put(0,"系统发给用户的消息");
            put(1,"用户发给用户的消息");
        }
    };

    public static final Map<Integer, String> AUTO_COLLECT_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"销车资源");
            put(2,"特卖会");
        }
    };

    public static final Map<Integer, String> AUTO_COLLECT_STATUS = new HashMap<Integer, String>(){
        {
            put(0,"已删除");
            put(1,"正常");
        }
    };

    public static final Map<Integer, String> AUTO_CAR_TYPE = new HashMap<Integer, String>(){
        {
            put(0,"销车资源");
            put(1,"特卖会");
        }
    };

    public static final Map<Integer, String> AUTO_ATTR_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"单选框");
            put(2,"复选框");
            put(3,"多选菜单");
            put(4,"单行文本");
            put(5,"多行文本");
        }
    };

    public static final Map<Integer, String> AUTO_SMS_TYPE = new HashMap<Integer, String>(){
        {
            put(1,"注册验证码");
            put(2,"找回密码");
            put(3,"修改密码");
            put(4,"通知短信");
        }
    };

    public static final Map<Integer, String> AUTO_SMS_STATUS = new HashMap<Integer, String>(){
        {
            put(1,"发送成功");
            put(2,"发送失败");;
        }
    };

    public static final Integer DEFAULT_ROLE_TYPE = 2;

    public static final Integer DEFAULT_SMS_TYPE = 4;


    //region 订单
    /*
        item_name   		order_status     		pay_status    		shipping_status
    1. 	提交订单    			买家已提交订单			等待买家付订金        未提车

    2. 	买家支付     			买家已提交订单			买家已支付订金	    未提车

    3. 	卖家同意订单         	卖家已同意交易            等待卖家付订金    	未提车

    3A.	买家拒绝订单         	交易取消                 买家已支付订金        未提车

    4. 	卖家支付      		交易中	        		卖家已支付订金        未提车

    5. 	买家确认收货         	交易成功                 等待系统退还定金       已提车

    6. 	系统退还订金         	交易成功                 定金已经退还          已提车

    7. 	管理员取消订单       	交易取消                 定金已经退还          未提车
    */


    /**
     * 订单状态(买家已提交,卖家已同意,卖家不同意,交易中,交易成功,交易取消)
     */
    public static final Integer OS_SUBMIT = 1;
    public static final Integer OS_ACCEPT = 2;
    public static final Integer OS_REJECT = 3;
    public static final Integer OS_EXECUTE = 4;
    public static final Integer OS_SUCCESS = 5;
    public static final Integer OS_CANCEL = 0;

    /**
     * 支付状态(等待买家支付订金,买家已支付订金,等待卖家支付订金,卖家已支付订金,等待系统退还订金,系统已退还订金)
     */
    public static final Integer PS_WAIT_BUYER_DEPOSIT = 1;
    public static final Integer PS_BUYER_PAY_DEPOSIT = 2;
    public static final Integer PS_WAIT_SELLER_DEPOSIT = 3;
    public static final Integer PS_SELLER_PAY_DEPOSIT = 4;
    public static final Integer PS_WAIT_RETURN_DEPOSIT = 5;
    public static final Integer PS_SYSTEM_RETURN_DEPOSIT = 6;

    /**
     * 物流状态(未提车,已提车)
     */
    public static final Integer SS_UNSHIP = 1;
    public static final Integer SS_SHIPED = 2;

    //endregion


    public class UrlHelper{
        public static final String RETURN_URL = "ReturnUrl";

        public static final String USER_LOGIN = "user_login";

        public static final String USER_LIST="user_list";

        public static final String USER_DETAIL = "user_detail";

        public static final String ADMIN_LOGIN = "admin/login";

        public static final String ADMIN_MAIN = "admin/main";

        public static final String ADMIN_INDEX = "admin/index";

        public static final String ADMIN_MENU = "admin/menu/index";

        public static final String ADMIN_LOG = "admin/log/index";

        public static final String ADMIN_ROLE = "admin/role/index";

        public static final String ADMIN_USER = "admin/user/index";

        public static final String ADMIN_USER_GROUP = "admin/userGroup/index";

        public static final String ADMIN_MENU_SHOW = "admin/menu/show";

        public static final String ADMIN_ROLE_SHOW = "admin/role/show";

        public static final String ADMIN_USER_SHOW = "admin/user/show";

        public static final String ADMIN_CONFIG = "admin/config/index";

        public static final String ADMIN_AUTO_USER = "admin/autoUser/index";

        public static final String ADMIN_AUTO_ARTICLE = "admin/article/index";

        public static final String ADMIN_ARTICLE_CAT = "admin/articleCat/index";

        public static final String ADMIN_ARTICLE_TEMPLATE = "admin/articletemplate/index";

        public static final String ADMIN_AUTO_USER_COLLECTION = "admin/autousercollect/index";

        public static final String ADMIN_AUTO_FEEDBACK = "admin/feedback/index";

        public static final String ADMIN_AUTO_MSG = "admin/msg/index";

        public static final String ADMIN_BRAND = "admin/brand/index";

        public static final String ADMIN_BRAND_CAT = "admin/brandcat/index";

        public static final String ADMIN_BRAND_MODEL = "admin/brandmodel/index";

        public static final String ADMIN_AD = "admin/ad/index";

        public static final String ADMIN_CAR = "admin/car/index";

        public static final String INDEX = "index";

        public static final String ADMIN_DEPT = "admin/dept/index";

        public static final String ADMIN_AUTHORITY = "admin/authority/index";

        public static final String ADMIN_MENU_ACTION = "admin/menuaction/index";

        public static final String ADMIN_ADPOSITION = "admin/adposition/index";

        public static final String ADMIN_ATTR = "admin/attr/index";

        public static final String ADMIN_SMS = "admin/sms/index";
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
