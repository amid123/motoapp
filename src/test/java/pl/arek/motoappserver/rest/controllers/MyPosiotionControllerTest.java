/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.arek.motoappserver.rest.controllers;

import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.arek.motoappserver.configuration.app.OtherComponentsConfiguration;
import pl.arek.motoappserver.configuration.app.RestConfiguration;
import pl.arek.motoappserver.configuration.app.ServicesConfiguration;
import pl.arek.motoappserver.configuration.app.TestConfiguration;
import pl.arek.motoappserver.domain.UserPosition;
import pl.arek.motoappserver.domain.SystemResponse;

/**
 *
 * @author Arkadiusz Gibes <arkadiusz.gibes@yahoo.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)

@ContextConfiguration(classes = {
    TestConfiguration.class,
    RestConfiguration.class,
    //SecurityConfiguration.class,
    OtherComponentsConfiguration.class,
    ServicesConfiguration.class
})

@WebAppConfiguration
@ActiveProfiles(profiles = {"test", "main"})
public class MyPosiotionControllerTest {

    private static final Logger logger = Logger.getLogger(MyPosiotionControllerTest.class.getName());

    @Autowired
    MyPosiotionController myPosiotionController;

    @Autowired
    Gson gson;

    private MockMvc mockMvc;
    private UserPosition position;
    private String myJson;
    private MvcResult result;

    public MyPosiotionControllerTest() {
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(myPosiotionController).build();

        position = new UserPosition();
        position.setLatitude(122.31);
        position.setLongitude(3252.31);
        myJson = this.gson.toJson(position);
    }

    @After
    public void tearDown() throws UnsupportedEncodingException {

        MockHttpServletResponse response;
        response = result.getResponse();

        SystemResponse myResponse = gson.fromJson(response.getContentAsString(), SystemResponse.class);
        logger.log(Level.SEVERE, "Response from MyPositionController after transform back from Json to POJO is: " + myResponse);
    }

    /**
     * Test of saveMyPosition method, of class MyPosiotionController.
     */
    @Test
    public void testSaveMyPosition() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/myposition/save")
                .content(myJson)
                .contentType(MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

    }
}
