package com.example.erp;

import com.example.erp.article.Article;
import com.example.erp.article.ArticleRepository;
import com.example.erp.customer.Customer;
import com.example.erp.customer.CustomerContact;
import com.example.erp.customer.CustomerRepository;
import com.example.erp.order.Order;
import com.example.erp.order.OrderRepository;
import com.example.erp.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Seeds the in-memory repositories with a handful of sample rows. */
final class DataSeeder {

    static void seed(CustomerRepository customers,
                     ArticleRepository articles,
                     OrderRepository orders) {

        var c1 = new Customer("C-1001", "Acme Iberia SL", "ES", "ESB12345674", 30);
        c1.getContacts().add(new CustomerContact("Lucía García", "Buyer",
                "lucia@acme.es", "+34 600 000 001"));
        customers.save(c1);

        var c2 = new Customer("C-1002", "Boulangerie Martin", "FR", "FR40303265045", 45);
        customers.save(c2);

        var c3 = new Customer("C-1003", "Olá Comércio Lda.", "PT", "PT501964843", 30);
        c3.deactivate();
        customers.save(c3);

        articles.save(new Article("A-100", "USB-C cable 1 m",     new BigDecimal("4.90")));
        articles.save(new Article("A-101", "USB-C cable 2 m",     new BigDecimal("6.90")));
        articles.save(new Article("A-200", "Laptop stand alu",    new BigDecimal("39.00")));

        orders.save(new Order("O-5001", "C-1001", LocalDate.now().minusDays(3), OrderStatus.OPEN));
        orders.save(new Order("O-5002", "C-1002", LocalDate.now().minusDays(10), OrderStatus.CLOSED));
        orders.save(new Order("O-5003", "C-1001", LocalDate.now().minusDays(1), OrderStatus.PENDING_APPROVAL));
    }

    private DataSeeder() {}
}
