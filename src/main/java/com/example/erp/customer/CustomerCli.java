package com.example.erp.customer;

import com.example.erp.common.Cli;
import com.example.erp.common.RuleException;

import java.io.PrintWriter;

/** Customer "maintenance form" — CLI stand-in. */
public class CustomerCli {

    private final Cli cli;
    private final CustomerService service;

    public CustomerCli(Cli cli, CustomerService service) {
        this.cli = cli;
        this.service = service;
    }

    public void run() {
        while (true) {
            cli.println();
            cli.println("-- Customers --");
            cli.println(" 1) List");
            cli.println(" 2) Create");
            cli.println(" 3) Deactivate");
            cli.println(" 4) Report (active, CSV to stdout)");
            cli.println(" b) Back");
            switch (cli.prompt("> ")) {
                case "1" -> list();
                case "2" -> create();
                case "3" -> deactivate();
                case "4" -> report();
                case "b", "B" -> { return; }
                default -> cli.println("Unknown option.");
            }
        }
    }

    private void list() {
        // In a real screen this would be a grid. Here: print the findAll().
        cli.println("code     legal_name                 country  vat              term active");
        cli.println("-------- -------------------------- -------- ---------------- ---- ------");
        for (var c : service.findAll()) {
            cli.printf("%-8s %-26s %-8s %-16s %4d %s%n",
                    c.getCode(), truncate(c.getLegalName(), 26), c.getCountry(),
                    c.getVat(), c.getPaymentTermDays(), c.isActive() ? "yes" : "no");
        }
    }

    private void create() {
        try {
            var code = cli.prompt("code: ");
            var name = cli.prompt("legal name: ");
            var country = cli.prompt("country (ISO-2): ");
            var vat = cli.prompt("vat: ");
            var term = Integer.parseInt(cli.prompt("payment term (days): "));
            service.save(new Customer(code, name, country, vat, term));
            cli.println("Saved.");
        } catch (NumberFormatException e) {
            cli.println("Payment term must be a number.");
        } catch (RuleException e) {
            cli.println("Refused: " + e.getMessage());
        }
    }

    private void deactivate() {
        try {
            service.deactivate(cli.prompt("customer code to deactivate: "));
            cli.println("Deactivated.");
        } catch (RuleException e) {
            cli.println("Refused: " + e.getMessage());
        }
    }

    private void report() {
        var pw = new PrintWriter(System.out);
        service.writeActiveReport(pw);
    }

    private static String truncate(String s, int n) {
        return s == null ? "" : (s.length() <= n ? s : s.substring(0, n));
    }
}
