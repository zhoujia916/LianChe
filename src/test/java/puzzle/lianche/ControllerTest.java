package puzzle.lianche;

import java.awt.print.Printable;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.Assert;
import puzzle.lianche.controller.UserController;
import puzzle.lianche.service.ISystemUserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath*:spring-*.xml"})
public class ControllerTest {
    private static Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    private WebApplicationContext wac;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        try {

            this.mockMvc = webAppContextSetup(wac).build();
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userService).build();
    }

    @Test
//    @Rollback(false)  //有些单元测试你不希望回滚
    public void testAdminLogin() throws Exception{
//        mockMvc.perform((get("/admin/"))).andExpect(status().isOk())
//                .andDo(print());

//        mockMvc.perform((get("/admin/login"))).andExpect(status().isOk())
//                .andDo(print());
//
//        mockMvc.perform((get("/admin/index"))).andExpect(status().is3xxRedirection())
//                .andDo(print());
    }

    @Test
    public void testAdminMenu() throws Exception {
        String src = "/179,153";
        String pattern = "/[\\d,]+";
        Assert.isTrue(src.matches(pattern));
    }
//
//    @Test
//    public void test3() throws Exception {
//        mockMvc.perform((get("/user/testb.do"))).andExpect(status().isOk())
//                .andDo(print());
//
//
//    @Test
//    public void test4() throws Exception {
//        mockMvc.perform((post("/spring/post.do").param("abc", "def")))
//                .andExpect(status().isOk()).andDo(print());
//    }

    @Test
    @Rollback(false)
    public void testPhoneUser() throws Exception {
//        mockMvc.perform(((post("/phone/autouser/register.do").param("userName", "13658473085").param("password","111111")).param("code","743996")))
//        mockMvc.perform(((post("/phone/autouser/sendCode.do").param("phone", "13658473085").param("keyword","register"))))
//        mockMvc.perform(((post("/phone/autouser/login.do").param("username", "13658473085").param("password","111111"))))
        mockMvc.perform(((post("/phone/autouser/prefectUser.do").param("realName", "李建生").param("phone","13658473085")).param("shopType","1").param("shopName","铜仁土特产代购").param("shopDesc","zxczxczxca").param("shopBrands","asdsad").param("shopBase","asdsad")))
                .andExpect(status().isOk()).andDo(print());
    }
}