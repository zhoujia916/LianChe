package puzzle.lianche;

import org.springframework.util.Assert;
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

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

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
    }

    @Test
    public void phoneAutoUserSendCode() throws Exception{
        mockMvc.perform(post("/phone/autouser/sendCode.do"))
                .andExpect(status().isOk()).andDo(print());

        mockMvc.perform(post("/phone/autouser/sendCode.do").param("phone", "123456"))
                .andExpect(status().isOk()).andDo(print());

        mockMvc.perform(post("/phone/autouser/sendCode.do").param("phone", "13501656316"))
                .andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void phoneAutoUserRegister() throws Exception{
        mockMvc.perform(post("/phone/autouser/register.do").param("userName", "13501656316").param("password", "111111").param("code", "123456"))
                .andExpect(status().isOk()).andDo(print());
    }
}