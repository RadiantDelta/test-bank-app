package com.radiantdelta.bankapp.controllers;

import com.radiantdelta.bankapp.SpringAuthApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringAuthApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

class AuthControllerTest {

    @Autowired
    WebApplicationContext context;
    private MockMvc mvc;

    @BeforeEach
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void signupSignin2UsersTransferSuccess() throws Exception {

        String json1 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"88005553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"mail@mail.com\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 1000,\n" +
                "  \"dob\": \"2000-05-23T22:07:44.779Z\",\n" +
                "  \"fio\": \"JackJackson\"\n" +
                "}";

        String json2 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"84955553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"epost@mail.net\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 2000,\n" +
                "  \"dob\": \"2000-03-21T22:07:44.779Z\",\n" +
                "  \"fio\": \"EmilyWilson\"\n" +
                "}";
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json1))
                .andExpect(status().isCreated())
              .andReturn();

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isCreated())
                .andReturn();

        String json12 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\"\n" +
                "}";

        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json12))
                .andExpect(status().isOk())
                .andReturn();

        String json22 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\"\n" +
                "}";

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json22))
                .andExpect(status().isOk())
                .andReturn();

        String token1 = mvcResult1.getResponse().getHeader("Authorization").replace("Bearer ", "");
        String token2 = mvcResult2.getResponse().getHeader("Authorization").replace("Bearer ", "");

        mvc.perform(get("/api/v1/user/transfer?phone={phone}&money={money}",
                        "84955553535", 50)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token1)
//                        .param("84955553535")
//                        .param(500.0f)
                ).andExpect(status().isOk())
                .andReturn();


      //  MvcResult mvcResult3 =

    }

    @Test
    public void signupSignin2UsersTransferMoreThanHave() throws Exception {

        String json1 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"88005553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"mail@mail.com\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 1000,\n" +
                "  \"dob\": \"2000-05-23T22:07:44.779Z\",\n" +
                "  \"fio\": \"JackJackson\"\n" +
                "}";

        String json2 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"84955553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"epost@mail.net\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 2000,\n" +
                "  \"dob\": \"2000-03-21T22:07:44.779Z\",\n" +
                "  \"fio\": \"EmilyWilson\"\n" +
                "}";
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json1))
                .andExpect(status().isCreated())
                .andReturn();

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isCreated())
                .andReturn();

        String json12 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\"\n" +
                "}";

        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json12))
                .andExpect(status().isOk())
                .andReturn();

        String json22 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\"\n" +
                "}";

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json22))
                .andExpect(status().isOk())
                .andReturn();

        String token1 = mvcResult1.getResponse().getHeader("Authorization").replace("Bearer ", "");
        String token2 = mvcResult2.getResponse().getHeader("Authorization").replace("Bearer ", "");

        mvc.perform(get("/api/v1/user/transfer?phone={phone}&money={money}",
                                "84955553535", 534530)
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", token1)
                ).andExpect(status().isForbidden())
                .andReturn();


    }

    @Test
    public void signupSignin2UsersTransferToNotExistUser() throws Exception {

        String json1 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"88005553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"mail@mail.com\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 1000,\n" +
                "  \"dob\": \"2000-05-23T22:07:44.779Z\",\n" +
                "  \"fio\": \"JackJackson\"\n" +
                "}";

        String json2 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\",\n" +
                "  \"phoneList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"phone\": \"84955553535\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"emailList\": [\n" +
                "    {\n" +
                "      \"id\": 0,\n" +
                "      \"email\": \"epost@mail.net\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"amount\": 2000,\n" +
                "  \"dob\": \"2000-03-21T22:07:44.779Z\",\n" +
                "  \"fio\": \"EmilyWilson\"\n" +
                "}";
        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json1))
                .andExpect(status().isCreated())
                .andReturn();

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isCreated())
                .andReturn();

        String json12 = "{\n" +
                "  \"login\": \"user1\",\n" +
                "  \"password\": \"qwertyui\"\n" +
                "}";

        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json12))
                .andExpect(status().isOk())
                .andReturn();

        String json22 = "{\n" +
                "  \"login\": \"user2\",\n" +
                "  \"password\": \"asdfghjk\"\n" +
                "}";

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(json22))
                .andExpect(status().isOk())
                .andReturn();

        String token1 = mvcResult1.getResponse().getHeader("Authorization").replace("Bearer ", "");
        String token2 = mvcResult2.getResponse().getHeader("Authorization").replace("Bearer ", "");

        mvc.perform(get("/api/v1/user/transfer?phone={phone}&money={money}",
                        "84950000000", 50)
                        .contentType(MediaType.APPLICATION_JSON).header("Authorization", token1)
                ).andExpect(status().isNotFound())
                .andReturn();


    }


}