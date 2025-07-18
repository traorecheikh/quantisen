# QuantiSen Boisson

[![CI/CD Pipeline](https://github.com/traorecheikh/quantisen-boisson/actions/workflows/ci.yml/badge.svg)](https://github.com/traorecheikh/quantisen-boisson/actions/workflows/ci.yml)

**QuantiSen Boisson** est une application web robuste et complète, conçue pour la gestion optimisée des stocks de boissons. Elle offre une traçabilité précise des produits, une gestion fine des mouvements d'inventaire et des outils d'analyse avancés pour une prise de décision éclairée. Développée par [Cheikh Tidiane](https://github.com/traorecheikh), cette application est déployée sur Microsoft Azure et accessible via son nom de domaine complet (FQDN) : [quantisen-42615.azurewebsites.net](https://quantisen-42615.azurewebsites.net).

## Table des matières

*   [Vision du Projet](#vision-du-projet)
*   [Architecture](#architecture)
*   [Fonctionnalités Clés](#fonctionnalités-clés)
*   [Technologies Utilisées](#technologies-utilisées)
*   [Structure du Projet](#structure-du-projet)
*   [Pipeline CI/CD](#pipeline-cicd)
*   [Déploiement sur Azure](#déploiement-sur-azure)
*   [Tests Unitaires](#tests-unitaires)
*   [Améliorations Futures et Points d'Attention](#améliorations-futures-et-points-dattention)
*   [Démarrage Local](#démarrage-local)
*   [Auteur](#auteur)

## Vision du Projet

QuantiSen Boisson vise à fournir une solution intuitive et performante pour les entreprises gérant des stocks de boissons. L'objectif est de simplifier les opérations quotidiennes d'inventaire, de minimiser les pertes dues à l'obsolescence ou aux ruptures de stock, et de fournir des insights précieux sur la performance des produits et les tendances du marché.

## Architecture

L'application est structurée selon une architecture en couches, favorisant la modularité, la maintenabilité et la séparation des préoccupations. Elle s'inspire des principes du Domain-Driven Design (DDD) :

*   **Couche Domaine (`com.quantisen.boisson.domaine`)** :
    *   Cœur de l'application, encapsulant la logique métier essentielle et les règles de l'entreprise.
    *   Contient les entités (`Boisson`, `CompteUtilisateur`, `Lot`, `Mouvement`, `LigneOperation`), les agrégats, les services de domaine et les interfaces de port (contrats pour l'accès aux données).
    *   Indépendante de toute technologie d'infrastructure.

*   **Couche Application (`com.quantisen.boisson.application`)** :
    *   Orchestre les cas d'utilisation de l'application.
    *   Contient les services d'application (par exemple, `AnalyticsService`, `BoissonService`, `UtilisateurService`, `StockageService`) qui coordonnent les opérations en utilisant les services du domaine et les ports d'infrastructure.
    *   Gère les DTOs (Data Transfer Objects) pour la communication avec la couche d'infrastructure.

*   **Couche Infrastructure (`com.quantisen.boisson.infrastructure`)** :
    *   Implémente les ports définis dans la couche domaine.
    *   Gère les aspects techniques tels que la persistance des données (via JPA/Hibernate), l'exposition des services web REST (contrôleurs JAX-RS), la sécurité (JWT) et la configuration.
    *   Contient les implémentations concrètes des dépôts (repositories) et des adaptateurs pour les technologies externes.

### Diagramme Conceptuel de l'Architecture

```mermaid
graph TD
    SubGraph Presentation [Infrastructure: Web]
        A[AnalytiqueController] --> B(AnalyticsService)
        C[BoissonController] --> D(BoissonService)
        E[IdentiteController] --> F(UtilisateurService)
        G[StockageController] --> H(StockageService)
    End

    SubGraph Application [Application Layer]
        B --> I(AnalyticsServiceImpl)
        D --> J(BoissonServiceImpl)
        F --> K(UtilisateurServiceImpl)
        H --> L(StockageServiceImpl)
    End

    SubGraph Domain [Domain Layer]
        I --> M(EntityManager)
        J --> N(BoissonPort)
        K --> O(IdentiteRepository)
        L --> P(LigneOperationPort)
        L --> Q(LotPort)
        L --> R(MouvementPort)
    End

    SubGraph Persistence [Infrastructure: Persistence]
        N --> S(BoissonRepository)
        O --> T(IdentiteRepositoryImpl)
        P --> U(LigneOperationRepository)
        Q --> V(LotRepository)
        R --> W(MouvementRepository)
    End

    SubGraph Database [External: Database]
        S --> DB(PostgreSQL)
        T --> DB
        U --> DB
        V --> DB
        W --> DB
    End

    SubGraph Security [Infrastructure: Security]
        K --> X(JwtSession)
    End

    SubGraph Utils [Utilities]
        J --> Y(BoissonMapper)
        K --> Z(UtilisateurMapper)
    End

    I --> M
    J --> Y
    K --> Z
    K --> X

    style Presentation fill:#f9f,stroke:#333,stroke-width:2px
    style Application fill:#ccf,stroke:#333,stroke-width:2px
    style Domain fill:#cfc,stroke:#333,stroke-width:2px
    style Persistence fill:#ffc,stroke:#333,stroke-width:2px
    style Database fill:#eee,stroke:#333,stroke-width:2px
    style Security fill:#fcf,stroke:#333,stroke-width:2px
    style Utils fill:#eef,stroke:#333,stroke-width:2px
```

## Fonctionnalités Clés

L'application QuantiSen Boisson offre un ensemble complet de fonctionnalités pour une gestion efficace des stocks :

### 1. Gestion des Boissons

*   **Ajout/Modification/Suppression :** CRUD complet pour les fiches de boissons (nom, description, prix, volume, unité, seuil d'alerte).
*   **Statut :** Activation/désactivation des boissons pour gérer leur visibilité et leur disponibilité.
*   **Recherche :** Récupération des boissons par ID, par nom, ou listes complètes (actives/inactives).

### 2. Gestion des Stocks et Mouvements

*   **Entrées en Stock :**
    *   **Entrée Simple :** Enregistrement d'un nouveau lot de boisson avec sa quantité initiale, son numéro de lot, sa date de péremption et son fournisseur.
    *   **Entrée par Lots (Batch) :** Possibilité d'enregistrer plusieurs lots simultanément.
*   **Sorties de Stock :** Gestion des sorties de boissons avec une stratégie optimisée :
    *   **FEFO (First-Expired, First-Out) :** Les lots dont la date de péremption est la plus proche sont prélevés en premier.
    *   **FIFO (First-In, First-Out) :** Si les dates de péremption sont identiques, les lots entrés en premier sont prélevés.
*   **Ajustements de Stock :** Opérations pour corriger les quantités en stock (ajustements positifs ou négatifs) avec une raison documentée.
*   **Traçabilité :** Enregistrement détaillé de chaque mouvement (entrée, sortie, ajustement) et de chaque ligne d'opération associée à un lot spécifique.
*   **Consultation :** Accès aux listes de lots (valides, par boisson), de mouvements (par boisson, par lot, par utilisateur) et de lignes d'opérations (par mouvement, par lot, paginées).

### 3. Gestion des Utilisateurs et Sécurité

*   **Enregistrement :** Création de nouveaux comptes utilisateurs avec hachage sécurisé des mots de passe (Argon2).
*   **Authentification :** Connexion sécurisée via email et mot de passe, générant un JSON Web Token (JWT) pour les sessions authentifiées.
*   **Gestion des Rôles :** Attribution de rôles aux utilisateurs (non explicitement détaillé dans le code analysé, mais implicite via `Role` enum).
*   **Changement de Mot de Passe :** Fonctionnalité de mise à jour du mot de passe utilisateur.
*   **Statut Utilisateur :** Activation/désactivation des comptes utilisateurs.

### 4. Analytique et Rapports

*   **Tableau de Bord :** Vue d'ensemble des métriques clés :
    *   Nombre total de boissons.
    *   Stock total disponible.
    *   Alertes de stock bas.
    *   Nombre total de mouvements.
    *   Nombre total d'utilisateurs.
    *   Valeur totale du stock.
*   **Mouvements Hebdomadaires :** Visualisation des entrées, sorties et ajustements sur une base hebdomadaire.
*   **Tendances des Mouvements :** Analyse des tendances (à la hausse, à la baisse, stable) sur des périodes hebdomadaires, mensuelles et annuelles.
*   **Performance des Boissons :** Classement des boissons par nombre de mouvements, quantité totale et impact sur les revenus.
*   **Distribution du Stock :** Répartition de la valeur du stock par catégorie de boisson.
*   **Alertes :** Notifications pour les stocks faibles et les dates de péremption proches.
*   **Export de Données :** Génération de rapports détaillés aux formats Excel (`.xlsx`) et PDF (`.pdf`) pour les analyses, les mouvements et l'inventaire.

## Technologies Utilisées

Le projet est construit sur un stack technologique moderne et robuste, principalement basé sur l'écosystème Java et Jakarta EE.

*   **Langage :** Java 17
*   **Gestion de Projet :** Maven
*   **Frameworks & API :**
    *   **Jakarta EE :** Utilisation des spécifications clés pour les applications d'entreprise :
        *   **Jakarta Servlet 6.1.0 :** Pour la gestion des requêtes web.
        *   **Jakarta RESTful Web Services (JAX-RS) 3.1.0 (Jersey 3.1.10) :** Pour la création d'APIs RESTful.
        *   **Jakarta Contexts and Dependency Injection (CDI) (Weld SE 6.0.0.Beta4) :** Pour l'injection de dépendances et la gestion du cycle de vie des composants.
        *   **Jakarta Persistence API (JPA) 3.2.0 (Hibernate ORM 7.0.0.Alpha2) :** Pour la persistance des données relationnelles.
    *   **Jackson 2.17.0 :** Bibliothèque de sérialisation/désérialisation JSON.
    *   **Lombok 1.18.36 :** Réduit le code boilerplate (getters, setters, constructeurs, etc.).
*   **Base de Données :** PostgreSQL (via `postgresql` driver 42.7.6)
*   **Sécurité :**
    *   **Nimbus JOSE + JWT 9.41.1 :** Implémentation de JWT pour l'authentification basée sur les tokens.
    *   **Password4j 1.8.1 :** Bibliothèque pour le hachage sécurisé des mots de passe (utilise Argon2).
*   **Journalisation :** Logback 1.5.16
*   **Reporting & Export :**
    *   **Apache POI 5.4.0 :** Pour la génération de fichiers Excel (`.xlsx`).
    *   **OpenPDF 1.3.30 :** Pour la génération de fichiers PDF (`.pdf`).
*   **Tests :**
    *   **JUnit 5.11.0 :** Framework de tests unitaires.
    *   **Mockito 5.10.0 :** Framework de mocking pour les tests unitaires.
*   **Conteneurisation :** Docker
*   **Serveur d'Applications :** Apache Tomcat 11.0.9 (conteneurisé)
*   **Infrastructure as Code (IaC) :** Terraform
*   **Cloud Provider :** Microsoft Azure

## Structure du Projet

Le projet suit une structure Maven standard, avec une organisation claire des sources et des ressources :

```
/home/cheikh/IdeaProjects/quantisen-boisson/
├───.github/                  # Configurations GitHub (workflows CI/CD)
│   └───workflows/
│       └───ci.yml            # Définition du pipeline CI/CD
├───infra/                    # Fichiers Terraform pour l'infrastructure Azure
│   └───main.tf
├───src/
│   ├───main/
│   │   ├───java/             # Code source Java
│   │   │   └───com/quantisen/boisson/
│   │   │       ├───application/  # Services d'application et DTOs
│   │   │       │   ├───analytique/
│   │   │       │   ├───boisson/
│   │   │       │   ├───identite/
│   │   │       │   └───stockage/
│   │   │       ├───core/         # Classes principales de l'application
│   │   │       ├───domaine/      # Modèles de domaine et interfaces de port
│   │   │       │   ├───boisson/
│   │   │       │   ├───identite/
│   │   │       │   └───stockage/
│   │   │       └───infrastructure/ # Implémentations des ports, contrôleurs web, sécurité
│   │   │           ├───config/
│   │   │           ├───persistence/
│   │   │           ├───security/
│   │   │           └───web/
│   │   └───resources/        # Fichiers de configuration et ressources
│   │       ├───logback.xml
│   │       └───META-INF/
│   │           └───persistence.xml # Configuration JPA (Hibernate)
│   └───test/
│       └───java/             # Tests unitaires
│           └───com/quantisen/boisson/
│               └───domaine/  # Tests pour les couches domaine et application
├───Dockerfile                # Instructions pour la construction de l'image Docker
├───pom.xml                   # Fichier de configuration Maven
└───...
```

## Pipeline CI/CD

Un pipeline d'intégration et de déploiement continus (CI/CD) est mis en place via GitHub Actions, garantissant que chaque modification du code est automatiquement construite, testée et déployée. Le workflow est défini dans `.github/workflows/ci.yml` et se compose de trois étapes principales :

1.  **`build-and-test`** :
    *   **Déclencheur :** `push` et `pull_request` sur la branche `main`.
    *   **Actions :**
        *   Checkout du code source.
        *   Configuration de l'environnement Java 17 (Temurin).
        *   Compilation du projet Maven (`./mvnw clean package --no-transfer-progress`).
        *   Exécution des tests unitaires (`./mvnw test --no-transfer-progress`).
        *   Upload de l'artefact WAR (`target/boisson-1.0-SNAPSHOT.war`) pour les étapes suivantes.

2.  **`dockerize`** :
    *   **Dépendance :** Succès de l'étape `build-and-test`.
    *   **Actions :**
        *   Checkout du code source.
        *   Téléchargement de l'artefact WAR généré précédemment.
        *   Connexion sécurisée à DockerHub en utilisant des secrets GitHub.
        *   Construction de l'image Docker à partir du `Dockerfile` et push vers DockerHub (`${{ secrets.DOCKERHUB_USERNAME }}/quantisen:latest`).
        *   Sauvegarde du tag de l'image Docker dans un fichier pour l'étape de déploiement.

3.  **`deploy`** :
    *   **Dépendance :** Succès de l'étape `dockerize`.
    *   **Actions :**
        *   Checkout du code source.
        *   Téléchargement du tag de l'image Docker.
        *   Mise en place de Terraform.
        *   **Gestion de l'Infrastructure (Terraform) :**
            *   `terraform init` : Initialisation du répertoire de travail Terraform.
            *   `terraform plan` : Génération d'un plan d'exécution pour visualiser les changements d'infrastructure.
            *   `terraform apply -auto-approve` : Application des changements pour provisionner ou mettre à jour les ressources Azure. Les identifiants Azure sont passés via des variables d'environnement sécurisées.
        *   Le déploiement de l'application sur Azure est géré par Terraform, qui s'assure que l'instance Azure App Service utilise la dernière image Docker poussée sur DockerHub.

## Déploiement sur Azure

L'application est déployée sur Microsoft Azure en utilisant Azure App Service pour l'hébergement de conteneurs. L'automatisation du déploiement est assurée par Terraform, ce qui permet une gestion déclarative et reproductible de l'infrastructure.

**FQDN de l'application :** [quantisen-42615.azurewebsites.net](https://quantisen-42615.azurewebsites.net)

Le `Dockerfile` spécifie l'environnement d'exécution :

*   **Image de base :** `eclipse-temurin:17-jdk-alpine` (JDK 17 sur Alpine Linux).
*   **Serveur d'applications :** Apache Tomcat 11.0.9, téléchargé et configuré dynamiquement dans l'image.
*   **Déploiement WAR :** Le fichier `boisson-1.0-SNAPSHOT.war` est copié dans le répertoire `webapps/ROOT.war` de Tomcat, rendant l'application accessible à la racine.

## Tests Unitaires

Le projet intègre une suite complète de tests unitaires pour garantir la fiabilité et la robustesse du code. Ces tests sont essentiels pour valider le comportement des composants individuels et prévenir les régressions.

*   **Frameworks :** JUnit 5 pour l'écriture des tests et Mockito pour la création de mocks et la vérification des interactions.
*   **Couverture :** Les tests couvrent les couches domaine et application, validant la logique métier et les interactions avec les ports.
*   **Exécution :** Les tests sont automatiquement exécutés à chaque exécution du pipeline CI/CD, assurant une validation continue de la qualité du code.

Exemples de fichiers de test :

*   `src/test/java/com/quantisen/boisson/domaine/boisson/port/BoissonPortTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/boisson/services/BoissonServiceTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/identite/port/CompteIdentiteDaoTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/identite/services/UtilisateurServiceTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/statistiques/services/AnalyticsServiceTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/stockage/port/LigneOperationPortTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/stockage/port/LotPortTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/stockage/port/MouvementPortTest.java`
*   `src/test/java/com/quantisen/boisson/domaine/stockage/services/StockageServiceTest.java`

## Améliorations Futures et Points d'Attention

Bien que l'application soit fonctionnelle et robuste, plusieurs pistes d'amélioration et points d'attention ont été identifiés :

*   **Sécurité - Vulnérabilité `changerMotDePasse` :** La méthode `changerMotDePasse` dans `UtilisateurServiceImpl` compare directement l'ancien mot de passe fourni en texte brut avec le mot de passe stocké. **Ceci est une vulnérabilité de sécurité critique.** La comparaison des mots de passe doit toujours être effectuée en utilisant la bibliothèque de hachage (`Password4j`) pour comparer le hachage du mot de passe fourni avec le hachage stocké, sans jamais manipuler les mots de passe en texte brut.

*   **Gestion des Erreurs :** L'utilisation généralisée de `RuntimeException` pourrait être affinée pour des exceptions plus spécifiques et une gestion d'erreurs plus granulaire, améliorant la robustesse et la clarté du code.

*   **Fonctionnalités Analytiques :** Les méthodes `getDailyMovements`, `getExpirationAlerts`, `getUserActivity` et `getRevenueMetrics` dans `AnalyticsServiceImpl` retournent actuellement des données vides ou par défaut. Elles représentent des opportunités d'extension pour des analyses plus approfondies.

*   **Gestion des Stocks - `getBoissonById` :** La méthode `getBoissonById` dans `BoissonServiceImpl` retourne actuellement `null`. Elle devrait être implémentée pour permettre une récupération directe des boissons par leur identifiant.

*   **Internationalisation (i18n) :** L'application est actuellement en français. L'ajout de l'internationalisation permettrait de supporter plusieurs langues.

*   **Interface Utilisateur (Frontend) :** Bien que cette documentation se concentre sur le backend, une interface utilisateur moderne et réactive serait un ajout significatif pour exploiter pleinement les fonctionnalités de l'API.

*   **Monitoring et Logging :** Mise en place de solutions de monitoring plus avancées et d'une stratégie de logging plus détaillée pour faciliter le débogage et l'analyse des performances en production.

## Démarrage Local

Pour démarrer l'application localement, suivez les étapes suivantes :

1.  **Prérequis :**
    *   Java Development Kit (JDK) 17 ou supérieur.
    *   Maven 3.x.
    *   Docker (optionnel, pour exécuter l'application dans un conteneur).
    *   Une instance PostgreSQL accessible (les informations de connexion sont dans `src/main/resources/META-INF/persistence.xml`).

2.  **Cloner le dépôt :**
    ```bash
    git clone https://github.com/traorecheikh/quantisen-boisson.git
    cd quantisen-boisson
    ```

3.  **Configuration de la Base de Données :**
    *   Assurez-vous que votre base de données PostgreSQL est en cours d'exécution.
    *   Mettez à jour les propriétés de connexion dans `src/main/resources/META-INF/persistence.xml` si nécessaire.

4.  **Construction du Projet :**
    ```bash
    ./mvnw clean install
    ```
    Cela compilera le code, exécutera les tests et générera le fichier WAR dans le répertoire `target/`.

5.  **Exécution avec Docker (recommandé) :**
    ```bash
    docker build -t quantisen-boisson .
    docker run -p 8080:8080 quantisen-boisson
    ```
    L'application sera accessible à `http://localhost:8080/`.

6.  **Exécution Manuelle (sans Docker) :**
    *   Vous pouvez déployer le fichier WAR généré (`target/boisson-1.0-SNAPSHOT.war`) sur un serveur d'applications compatible Jakarta EE comme Apache Tomcat.

## Auteur

*   **Cheikh Tidiane**
*   **GitHub :** [traorecheikh](https://github.com/traorecheikh)

---