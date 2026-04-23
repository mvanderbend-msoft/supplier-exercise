package com.example.erp;

import com.example.erp.article.ArticleCli;
import com.example.erp.article.ArticleService;
import com.example.erp.article.InMemoryArticleRepository;
import com.example.erp.common.Cli;
import com.example.erp.customer.CustomerCli;
import com.example.erp.customer.CustomerService;
import com.example.erp.customer.InMemoryCustomerRepository;
import com.example.erp.customer.rules.VatValidator;
import com.example.erp.order.InMemoryOrderRepository;
import com.example.erp.order.OrderCli;

/**
 * Entry point for the ERP starter CLI.
 *
 * <p>Wires the in-memory repositories, services and per-module CLIs, seeds
 * some sample data and then loops on the top-level menu.</p>
 */
public final class ErpApp {

    public static void main(String[] args) {
        var cli = new Cli();

        var customers = new InMemoryCustomerRepository();
        var articles  = new InMemoryArticleRepository();
        var orders    = new InMemoryOrderRepository();

        DataSeeder.seed(customers, articles, orders);

        var vat = new VatValidator();
        var customerService = new CustomerService(customers, orders, vat);
        var articleService  = new ArticleService(articles);

        var customerCli = new CustomerCli(cli, customerService);
        var articleCli  = new ArticleCli(cli, articleService);
        var orderCli    = new OrderCli(cli, orders);

        while (true) {
            cli.println();
            cli.println("=== ERP Starter ===");
            cli.println(" 1) Customers");
            cli.println(" 2) Articles");
            cli.println(" 3) Orders");
            cli.println(" q) Quit");
            var choice = cli.prompt("> ");
            switch (choice) {
                case "1" -> customerCli.run();
                case "2" -> articleCli.run();
                case "3" -> orderCli.run();
                case "q", "Q" -> { cli.println("Bye!"); return; }
                default -> cli.println("Unknown option.");
            }
        }
    }

    private ErpApp() {}
}
