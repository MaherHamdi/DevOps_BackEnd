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
import tn.esprit.devops_project.entities.Supplier;
import tn.esprit.devops_project.entities.SupplierCategory;
import tn.esprit.devops_project.services.SupplierServiceImpl;

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
class SupplierServiceImplTest {

    @Autowired
    private SupplierServiceImpl supplierService;

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void addSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setSupplierCategory(SupplierCategory.CONVENTIONNE);
        supplier.setCode("11111");
        supplier.setLabel("label 2");
        supplier.setInvoices(null);
        this.supplierService.addSupplier(supplier);
        assertEquals(this.supplierService.retrieveAllSuppliers().size(),2);
        assertEquals(this.supplierService.retrieveSupplier(supplier.getIdSupplier()).getCode(),"11111");
    }


    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveAllSuppliers() {
        assertEquals(this.supplierService.retrieveAllSuppliers().size(),1);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplier() {
        final Supplier supplier = this.supplierService.retrieveSupplier(1L);
        assertEquals("00000", supplier.getCode());
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void retrieveSupplierNotFound() {
        assertThrows(IllegalArgumentException.class, () -> {
            this.supplierService.retrieveSupplier(40L);
        });
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    public void deleteSupplier() {
        final Supplier supplier = new Supplier();
        supplier.setSupplierCategory(SupplierCategory.ORDINAIRE);
        supplier.setCode("9999");
        supplier.setLabel("label 3");
        this.supplierService.addSupplier(supplier);
        supplierService.deleteSupplier(supplier.getIdSupplier());
        assertEquals(supplierService.retrieveAllSuppliers().size(),1);
    }

    @Test
    @DatabaseSetup("/data-set/supplier-data.xml")
    void updateSupplier() {
        final Supplier supplier = supplierService.retrieveSupplier(1L);
        supplier.setLabel("label 0");
        this.supplierService.updateSupplier(supplier);
        assertEquals(this.supplierService.retrieveSupplier(supplier.getIdSupplier()).getLabel(),"label 0");
    }



}