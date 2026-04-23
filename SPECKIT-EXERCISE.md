# Spec Kit Workshop — Add a Supplier Module

> Brownfield exercise on top of this repo.
> You already have a working **Customer**, **Article** and **Order** ERP starter.
> Your job is to add a **Supplier** module using the existing patterns — but
> you will not touch a single line of Java until **Spec Kit** has turned a
> paragraph of English into a reviewed spec, a plan and a concrete task list.

---

## 0. Sanity check (2 min)

```bash
mvn test         # should be green (8+ tests)
mvn package
java -jar target/erp-starter.jar
# browse the menu: Customers / Articles / Orders, then Quit
```

## 1. The existing codebase in 30 seconds

Every module in `com.example.erp.*` already follows the same shape. Read one —
say **customer** — and you'll recognise the pattern in all of them:

| Layer | File | What it stands in for in ekon |
|---|---|---|
| Model | `customer/Customer.java`, `CustomerContact.java` | Business Object header + child segment |
| Data access | `customer/CustomerRepository.java` + `InMemoryCustomerRepository.java` | Base query + DA |
| Rules | `customer/rules/VatValidator.java` | Pure, JUnit-tested validation helper |
| Service | `customer/CustomerService.java` with `beforeSave` / `beforeDeactivate` | `FMDefaultEvents` customisation class |
| Report | `customer/CustomerListReport.java` | Report (RE) |
| Maintenance UI | `customer/CustomerCli.java` | Form (FM) / maintenance screen |
| Wiring | `ErpApp.java`, `DataSeeder.java` | Main boot + menu, seed data |

> **Take 5 minutes to open `CustomerService.java` and `CustomerServiceTest.java` before starting.** The Supplier module will copy this shape almost verbatim — the Spec Kit exercise is about producing that copy *deliberately* from a spec rather than cargo-culting it.

## 2. The feature brief — paste into `/speckit.specify`

> Our purchasing team needs a **Supplier maintenance** inside the ERP. A
> supplier has a code, legal name, country (ISO-3166 alpha-2), VAT number,
> default payment term in days and an *Active* flag. Each supplier may have
> zero or more contacts (name, role, email, phone). The system must refuse
> to save a supplier with a VAT number that is invalid for the chosen
> country, and refuse to deactivate a supplier that still has *open orders*
> (any order not in status `CLOSED`). Purchasing managers need a printable
> "Active suppliers" list sorted by country, then by legal name. The new
> module must plug into the existing CLI as option 4, next to Customers,
> Articles and Orders. As a stretch goal, we would like a small local HTTP
> endpoint returning the active suppliers as JSON so the portal team can
> prototype against it.

Deliberately a little vague — `/speckit.clarify` has room to do real work.

## 3. Clarifications `/speckit.clarify` should raise

Aim to answer each with the smallest workable choice so the workshop fits the
time-box:

- **VAT validation** — reuse the existing `VatValidator` (format-only), or a new stricter one? → reuse.
- **Open-order check** — against which repository? → the existing `OrderRepository#findByCustomerCode` but we will need a **`findBySupplierCode`** equivalent; since the starter has no `supplier_code` on `Order`, the clarify step must surface this. The simplest answer for the workshop is: **add a new `SupplierOrderRepository` with its own stub data**, or reuse `OrderRepository` with a mocked rule — either is fine as long as it's explicit in the plan.
- **Active flag** — mirror Customer's boolean, or introduce a status enum? → mirror Customer (boolean).
- **Report output** — stdout CSV (like Customer), file, or both? → stdout CSV to match Customer.
- **HTTP stretch goal** — auth? framework? → no auth (localhost demo), JDK's `com.sun.net.httpserver`.
- **Persistence** — stay in-memory like the rest of the starter? → yes.

## 4. What "good" `/speckit.plan` output looks like

The plan should name real files in this repo. Expect something like:

1. **New package** `com.example.erp.supplier` following the Customer layout:
   - `Supplier`, `SupplierContact`
   - `SupplierRepository` + `InMemorySupplierRepository`
   - `SupplierService` with `beforeSave(Supplier)` and `beforeDeactivate(Supplier)`
   - `SupplierListReport`
   - `SupplierCli`
