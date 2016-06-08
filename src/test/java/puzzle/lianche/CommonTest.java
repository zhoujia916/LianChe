package puzzle.lianche;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import puzzle.lianche.entity.*;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.service.*;
import puzzle.lianche.service.impl.AutoBrandServiceImpl;
import puzzle.lianche.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-*.xml"})
public class CommonTest {
    private static Logger logger = LoggerFactory.getLogger(CommonTest.class);

    private ApplicationContext ctx;

    private ISystemUserService systemUserService;

    private IAutoCarService autoCarService;


    private SqlMapper sqlMapper;


    //手动方式加载spring配置
	@Before
	public void before() {
        ctx = new ClassPathXmlApplicationContext("classpath:spring-main.xml", "classpath:spring-mybatis.xml", "classpath:spring-mvc.xml");

        systemUserService = (ISystemUserService)ctx.getBean("systemUserService");

        autoBrandService = (IAutoBrandService)ctx.getBean("autoBrandService");

        autoBrandCatService = (IAutoBrandCatService)ctx.getBean("autoBrandCatService");

        autoBrandModelService = (IAutoBrandModelService)ctx.getBean("autoBrandModelService");

        autoCarService = (IAutoCarService)ctx.getBean("autoCarService");

        sqlMapper = (SqlMapper)ctx.getBean("sqlMapper");

    }

//    @Test
//    public void testSystemMenu(){
//        List<SystemMenu> list = systemMenuService.queryList(null);
//        for(SystemMenu item : list){
//            System.out.println(item.getActions());
//        }
//        logger.info(JSONArray.fromObject(list).toString());
//    }
//
//    @Test
//    public void testMemory(){
//        System.gc();
//        long total = Runtime.getRuntime().totalMemory(); // byte
//        long m1 = Runtime.getRuntime().freeMemory();
//        long before = total - m1;
//        System.out.println("before:" + before);
//        SystemMenu menu = new SystemMenu();
//        menu.setMenuName("asdfasofdasdf");
//        menu.setMenuId(5);
//        menu.setMenuUrl("asfdasddddddddddddddddddddddddddddddddddddddddddd");
//        menu.setParentId(6);
//        menu.setSortOrder(6);
//
//        long total1 = Runtime.getRuntime().totalMemory();
//        long m2 = Runtime.getRuntime().freeMemory();
//        long after = total1 - m2;
//        System.out.println("after:" +after);
//
//
//        System.out.println("size:" + (after - before));
//    }
//    @Autowired
    private IAutoBrandService autoBrandService;

//    @Autowired
    private IAutoBrandCatService autoBrandCatService;

//    @Autowired
    private IAutoBrandModelService autoBrandModelService;

    
//    @Test
//    public void readBrand(){
//        //String json = FileUtil.readFile("D:\\brand.js");
//        String json = "[]";
//        //System.out.println(json);
//        if(StringUtil.isNotNullOrEmpty(json)) {
//            JSONArray array = JSONArray.fromObject(json);
//            int index = 99;
//            for(int i = 0; i < array.size(); i++){
//                index++;
//                AutoBrand brand = new AutoBrand();
//                JSONObject jsonBrand = (JSONObject)array.get(i);
//                brand.setBrandName(jsonBrand.getString("N"));
//                brand.setFirstLetter(jsonBrand.getString("L"));
//                brand.setSortOrder(index);
//                autoBrandService.insert(brand);
//                JSONArray cats = jsonBrand.getJSONArray("List");
//                for(int j = 0; j < cats.size(); j++){
//                    AutoBrandCat cat = new AutoBrandCat();
//                    JSONObject jsonCate = (JSONObject)cats.get(j);
//                    cat.setBrandId(brand.getBrandId());
//
//                    JSONArray subCats = jsonCate.getJSONArray("List");
//                    for(int k = 0; k < subCats.size(); k++){
//                        JSONObject jsonSubCate = (JSONObject)subCats.get(k);
//                        cat.setCatName(jsonCate.getString("N") + "---" + jsonSubCate.getString("N"));
//                        cat.setSortOrder(j + k + 1);
//                        cat.setRecId(jsonSubCate.getInt("I"));
//                        autoBrandCatService.insert(cat);
//                    }
//                }
//            }
//        }
//    }

//    @Test
//    public void readBrandModel(){
//        final String url = "http://car.autohome.com.cn/duibi/ashx/SpecCompareHandler.ashx?type=2&isie6=0&seriesid=";
//        List<AutoBrandCat> list = autoBrandCatService.queryList(null);
//        HttpUtil http = new HttpUtil(HttpUtil.HTTP_GET);
//        http.setResponseCode("gb2312");
//        long total = list.size();
//        long finished = 0;
//        for(AutoBrandCat cat : list){
//            http.setUrl(url + cat.getRecId());
//            if(http.request()){
//                JSONObject jsonObject = JSONObject.fromObject(http.getResponse());
//                JSONArray array1 = jsonObject.getJSONArray("List");
//                for(int i = 0; i < array1.size(); i++){
//                    JSONArray array2 = ((JSONObject)array1.get(i)).getJSONArray("List");
//                    for(int j = 0; j < array2.size(); j++){
//                        JSONArray array3 = ((JSONObject)array2.get(j)).getJSONArray("List");
//                        for(int k = 0; k < array3.size(); k++){
//                            AutoBrandModel model = new AutoBrandModel();
//                            model.setBrandCatId(cat.getCatId());
//                            model.setSortOrder(i+j+k+1);
//                            model.setModelName(((JSONObject)array3.get(k)).getString("N"));
//                            autoBrandModelService.insert(model);
//                        }
//                    }
//
//                }
//                finished++;
//                System.out.println("process:" + finished + "/" + total);
//            }
//        }
//    }

    @Test
    public void queryBrand(){
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("carId", 5);
        AutoCar car = autoCarService.query(map);

        System.out.println(car);
    }

}