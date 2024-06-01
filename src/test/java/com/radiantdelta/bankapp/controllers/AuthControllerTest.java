package com.radiantdelta.bankapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radiantdelta.bankapp.SpringAuthApplication;
import com.radiantdelta.bankapp.dtos.SignInDto;
import com.radiantdelta.bankapp.dtos.SignUpDto;
import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.entities.Phone;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringAuthApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

class AuthControllerTest {

    @Autowired
    WebApplicationContext context;
    private MockMvc mvc;

    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
    }

    @BeforeEach
    public void initTests() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

    }


    @Test
    public void signupSignin2UsersTransferSuccess() throws Exception {


        // Create a list of Phone objects
        List<Phone> phoneList1 = new ArrayList<>();
        Phone phone1 = new Phone();
        phone1.setPhone("88005553535");
        phoneList1.add(phone1);
        // Create a list of Email objects
        List<Email> emailList1 = new ArrayList<>();
        Email email1 = new Email();
        email1.setEmail("mail@mail.com");
        emailList1.add(email1);

        SignUpDto signUpDto1 = new SignUpDto("user1", "qwertyui",phoneList1,emailList1,1000.0f,
                new Date(2000, 5, 23, 22, 07, 44),
                "JackJackson");
        signUpDto1.setPhoneList(phoneList1);
        signUpDto1.setEmailList(emailList1);

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(signUpDto1)))
                .andExpect(status().isCreated())
              .andReturn();


        // Create a list of Phone objects
        List<Phone> phoneList2 = new ArrayList<>();
        Phone phone2 = new Phone();
        phone2.setPhone("84955553535");
        phoneList2.add(phone2);
        // Create a list of Email objects
        List<Email> emailList2 = new ArrayList<>();
        Email email2 = new Email();
        email2.setEmail("epost@mail.net");
        emailList2.add(email2);

        SignUpDto signUpDto2 = new SignUpDto("user2", "asdfghjk",phoneList2,emailList2,2000.0f,
                new Date(2000, 3, 21, 22, 07, 44),
                "EmilyWilson");
        signUpDto2.setPhoneList(phoneList2);
        signUpDto2.setEmailList(emailList2);


        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpDto2)))
                .andExpect(status().isCreated())
                .andReturn();


        SignInDto signInDto1 = new SignInDto("user1", "qwertyui");


        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto1)))
                .andExpect(status().isOk())
                .andReturn();


        SignInDto signInDto2 = new SignInDto("user2", "asdfghjk");

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto2)))
                .andExpect(status().isOk())
                .andReturn();

        String token1 = mvcResult1.getResponse().getHeader("Authorization").replace("Bearer ", "");
        String token2 = mvcResult2.getResponse().getHeader("Authorization").replace("Bearer ", "");



        mvc.perform(get("/api/v1/user/transfer?phone={phone}&money={money}",
                                signUpDto2.getPhoneList().get(0).getPhone(), 50)
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", token1)
                ).andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void signupSignin2UsersTransferMoreThanHave() throws Exception {


        // Create a list of Phone objects
        List<Phone> phoneList1 = new ArrayList<>();
        Phone phone1 = new Phone();
        phone1.setPhone("88005553535");
        phoneList1.add(phone1);
        // Create a list of Email objects
        List<Email> emailList1 = new ArrayList<>();
        Email email1 = new Email();
        email1.setEmail("mail@mail.com");
        emailList1.add(email1);

        SignUpDto signUpDto1 = new SignUpDto("user1", "qwertyui",phoneList1,emailList1,1000.0f,
                new Date(2000, 5, 23, 22, 07, 44),
                "JackJackson");
        signUpDto1.setPhoneList(phoneList1);
        signUpDto1.setEmailList(emailList1);

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpDto1)))
                .andExpect(status().isCreated())
                .andReturn();

        // Create a list of Phone objects
        List<Phone> phoneList2 = new ArrayList<>();
        Phone phone2 = new Phone();
        phone2.setPhone("84955553535");
        phoneList2.add(phone2);
        // Create a list of Email objects
        List<Email> emailList2 = new ArrayList<>();
        Email email2 = new Email();
        email2.setEmail("epost@mail.net");
        emailList2.add(email2);

        SignUpDto signUpDto2 = new SignUpDto("user2", "asdfghjk",phoneList2,emailList2,2000.0f,
                new Date(2000, 3, 21, 22, 07, 44),
                "EmilyWilson");
        signUpDto2.setPhoneList(phoneList2);
        signUpDto2.setEmailList(emailList2);

        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpDto2)))
                .andExpect(status().isCreated())
                .andReturn();

        SignInDto signInDto1 = new SignInDto("user1", "qwertyui");

        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto1)))
                .andExpect(status().isOk())
                .andReturn();

        SignInDto signInDto2 = new SignInDto("user2", "asdfghjk");

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto2)))
                .andExpect(status().isOk())
                .andReturn();

        String token1 = mvcResult1.getResponse().getHeader("Authorization").replace("Bearer ", "");
        String token2 = mvcResult2.getResponse().getHeader("Authorization").replace("Bearer ", "");

        mvc.perform(get("/api/v1/user/transfer?phone={phone}&money={money}",
                        signUpDto2.getPhoneList().get(0).getPhone(), 534530)
                                .contentType(MediaType.APPLICATION_JSON).header("Authorization", token1)
                ).andExpect(status().isForbidden())
                .andReturn();


    }

    @Test
    public void signupSignin2UsersTransferToNotExistUser() throws Exception {


        // Create a list of Phone objects
        List<Phone> phoneList1 = new ArrayList<>();
        Phone phone1 = new Phone();
        phone1.setPhone("88005553535");
        phoneList1.add(phone1);
        // Create a list of Email objects
        List<Email> emailList1 = new ArrayList<>();
        Email email1 = new Email();
        email1.setEmail("mail@mail.com");
        emailList1.add(email1);

        SignUpDto signUpDto1 = new SignUpDto("user1", "qwertyui",phoneList1,emailList1,1000.0f,
                new Date(2000, 5, 23, 22, 07, 44),
                "JackJackson");
        signUpDto1.setPhoneList(phoneList1);
        signUpDto1.setEmailList(emailList1);


        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpDto1)))
                .andExpect(status().isCreated())
                .andReturn();

        // Create a list of Phone objects
        List<Phone> phoneList2 = new ArrayList<>();
        Phone phone2 = new Phone();
        phone2.setPhone("84955553535");
        phoneList2.add(phone2);
        // Create a list of Email objects
        List<Email> emailList2 = new ArrayList<>();
        Email email2 = new Email();
        email2.setEmail("epost@mail.net");
        emailList2.add(email2);

        SignUpDto signUpDto2 = new SignUpDto("user2", "asdfghjk",phoneList2,emailList2,2000.0f,
                new Date(2000, 3, 21, 22, 07, 44),
                "EmilyWilson");
        signUpDto2.setPhoneList(phoneList2);
        signUpDto2.setEmailList(emailList2);


        mvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signUpDto2)))
                .andExpect(status().isCreated())
                .andReturn();

        SignInDto signInDto1 = new SignInDto("user1", "qwertyui");

        MvcResult mvcResult1 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto1)))
                .andExpect(status().isOk())
                .andReturn();


        SignInDto signInDto2 = new SignInDto("user2", "asdfghjk");

        MvcResult mvcResult2 = mvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(signInDto2)))
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