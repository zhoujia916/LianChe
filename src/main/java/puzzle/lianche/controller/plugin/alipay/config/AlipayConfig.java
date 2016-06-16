package puzzle.lianche.controller.plugin.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088121255400017";
    // 买家帐号，一般就是合作者ID
    public static String seller = partner;
    // 支付宝账户
    public static String account = "xicheninfo@163.com";
    // 通知账户
    public static String notify_url = "http://test.one-auto.com/alipay/notify";

	// 商户的私钥
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL/LzhtqbP44IU9MRI88sx2Q7ZSnN1yukrmRluJwW9aYNpSx1lyskZG8v3+4bkL+8dbuzJOeR3n8Dxb55KzTmrqEjD3QV05hIBcSEFRhPz75FmqfiqocL8h/JDWbZTJwVae2gHCzAhfWgGEnJYPusOgpvkt4aUEfBMLe00eVN68XAgMBAAECgYBzQi7iGtrVCzY5SUQKMBgTtHyR0MtcTyyth+h8u7j1BRQH17dSigIQ6rTKNDNO35bF60vVilC55dfLnvDlS2S3ly7OZZ0xjObLrXVrhkuB7HRHWZCaTrJBxqCaB8f9iWum57fZqjiMRqybLLngt3aN97IRyAUXCk3b6RunsmfTwQJBAOmOSqIFwZg8TRR65hNP+SaFv7+AONs0NFo9olc4fYV5ieILemx/Ina2cMyOybrInWsvIaUXCMa6/7P1Ak3yoUsCQQDSOi6+wOOcynX7Sd4HsBcgRBj5ktC7XOvPz9i8pbrjan4dDuAcEIO0mK6dvbFNSdy41sJHPQKHKTiyLVvOrtXlAkBcMY0IdBk9jOAoa3MSwIjrSfStFWJcNTlNPfMtVuFFpPpmvn0vAuiJz+6q8Np03ug0/T/cuUD/oaV6Vgb42gQNAkEAhQnw/yKOaHDvzQDoE7FqUS8HJxm0In4hdTexj2DKLpT8DmOVi+0fjDB7gLPE+oZdulOeSvrfGK4LhSaTTO7zyQJAPBnV72w1p8u6pC3wICMSiY/TFUOFOcbesuBRssEo4nzLODVukP1sAYqhNWGMl2OegWNkahn2SemYVwrnloZKlQ==";
    public static String private_key_path = "key\\rsa_private_key.pem";
	
	// 支付宝的公钥，无需修改该值
	public static String public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    public static String public_key_path = "key\\rsa_public_key.pem";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";


//    a) Java与。Net开发语言
//    ◆商户的私钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、去掉“-----BEGIN RSA PRIVATE KEY-----”、“-----END RSA PRIVATE KEY-----”，只保存这两条文字之中的部分
//    ◆商户的公钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、去掉“-----BEGIN PUBLIC KEY-----”、“-----END PUBLIC KEY-----”，只保存这两条文字之中的部分
//    3、保存到一个临时的记事本中，再打开b.alipay.com，并用要绑定密钥的支付宝账号登录，找到“获取PID、KEY”的按钮
//    4、根据http://help.alipay.com/support/help_detail.htm?help_id=243726&sh=Y&tab=null&info_type=9里的步骤上传RSA公钥，即刚才保存在临时记事本中的那串字符串。
//
//            ◆支付宝公钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、去掉“-----BEGIN PUBLIC KEY-----”、“-----END PUBLIC KEY-----”，只保存这两条文字之中的部分
//    a) Php开发语言
//    ◆商户的私钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、不需要对刚生成的（原始的）私钥做pkcs8编码，即不需要使用到PKCS8格式的私钥
//    3、不需要去掉去掉“-----BEGIN PUBLIC KEY-----”、“-----END PUBLIC KEY-----”
//    简言之，只要维持刚生成出来的私钥的内容即可。
//            ◆商户的公钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、去掉“-----BEGIN PUBLIC KEY-----”、“-----END PUBLIC KEY-----”，只保存这两条文字之中的部分
//    3、保存到一个临时的记事本中，再打开b.alipay.com，并用要绑定密钥的支付宝账号登录，找到“获取PID、KEY”的按钮
//    4、根据http://help.alipay.com/support/help_detail.htm?help_id=243726&sh=Y&tab=null&info_type=9里的步骤上传RSA公钥，即刚才保存在临时记事本中的那串字符串。
//            ◆支付宝公钥
//    1、必须保证只有一行文字，即，没有回车、换行、空格等
//    2、须保留“-----BEGIN PUBLIC KEY-----”、“-----END PUBLIC KEY-----”这两条文字。
//    简言之，支付宝公钥只需要维持原样即可。
//            2） 把公钥上传给支付宝
//    操作流程见：http://help.alipay.com/support/help_detail.htm?help_id=243726&sh=Y&tab=null&info_type=9
//            3） 从支付宝那获得支付宝公钥
//    此处公钥由技术支持提供，请找技术支持索要支付宝公钥文件。

}
