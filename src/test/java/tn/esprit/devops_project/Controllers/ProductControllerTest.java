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
import tn.esprit.devops_project.entities.Product;
import tn.esprit.devops_project.entities.ProductCategory;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    public void retreiveAllProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    public void retrieveProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup({"/data-set/product-data.xml","/data-set/stock-data.xml"})
    public void addProduct() throws Exception {
        Product product = new Product(7L,"Product 7",100,70,ProductCategory.BOOKS,null);

        ObjectMapper objectMapper = new ObjectMapper();
        String operatorJson = objectMapper.writeValueAsString(product);

        mockMvc.perform(MockMvcRequestBuilders.post("/product/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(operatorJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetup("/data-set/product-data.xml")
    public void deleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetup({"/data-set/product-data.xml","/data-set/stock-data.xml"})
    public void retreiveProductStock() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/stock/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }


}
