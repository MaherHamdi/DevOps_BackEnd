package tn.esprit.devops_project.Controllers;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    public void testGetInvoices() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    public void testRetrieveInvoice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/invoice/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    public void testCancelInvoice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/invoice/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }



    @Test
    @DatabaseSetup({"/data-set/invoice-data.xml","/data-set/operator-data.xml"})
    public void testAssignOperatorToInvoice() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/invoice/operator/" + 1L + "/" + 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
