package puzzle.lianche;

import org.aspectj.lang.annotation.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.XmlWebApplicationContext;
import puzzle.lianche.entity.AutoBrand;
import puzzle.lianche.entity.AutoBrandCat;
import puzzle.lianche.entity.SystemConfig;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.service.IAutoBrandCatService;
import puzzle.lianche.service.IAutoBrandService;
import puzzle.lianche.service.impl.AutoBrandCatServiceImpl;
import puzzle.lianche.utils.EncryptUtil;
import puzzle.lianche.utils.RSAUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring-*.xml"})
public class CommonTest {

    SqlMapper mapper;

//    @Test
//    public void testBase64(){
//        String imgFile = "C:\\Users\\IBM-Thinkpad\\Desktop\\temp2.jpg";
//        InputStream in = null;
//        byte[] data = null;
//        //读取图片字节数组
//        try
//        {
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        //对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        String base64 = encoder.encode(data);
////        String base64 = new String(data);
//        System.out.println(base64);
//
//        BASE64Decoder decoder = new BASE64Decoder();
//        try
//        {
//            //Base64解码
//            byte[] b = decoder.decodeBuffer(base64);
//            for(int i=0;i<b.length;++i){
//                if(b[i]<0)
//                {//调整异常数据
//                    b[i]+=256;
//                }
//            }
//            //生成jpeg图片
//            String imgFilePath = "C:\\Users\\IBM-Thinkpad\\Desktop\\temp2(1).jpg";//新生成的图片
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(b);
//            out.flush();
//            out.close();
//        }
//        catch (Exception e)
//        {
//        }
//    }

//    @Test
//    public void test2(){
//        String imgFile = "C:\\Users\\IBM-Thinkpad\\Desktop\\txt.txt";
//        InputStream in = null;
//        byte[] data = null;
//        try
//        {
//            in = new FileInputStream(imgFile);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//        StringBuffer sb = new StringBuffer(new String(data));
//        int count = sb.length() / 76;
//        for(int i = count; i > 0; i--){
//            sb.insert(i * 76, '\n');
//        }
//        System.out.println(sb.toString());
//    }

//    @Test
//    public void test3(){
//        Calendar calendar = Calendar.getInstance();
//        System.out.println("Full Year:" + calendar.get(Calendar.YEAR));
//        System.out.println("Year:" + (calendar.get(Calendar.YEAR) % 100));
//        System.out.println("Month:" + (calendar.get(Calendar.MONTH) + 1));
//        System.out.println("Day:" + calendar.get(Calendar.DAY_OF_MONTH));
//        System.out.println("Hour:" + calendar.get(Calendar.HOUR_OF_DAY));
//        System.out.println("hour:" + calendar.get(Calendar.HOUR));
//        System.out.println("Minute:" + calendar.get(Calendar.MINUTE));
//        System.out.println("Second:" + calendar.get(Calendar.SECOND));
//        System.out.println("Minute:" + calendar.get(Calendar.MILLISECOND));
//    }

//    @Test
//    public void testEncrypt(){
//        String text = "这里是需要加密的文字内容。。。。。。。。。阿娇丹佛weurapejrpwe";
//        System.out.println("text:" + text);
//        RSAUtil rsa = new RSAUtil();
//        try {
//            rsa.initKey();
//            String privateKey = rsa.getPrivateKey();
//            String publicKey = rsa.getPublicKey();
//            System.out.println("privateKey:" + privateKey);
//            System.out.println("publicKey:" + publicKey);
//            String data1 = rsa.encrypt(publicKey, text);
//            System.out.println("encrypt text:" + data1);
//            String data2 = rsa.decrypt(privateKey, data1);
//            System.out.println("decrypt text:" + data2);
//
//            System.out.println(data2.equals(text));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }


    @Test
    public void updateBrandCat(){
    }

}