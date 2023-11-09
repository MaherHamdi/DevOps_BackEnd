package tn.esprit.devops_project.ServiceTest;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.services.OperatorServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void addOperator() {
        final Operator op2 = new Operator(7L,"fname 7","lname 7","0000",null);
        final Operator op = new Operator();
        op.setFname("Fname 2");
        op.setInvoices(null);
        this.operatorService.addOperator(op);
        this.operatorService.addOperator(op2);
        assertEquals(this.operatorService.retrieveAllOperators().size(),3);
        assertEquals(this.operatorService.retrieveOperator(op.getIdOperateur()).getFname(),"Fname 2");
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperator() {
        final Operator op = this.operatorService.retrieveOperator(1L);
        assertEquals("fname 1", op.getFname());
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveOperatorNotFound() {
        assertThrows(NullPointerException.class, () -> {
            this.operatorService.retrieveOperator(7L);
        });
    }



    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveAllOperator() {
        final List<Operator> allOperator = this.operatorService.retrieveAllOperators();
        assertEquals(allOperator.size(), 1);

    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void deleteOperator() {
        this.operatorService.deleteOperator(1L);
        assertEquals(this.operatorService.retrieveAllOperators().size(), 0);
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void updateOperator() {
        Operator op =this.operatorService.retrieveOperator(1L);
        op.setLname("Lname 1 updated");
        this.operatorService.updateOperator(op);
        assertEquals(this.operatorService.retrieveOperator(1L).getLname(), "Lname 1 updated");
    }
}