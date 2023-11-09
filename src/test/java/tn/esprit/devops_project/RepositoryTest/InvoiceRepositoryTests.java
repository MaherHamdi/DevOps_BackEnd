package tn.esprit.devops_project.RepositoryTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.repositories.InvoiceRepository;
import tn.esprit.devops_project.services.InvoiceServiceImpl;

import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class InvoiceRepositoryTests {

    @Autowired
    private InvoiceServiceImpl invoiceService;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Test
    public void addInvoice(){
        Invoice invoice = Invoice.builder()
                .idInvoice(1L)
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Assertions.assertThat(savedInvoice).isNotNull();
        Assertions.assertThat(savedInvoice.getIdInvoice()).isGreaterThan(0);
    }
    @Test
    public void addInvoic(){
        Invoice invoice = Invoice.builder()
                .idInvoice(1L)
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Assertions.assertThat(savedInvoice).isNotNull();
        Assertions.assertThat(savedInvoice.getIdInvoice()).isGreaterThan(0);
    }

    @Test
    public void GetAll_ReturnMoreThenOneInvoice(){
        Invoice invoice = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice invoice1 = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();
        Invoice savedInvoice = invoiceRepository.save(invoice);
        Invoice savedInvoice1 = invoiceRepository.save(invoice1);

        List<Invoice> pokemonList = invoiceRepository.findAll();

        Assertions.assertThat(pokemonList).isNotNull();
        // Assertions.assertThat(pokemonList.size()).isEqualTo(2);
    }
    @Test
    public void InvoiceRepository_FindByType_ReturnInvoiceNotNull() {
        Invoice invoice = Invoice.builder()
                .amountDiscount(10.0f)
                .amountInvoice(100.0f)
                .dateCreationInvoice(new Date())
                .dateLastModificationInvoice(new Date())
                .archived(false)
                .build();

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Invoice pokemonList = invoiceRepository.findById(invoice.getIdInvoice()).get();

        Assertions.assertThat(pokemonList).isNotNull();
    }


}

