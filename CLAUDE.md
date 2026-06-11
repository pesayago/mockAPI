# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

A minimal Spring Boot REST service (Java 21, Spring Boot 3.5.0) that acts as a **mock API** serving product data as JSON. It exists to back a separate front-end marketplace app (CORS is opened to `localhost:3000` and a Vercel deployment).

## Commands

Uses the Maven wrapper (`mvnw` / `mvnw.cmd`). On Windows PowerShell use `.\mvnw.cmd`.

- Build (jar): `.\mvnw.cmd clean package`
- Run locally: `.\mvnw.cmd spring-boot:run` (serves on port 8080)
- Run all tests: `.\mvnw.cmd test`
- Run a single test class: `.\mvnw.cmd test -Dtest=AppRestControllerTest`
- Run a single test method: `.\mvnw.cmd test -Dtest=AppRestControllerTest#getAllBares`
- Run the built jar: `java -jar target/pruebaApp-0.0.1-SNAPSHOT.jar`

CI (`.github/workflows/ci.yml`) runs `mvn clean test` on pushes to `develop` and PRs targeting `main`/`develop`.

## Architecture

The entire app is four classes under `src/main/java/com/psayago/pruebaApp/`:

- `Application.java` — standard `@SpringBootApplication` entry point.
- `controller/AppRestController.java` — the only controller. Two GET endpoints:
  - `/getBar` — returns a hardcoded `Greeting` serialized to a `JsonNode`.
  - `/getJson` — reads and returns product data. `@CrossOrigin` is applied here specifically.
- `model/Greeting.java` — immutable `id`/`content` value object.
- `JacksonConfiguration.java` — global Jackson customizer forcing `LocalDate` → `dd/MM/yyyy` and `LocalDateTime` → `HH:mm dd/MM/yyyy` for all (de)serialization.

### The object.json gotcha (important)

`/getJson` reads `new File("object.json")` — a **relative path resolved against the process working directory**, not the classpath. There are two copies of this file and they must be kept in sync:

- `object.json` (repo root) — what the endpoint actually reads when run from the project root or in Docker.
- `src/main/resources/static/object.json` — the classpath/static copy; the controller's `leerArchivo()` helper reads it from the classpath as `/object.json` purely to log its contents.

The `Dockerfile` copies the root `object.json` next to the jar (`/app/object.json`) precisely so the working-directory lookup succeeds at runtime. If you change the product data, update **both** copies.

### Notes / cruft to be aware of

- The controller is heavily annotated with `System.out.println` debugging and commented-out experiments (env-var reads, a `verComando()` that shells out to `/bin/bash`). `verComando()` is dead code and Linux-only — do not wire it into request handling.
- `application.properties` defines a custom `java.net.URL=object.json` key that is currently unused by the code (the filename is hardcoded).
- Maven `dev`/`prod` profiles exist but only set an unused `env` property.
- Surefire is configured with the Mockito javaagent (`-javaagent .../mockito-core ... -Xshare:off`); keep that `<argLine>` if you touch the build, or Mockito-based tests may warn/fail under Java 21+.

## Deployment

`Dockerfile` is a two-stage build (Maven + Temurin 21 → JRE 21) that packages the jar with `-DskipTests` and exposes port 8080.
