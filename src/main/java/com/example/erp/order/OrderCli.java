package com.example.erp.order;

import com.example.erp.common.Cli;

/** Read-only Order CLI — enough to see what's in the system. */
public class OrderCli {

    private final Cli cli;
    private final OrderRepository orders;

    public OrderCli(Cli cli, OrderRepository orders) {
        this.cli = cli;
        this.orders = orders;
    }

    public void run() {
        while (true) {
            cli.println();
            cli.println("-- Orders --");
            cli.println(" 1) List all");
            cli.println(" 2) List by customer code");
            cli.println(" b) Back");
            switch (cli.prompt("> ")) {
                case "1" -> list(orders.findAll());
                case "2" -> list(orders.findByCustomerCode(cli.prompt("customer code: ")));
                case "b", "B" -> { return; }
                default -> cli.println("Unknown option.");
            }
        }
    }

    private void list(java.util.List<Order> rows) {
        cli.println("code     customer  date        status");
        cli.println("-------- --------- ----------- -----------------");
        for (var o : rows) {
            cli.printf("%-8s %-9s %-11s %s%n",
                    o.code(), o.customerCode(), o.date(), o.status());
        }
    }
}