2. **Reuse** `customer.rules.VatValidator` (move it to a `common.rules` package if both modules need it — note the refactor explicitly).
3. **Open-orders rule** — pick one:
   - a) add a `SupplierOrderRepository` interface + in-memory impl (seeded by `DataSeeder`) and consult it in `beforeDeactivate`, **or**
   - b) introduce an `OpenOrdersGuard` interface in `common`, inject it into `SupplierService`, seed a stub.
4. **Wire into** `ErpApp` as menu option 4 and extend `DataSeeder` with 2–3 sample suppliers.
5. **Tests** mirroring `CustomerServiceTest`: happy save, invalid VAT, blank name, negative term, deactivate-allowed, deactivate-refused.
6. **Stretch**: `SupplierHttpServer` using `com.sun.net.httpserver.HttpServer`, `GET /suppliers` returns active suppliers as JSON.

## 5. What "good" `/speckit.tasks` output looks like

Tasks should be small enough to each map to one commit. Example target shape:

1. Create `com.example.erp.supplier` package with `Supplier` + `SupplierContact` (copy shape from `Customer`).
2. Add `SupplierRepository` + `InMemorySupplierRepository`.
3. Move `VatValidator` to a shared `common.rules` package (or keep where it is and import it — document the choice).
4. Add `SupplierService` with `beforeSave` — JUnit tests first, watch them go red.
5. Make the beforeSave tests green.
6. Add the open-orders rule wiring chosen in §4.3; `beforeDeactivate` tests red → green.
7. Add `SupplierListReport` + a small test.
8. Add `SupplierCli`; wire menu option 4 in `ErpApp`.
9. Extend `DataSeeder` with sample suppliers.
10. `mvn verify` — everything green, manual smoke test from the CLI.
11. *(Stretch)* Add `SupplierHttpServer`, hit it with `curl http://localhost:8080/suppliers`.

## 6. Definition of done

- `mvn test` stays green and adds at least the six Supplier tests.
- From the CLI, option 4 opens a Supplier maintenance that can create, list, deactivate and report.
- Saving a supplier with an invalid VAT is refused with a clear message.
- Deactivating a supplier with at least one non-`CLOSED` order is refused.
- The "active suppliers" CSV report prints sorted by country, then legal name.
- *(Stretch)* `curl http://localhost:8080/suppliers` returns JSON listing the active suppliers.
- The jar still runs with `java -jar target/erp-starter.jar`.

## 7. 90-minute workshop schedule

| Time | Step |
|---|---|
| 0:00 – 0:05 | Clone, open in IDE, `mvn verify` |
| 0:05 – 0:10 | Walk through `CustomerService` + `CustomerServiceTest` as the pattern to copy |
| 0:10 – 0:20 | Paste §2 brief into `/speckit.specify` |
| 0:20 – 0:35 | Run `/speckit.clarify`, answer using §3 |
| 0:35 – 0:50 | Run `/speckit.plan`, sanity-check against §4 |
| 0:50 – 1:00 | Run `/speckit.tasks`, compare with §5 |
| 1:00 – 1:25 | `/speckit.implement` tasks **1–5** together (model + VAT rule TDD) |
| 1:25 – 1:30 | Debrief: map every produced file to its ekon equivalent (Supplier ≡ BO, `beforeSave` ≡ `FMDefaultEvents.beforeSave`, CLI ≡ FM, CSV ≡ RE, HTTP ≡ BO-as-REST, `mvn package` ≡ Setup & Transport) |

Tasks 6–11 become a take-home.

## 8. Closing slide — why this maps to ekon

```
Plain Java (what you build)           ekon Platform (your real stack)
───────────────────────────           ─────────────────────────────
Supplier + SupplierContact POJOs      BO ksk_supplier + "contact" segment
SupplierRepository (in-memory)        Base query + DA
SupplierService.beforeSave            FMSupplier extends FMDefaultEvents
  + VatValidator                        .beforeSave() raising OTException
SupplierService.beforeDeactivate      FMSupplier.beforeDelete consulting DA
SupplierListReport (CSV)              Report lst_supplier_active (RE)
SupplierCli                           Form ksk_supplier (FM)
(optional) SupplierHttpServer         Tick "REST" on BO (kdev0051/0053)
mvn package                           Setup & Transport package
```

The same spec → clarify → plan → tasks loop lands exactly the same way on
either stack. That's the point of the exercise.
