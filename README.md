# API Gestion Équipe de Football - OGC Nice

API REST pour gérer l'équipe de football de Nice : joueurs, budget et transferts.

---

## Stack Technique

- **Java 17** + **Spring Boot 3.5.7** + **Maven**
- **Spring Data JPA** + **Hibernate** + **H2 Database** (in-memory)
- **Lombok** + **Spring Validation** + **SLF4J**
- **JUnit 5** + **Mockito** + **MockMvc**

**Pourquoi H2 ?** Simple, pas d'installation, parfait pour dev/test, facilement remplaçable par PostgreSQL en prod.

---

## Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.8+

### Installation

```bash
cd footballteam
mvn clean install
mvn spring-boot:run
```

L'application démarre sur **http://localhost:8080**

**Données de test** : 8 équipes de Ligue 1 (Nice, PSG, OM, Monaco, Lyon, Lens, Lille, Rennes) avec leurs joueurs sont chargées automatiquement au démarrage.

### Vérification

```bash
# Liste toutes les équipes (8 équipes chargées)
curl http://localhost:8080/api/equipes

# Tri par budget décroissant
curl "http://localhost:8080/api/equipes?sortBy=budget&sortDir=desc"
```

---

## Utilisation de l'API

### 1. Liste des équipes (GET)

```bash
# Liste simple
GET /api/equipes

# Avec pagination
GET /api/equipes?page=0&size=5

# Tri par budget décroissant
GET /api/equipes?sortBy=budget&sortDir=desc

# Paramètres disponibles:
# - page (défaut: 0)
# - size (défaut: 10)
# - sortBy (name, acronym, budget)
# - sortDir (asc, desc)
```

**Réponse:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "OGC Nice",
      "acronym": "OGCN",
      "budget": 50000000,
      "joueurs": [
        {
          "id": 1,
          "name": "Kasper Schmeichel",
          "position": "Gardien"
        }
      ]
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

### 2. Créer une équipe (POST)

```bash
POST /api/equipes
Content-Type: application/json
```

**Sans joueurs:**
```json
{
  "name": "OGC Nice",
  "acronym": "OGCN",
  "budget": 50000000
}
```

**Avec joueurs:**
```json
{
  "name": "OGC Nice",
  "acronym": "OGCN",
  "budget": 50000000,
  "joueurs": [
    {
      "name": "Kasper Schmeichel",
      "position": "Gardien"
    },
    {
      "name": "Jean-Clair Todibo",
      "position": "Défenseur"
    }
  ]
}
```

**Exemple curl:**
```bash
curl -X POST http://localhost:8080/api/equipes \
  -H "Content-Type: application/json" \
  -d '{
    "name": "OGC Nice",
    "acronym": "OGCN",
    "budget": 50000000,
    "joueurs": [
      {"name": "Kasper Schmeichel", "position": "Gardien"}
    ]
  }'
```

**Réponse (201 Created):**
```json
{
  "id": 1,
  "name": "OGC Nice",
  "acronym": "OGCN",
  "budget": 50000000,
  "joueurs": [...]
}
```

### 3. Transférer un joueur (POST) 🚨 Style Fabrizio Romano

```bash
POST /api/equipes/transfer
Content-Type: application/json
```

**Requête:**
```json
{
  "joueurId": 1,
  "nouvelleEquipeId": 2
}
```

**Exemple curl:**
```bash
# Transférer Kasper Schmeichel (ID: 1) de Nice au PSG (ID: 2)
curl -X POST http://localhost:8080/api/equipes/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "joueurId": 1,
    "nouvelleEquipeId": 2
  }'
```

**Réponse (200 OK):**
```json
{
  "message": "🚨 HERE WE GO! Kasper Schmeichel (Gardien) has officially joined Paris Saint-Germain from OGC Nice! Deal confirmed and sealed. ✅🔴🔵 #TransferNews #HereWeGo",
  "joueurName": "Kasper Schmeichel",
  "position": "Gardien",
  "ancienneEquipe": "OGC Nice",
  "nouvelleEquipe": "Paris Saint-Germain",
  "confirmed": true
}
```

---

## Console H2

Pour visualiser la base de données :
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:footballdb`
- Username: `sa`
- Password: (vide)

---

## Tests

```bash
# Tous les tests
mvn test

# Tests unitaires: EquipeServiceTest
# Tests d'intégration: EquipeControllerIntegrationTest
```

---

## Architecture

```
src/main/java/com/ogcnice/footballteam/
├── controller/           # API REST endpoints
├── service/             # Logique métier
├── repository/          # Accès données (EquipeRepository, JoueurRepository)
├── model/              # Entités JPA (Equipe, Joueur)
├── dto/                # Data Transfer Objects (CreateEquipeRequest, TransferJoueurRequest, TransferJoueurResponse)
├── exception/          # Gestion erreurs
└── FootballTeamApplication.java

src/main/resources/
├── application.properties  # Configuration
└── data.sql               # Données initiales (8 équipes + joueurs)
```

### Choix Architecturaux

**Architecture en couches**
- Controller → Service → Repository
- Séparation claire des responsabilités
- Facilite les tests et la maintenance

**DTOs séparés des entités**
- Validation des données entrantes
- Découplage API/BDD
- Évolution indépendante

**Gestion globale des exceptions**
- `@RestControllerAdvice` centralise les erreurs
- Codes HTTP cohérents
- Messages clairs

**Relations bidirectionnelles**
- Méthodes `addJoueur()` / `removeJoueur()`
- Cohérence automatique des relations

---

## Gestion des Erreurs

L'API retourne des codes HTTP standard:

- **200 OK** - Succès
- **201 Created** - Ressource créée
- **400 Bad Request** - Validation échouée
- **404 Not Found** - Joueur ou équipe non trouvé
- **409 Conflict** - Acronyme existe déjà
- **500 Internal Server Error** - Erreur serveur

**Exemple erreur validation:**
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "errors": {
    "name": "Le nom de l'équipe est obligatoire",
    "budget": "Le budget doit être positif"
  }
}
```

**Exemple erreur 404 (joueur non trouvé):**
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 404,
  "message": "Joueur non trouvé avec l'ID: 999"
}
```

**Exemple erreur conflit:**
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 409,
  "message": "Une équipe avec l'acronyme OGCN existe déjà"
}
```

---

## Configuration Production

Pour PostgreSQL, modifier `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/footballdb
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
```

Ajouter dans `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>
```

---

## Fonctionnalités Implémentées

- ✅ GET /api/equipes - Liste paginée avec tri
- ✅ POST /api/equipes - Création avec/sans joueurs
- ✅ POST /api/equipes/transfer - Transfert de joueurs style Fabrizio Romano 🚨
- ✅ Validation complète des données
- ✅ Gestion erreurs (codes HTTP appropriés)
- ✅ Logs détaillés (SQL, métier)
- ✅ Tests unitaires et intégration
- ✅ Console H2 pour debug
- ✅ Documentation JavaDoc
- ✅ Unicité des acronymes
- ✅ Données de test (8 équipes Ligue 1)

---
