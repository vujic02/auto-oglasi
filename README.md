# Auto Oglasi — Sajt za oglase automobila

Završni projekat iz kursa **Internet softverske arhitekture (ISA)**.
Web aplikacija za objavljivanje i pretragu oglasa za prodaju automobila.

**Tehnologije:** Spring Boot 3 · Spring MVC · Thymeleaf · Spring Security 6 · Spring Data JPA (Hibernate) · MySQL · TailwindCSS · Java 21

---

## Funkcionalnosti

- Pregled i **pretraga/filtriranje** oglasa (po marki, gorivu, ceni, ključnoj reči) sa paginacijom
- **Registracija** i **prijava** korisnika (Spring Security, BCrypt)
- **CRUD nad oglasima** — registrovan korisnik objavljuje, menja i briše **svoje** oglase
- **Admin panel** — CRUD nad katalogom (marke, modeli, gradovi, oprema) i upravljanje korisnicima
- **Role**: `ROLE_USER` (objava oglasa) i `ROLE_ADMIN` (administracija)
- **Upravljanje sesijama** — session timeout, remember-me, invalidacija na odjavi, kontrola istovremenih sesija

---

## Pokretanje

### Preduslovi
- JDK 21
- Maven 3.9+
- MySQL server (ili koristi H2 profil, vidi dole)
- Node.js (samo ako želiš ponovo da generišeš CSS; generisani `app.css` je već uključen)

### 1) Baza (MySQL)
Aplikacija sama kreira šemu `car_ads` (`createDatabaseIfNotExist=true`).
Podesi kredencijale u [src/main/resources/application.properties](src/main/resources/application.properties):

```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 2) (Opciono) Generisanje Tailwind CSS-a
Generisani CSS je već u repozitorijumu. Ako menjaš stilove:

```bash
npm install
npm run build      # jednokratno
npm run watch      # automatsko pregenerisanje tokom razvoja
```

### 3) Pokretanje aplikacije

```bash
mvn spring-boot:run
```

Aplikacija je dostupna na **http://localhost:8080**.

### Brzi demo bez MySQL-a (H2 in-memory)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

H2 konzola: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:car_ads`, user `sa`).

---

## Demo nalozi

Pri prvom pokretanju baza se puni početnim podacima (`DataSeeder`):

| Korisnik | Lozinka | Role |
|----------|---------|------|
| `admin`  | `admin` | ROLE_ADMIN, ROLE_USER |
| `pera`   | `pera`  | ROLE_USER |

---

## Struktura projekta

```
src/main/java/com/autooglasi/
├── entity/        # JPA entiteti (User, Role, Brand, CarModel, City, Advertisement, Feature)
├── repository/    # Spring Data JPA repozitorijumi
├── service/       # servisni interfejsi + service/impl implementacije
├── controller/    # Spring MVC kontroleri (+ controller/admin za admin deo)
├── dto/           # form-backing objekti sa Bean Validation
├── security/      # CustomUserDetailsService
├── config/        # SecurityConfig, DataSeeder
└── exception/     # ResourceNotFoundException, GlobalExceptionHandler

src/main/resources/
├── templates/     # Thymeleaf strane (ad/, auth/, admin/, error/, fragments/)
├── static/css/    # generisani Tailwind CSS (app.css)
└── application*.properties
```

Detaljan opis modela, relacija i bezbednosti: [DOKUMENTACIJA.md](DOKUMENTACIJA.md).
