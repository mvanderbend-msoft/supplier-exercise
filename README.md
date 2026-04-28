# ERP Starter — Spec Kit Workshop Starter Repo

A tiny plain-Java / Maven / JUnit project used as the brownfield starter for a
GitHub **Spec Kit** workshop.

This is a **brownfield** starter: the repo already ships with three modules
(**Customer**, **Article**, **Order**) wired into a small CLI, following a
consistent pattern. The workshop exercise is to **add a Supplier module** using
the same pattern, but driven through the Spec Kit workflow rather than jumping
straight into code.

See [`LAB.html`](./LAB.html) for the workshop script (open it in a browser).

---

## Prerequisites

- JDK 17+
- Maven 3.9+ (or use your IDE's embedded one)
- Any IDE (Eclipse, IntelliJ, VS Code — it's a standard Maven project)
- GitHub Copilot CLI with Spec Kit

## Build & run

```bash
mvn test                 # runs the existing JUnit tests (should be green)
mvn package              # builds target/erp-starter.jar
java -jar target/erp-starter.jar
```

You should land in this CLI:

```
=== ERP Starter ===
 1) Customers
 2) Articles
 3) Orders
 q) Quit
>
```

## What is already implemented

```
com.example.erp
├── ErpApp              main() + top-level CLI menu
├── DataSeeder          seeds the in-memory repos on startup
├── common
│   ├── Cli             tiny stdin helper
│   └── RuleException   thrown by service-level rule failures
├── customer
│   ├── Customer, CustomerContact
│   ├── CustomerRepository  (+ InMemoryCustomerRepository)
│   ├── CustomerService     ← note beforeSave / beforeDeactivate hooks
│   ├── CustomerListReport
│   ├── CustomerCli
│   └── rules/VatValidator
├── article
│   ├── Article, ArticleRepository, InMemoryArticleRepository
│   ├── ArticleService
│   └── ArticleCli
└── order
    ├── Order, OrderStatus
    ├── OrderRepository (+ InMemoryOrderRepository)
    └── OrderCli
```

All three existing modules share the same shape:

- a **model** (POJO + optional child records)
- a **repository** interface with an in-memory implementation
- a **service** with explicit `beforeSave` / `beforeDeactivate` hooks for business rules
- optional **rules** classes (pure, JUnit-tested) — validation logic
- a **CLI** for the user

### Note on the `Order` module

`Order` is deliberately **read-only** in this starter. Its repository is what
the future `SupplierService.beforeDeactivate` rule must consult to check whether
a supplier has open orders. It's included so that rule has something real to
talk to.

## Repo shape (excerpt)

```
.
├── pom.xml
├── README.md
├── LAB.html
├── src/main/java/com/example/erp/…
└── src/test/java/com/example/erp/…
```
