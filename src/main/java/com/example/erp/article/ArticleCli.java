package com.example.erp.article;

import com.example.erp.common.Cli;
import com.example.erp.common.RuleException;

import java.math.BigDecimal;

public class ArticleCli {

    private final Cli cli;
    private final ArticleService service;

    public ArticleCli(Cli cli, ArticleService service) {
        this.cli = cli;
        this.service = service;
    }

    public void run() {
        while (true) {
            cli.println();
            cli.println("-- Articles --");
            cli.println(" 1) List");
            cli.println(" 2) Create");
            cli.println(" b) Back");
            switch (cli.prompt("> ")) {
                case "1" -> list();
                case "2" -> create();
                case "b", "B" -> { return; }
                default -> cli.println("Unknown option.");
            }
        }
    }

    private void list() {
        cli.println("code   description                unit_price");
        cli.println("------ -------------------------- ----------");
        for (var a : service.findAll()) {
            cli.printf("%-6s %-26s %10s%n",
                    a.getCode(),
                    a.getDescription() == null ? "" : a.getDescription(),
                    a.getUnitPrice());
        }
    }

    private void create() {
        try {
            var code = cli.prompt("code: ");
            var desc = cli.prompt("description: ");
            var price = new BigDecimal(cli.prompt("unit price: "));
            service.save(new Article(code, desc, price));
            cli.println("Saved.");
        } catch (NumberFormatException e) {
            cli.println("Unit price must be a number.");
        } catch (RuleException e) {
            cli.println("Refused: " + e.getMessage());
        }
    }
}
