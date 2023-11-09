package tn.esprit.devops_project.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OperatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    public void getOperators() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/operator"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    public void retrieveOperator() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/operator/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    public void addOperator() throws Exception {
        Operator operator = new Operator(7L,"fname2","lname3","0000",null);

        ObjectMapper objectMapper = new ObjectMapper();
        String operatorJson = objectMapper.writeValueAsString(operator);

        mockMvc.perform(MockMvcRequestBuilders.post("/operator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(operatorJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    public void modifyOperator() throws Exception {
        Operator operator = new Operator();
        operator.setIdOperateur(1L);
        operator.setFname("fname5");
        ObjectMapper objectMapper = new ObjectMapper();
        String operatorJson = objectMapper.writeValueAsString(operator);
        mockMvc.perform(MockMvcRequestBuilders.put("/operator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(operatorJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
