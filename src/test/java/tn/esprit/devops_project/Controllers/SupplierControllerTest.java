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
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.SupplierServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierServiceImpl supplierService;

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void addSupplier() throws Exception {
        final Supplier supplier= new Supplier(7L,"code","label",SupplierCategory.CONVENTIONNE,null);

        // convert the supplier object to JSON using JSON serializer
        ObjectMapper objectMapper = new ObjectMapper();
        String supplierJson = objectMapper.writeValueAsString(supplier);

        mockMvc.perform(MockMvcRequestBuilders.post("/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(supplierJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void retrieveSupplier() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/supplier/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void getSuppliers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/supplier"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void removeFournisseur() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/supplier/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void modifyFournisseur() throws Exception {
        Supplier supplier = supplierService.retrieveSupplier(1L);
        ObjectMapper objectMapper = new ObjectMapper();
        String supplierJson = objectMapper.writeValueAsString(supplier);

        mockMvc.perform(MockMvcRequestBuilders.put("/supplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(supplierJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
