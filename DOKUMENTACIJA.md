# Projektna dokumentacija — Auto Oglasi

Kurs: **Internet softverske arhitekture (ISA)** · Tip: Spring MVC + Thymeleaf aplikacija

---

## 1. Arhitektura

Aplikacija je realizovana kao klasična **slojevita (layered) Spring MVC** aplikacija:

```
Pregledač ──HTTP──> Controller ──> Service ──> Repository ──> Baza (MySQL)
                        │
                     Thymeleaf (server-side rendering) ──> HTML + Tailwind CSS
```

- **Controller sloj** (`controller/`) — prima HTTP zahteve, vraća nazive Thymeleaf pogleda.
- **Service sloj** (`service/` + `service/impl/`) — poslovna logika, `@Transactional` granice.
- **Repository sloj** (`repository/`) — Spring Data JPA interfejsi (automatske implementacije).
- **Entity sloj** (`entity/`) — JPA entiteti mapirani na tabele preko Hibernate-a.
- **DTO** (`dto/`) — form-backing objekti sa Bean Validation (`@Valid`) za unos.

---

## 2. Model podataka (9 tabela)

| Tabela | Opis |
|--------|------|
| `users` | registrovani korisnici |
| `roles` | role (ROLE_USER, ROLE_ADMIN) |
| `users_roles` | spojna tabela (ManyToMany User–Role) |
| `brands` | marke automobila |
| `car_models` | modeli (pripadaju marki) |
| `cities` | gradovi |
| `advertisements` | oglasi (centralni entitet) |
| `features` | oprema vozila |
| `advertisements_features` | spojna tabela (ManyToMany Advertisement–Feature) |

### Relacije

**OneToMany / ManyToOne:**
- `Brand` 1—* `CarModel` — marka ima više modela
- `CarModel` 1—* `Advertisement` — model se koristi u više oglasa
- `User` 1—* `Advertisement` — korisnik (prodavac) ima više oglasa
- `City` 1—* `Advertisement` — grad ima više oglasa

**ManyToMany:**
- `User` *—* `Role` (spojna `users_roles`)
- `Advertisement` *—* `Feature` (spojna `advertisements_features`)

Dijagram:
```
Role ─*—*─ User ─1—*─ Advertisement ─*—1─ City
                          │   │
       Brand ─1—*─ CarModel ─┘   └─*—*─ Feature
```

---

## 3. Bezbednost (Spring Security 6)

- **Autentifikacija:** form login (`/login`), lozinke heširane **BCrypt**-om,
  korisnici se učitavaju iz baze preko `CustomUserDetailsService`.
- **Autorizacija (role):**
  - `/admin/**` → samo `ROLE_ADMIN`
  - `/ads/new`, `/ads/*/edit`, `/my-ads` → samo prijavljeni korisnici
  - javno: početna, lista oglasa, detalj oglasa, registracija, login, statika
  - **provera vlasništva** na nivou servisa — korisnik može da menja/briše samo svoje
    oglase (u suprotnom `AccessDeniedException` → strana 403).
- **FE prikaz po roli:** Thymeleaf `sec:authorize` (npr. „Admin panel" se vidi samo
  administratoru, „Moji oglasi" samo prijavljenima), strana **403 / pristup odbijen**.
- **CSRF:** uključen (`CsrfTokenRequestAttributeHandler`); Thymeleaf forme automatski
  dodaju `_csrf` skriveno polje.

### Upravljanje sesijama (umesto JWT refresh-a)

U skladu sa ispitnim zahtevom, za MVC varijantu se umesto JWT tokena radi sa sesijama:

- **session timeout** — `server.servlet.session.timeout=30m`
- **session-fixation zaštita** — `migrateSession()` (nova sesija nakon prijave)
- **kontrola istovremenih sesija** — `maximumSessions(1)`
- **remember-me** — perzistentna prijava preko cookie-ja (7 dana)
- **logout** — invalidacija sesije + brisanje `JSESSIONID` cookie-ja

---

## 4. Mapiranje na bodovanje (30 bodova)

| Bodovi | Stavka | Realizacija |
|--------|--------|-------------|
| 5 | [BE] Arhitektura | Slojevita Spring MVC aplikacija (controller/service/repository/entity) |
| 5 | [BE+FE] CRUD za 1 tabelu | Pun CRUD nad `Advertisement` preko Thymeleaf formi |
| 5 | [BE] OneToMany i ManyToMany | Brand→Model, Model/User/City→Ad; User↔Role, Ad↔Feature |
| 4 | [BE] Auth/Autorizacija (role) | Spring Security, BCrypt, zaštita po rolama + vlasništvo |
| 4 | [FE] Auth/Autorizacija (role) | Login/register, `sec:authorize`, strana 403 |
| 4 | [BE+FE] Upravljanje sesijama | timeout, session-fixation, concurrent control, remember-me, logout |

---

## 5. Glavne rute

| Metoda | Ruta | Pristup | Opis |
|--------|------|---------|------|
| GET | `/`, `/ads` | javno | lista + pretraga oglasa |
| GET | `/ads/{id}` | javno | detalj oglasa |
| GET/POST | `/register` | javno | registracija |
| GET/POST | `/login`, POST `/logout` | javno | prijava/odjava |
| GET | `/my-ads` | USER | moji oglasi |
| GET | `/ads/new`, POST `/ads` | USER | kreiranje oglasa |
| GET/POST | `/ads/{id}/edit` | vlasnik/ADMIN | izmena |
| POST | `/ads/{id}/delete` | vlasnik/ADMIN | brisanje |
| GET | `/admin` | ADMIN | kontrolna tabla |
| CRUD | `/admin/{brands,models,cities,features}` | ADMIN | katalog |
| GET/POST | `/admin/users...` | ADMIN | korisnici (blokiranje/brisanje) |

---

## 6. Pokretanje i nalozi

Vidi [README.md](README.md). Demo: `admin/admin` (administrator), `pera/pera` (korisnik).
