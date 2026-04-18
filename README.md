# 🏠 ImmoApp — Brique Immobilière

## Avant-Vente & Après-Vente — CRM Immobilier Intelligent

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-brightgreen)
![Python](https://img.shields.io/badge/Python-3.14-blue)
![FastAPI](https://img.shields.io/badge/FastAPI-0.136.0-teal)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Next.js](https://img.shields.io/badge/Next.js-14-black)

============================

## 📋 Description

ImmoApp est une application web intégrée destinée aux agences immobilières.
Elle centralise la gestion des prospects, des biens immobiliers et du service
après-vente au sein d'un système unique, cohérent et intelligent.

============================

## 🏗️ Architecture du projet

ImmoApp-Repo/
├── ImmoApp/          → Backend Spring Boot (API REST)

└── immoapp_ia/       → Microservice IA Python FastAPI

============================

## 🚀 Technologies utilisées

### Backend
| Technologie | Version | Rôle |
|-------------|---------|------|
| Spring Boot | 3.2.4 | Framework backend |
| PostgreSQL | 17 | Base de données |
| JWT | 0.11.5 | Authentification |
| MapStruct | 1.5.5 | Mapping entités/DTOs |
| Lombok | 1.18.32 | Réduction boilerplate |
| Swagger UI | 2.5.0 | Documentation API |

### Microservice IA
| Technologie | Version | Rôle |
|-------------|---------|------|
| Python | 3.14 | Langage |
| FastAPI | 0.136.0 | Framework API |
| Uvicorn | 0.44.0 | Serveur ASGI |
| Pydantic | 2.13.2 | Validation données |

### Frontend
| Technologie | Rôle |
|-------------|------|
| Next.js 14 | Framework frontend |
| Tailwind CSS | Stylisation |

============================

## 👥 Rôles utilisateurs

| Rôle | Permissions |
|------|-------------|
| **ADMIN** | Gestion complète du système |
| **AGENT_COMMERCIAL** | Prospects, biens, dashboard |
| **AGENT_SAV** | Tickets, messages, résolution |
| **CLIENT** | Biens, chatbot, tickets |

============================

## 📦 Modules

### ✅ Backend Spring Boot
- **Auth** → Register, Login, JWT
- **Biens Immobiliers** → CRUD complet + Photos
- **Prospects** → CRUD + Interactions + Score IA
- **Tickets SAV** → CRUD + Messages + Statuts
- **Chatbot** → Recommandation de biens
- **Dashboard** → Statistiques et KPIs

### ✅ Microservice IA
- **Score prospect** → Calcul automatique (0-100)
- **Catégorie IA** → CHAUD / TIÈDE / FROID
- **Fallback local** → Si microservice indisponible

============================

## ⚙️ Installation et lancement

### Prérequis
Java 17+
Maven 3.8+
PostgreSQL 17
Python 3.14
Node.js 18+
### 1️⃣ Base de données PostgreSQL
```sql
CREATE DATABASE "immoApp";
```

### 2️⃣ Backend Spring Boot
```bash
cd ImmoApp
mvn clean install
mvn spring-boot:run
```
API disponible sur : `http://localhost:8080`

Swagger UI : `http://localhost:8080/swagger-ui/index.html`

### 3️⃣ Microservice IA
```bash
cd immoapp_ia
.venv\Scripts\activate      # Windows
source .venv/bin/activate   # Linux/Mac
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```
API IA disponible sur : `http://localhost:8000`

Swagger IA : `http://localhost:8000/docs`

## 🔐 Variables d'environnement

Dans `ImmoApp/src/main/resources/application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/immoApp
spring.datasource.username=postgres
spring.datasource.password=ton_mot_de_passe
jwt.secret=immoapp_secret_key_must_be_at_least_32_chars_long
jwt.expiration=86400000
```

============================

## 📡 Endpoints principaux

### Auth
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription |
| POST | `/api/auth/login` | Connexion |

### Biens
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/biens` | Lister tous |
| POST | `/api/biens` | Créer |
| GET | `/api/biens/disponibles` | Biens disponibles |
| GET | `/api/biens/recherche` | Recherche filtrée |
| PUT | `/api/biens/{id}` | Modifier |
| DELETE | `/api/biens/{id}` | Supprimer |

### Prospects
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/prospects` | Lister tous |
| POST | `/api/prospects` | Créer |
| PATCH | `/api/prospects/{id}/statut` | Changer statut |
| POST | `/api/prospects/interactions` | Ajouter interaction |
| GET | `/api/prospects/{id}/interactions` | Historique |

### Tickets SAV
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/tickets` | Lister tous |
| POST | `/api/tickets` | Créer ticket |
| PATCH | `/api/tickets/{id}/statut` | Changer statut |
| POST | `/api/tickets/messages` | Ajouter message |
| GET | `/api/tickets/{id}/messages` | Voir messages |

### Chatbot
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/chatbot/demarrer/{clientId}` | Démarrer conversation |
| POST | `/api/chatbot/recommander` | Recommandations |
| POST | `/api/chatbot/reaction` | LIKE/DISLIKE |

### Dashboard
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/dashboard` | Statistiques complètes |

### Microservice IA
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/score` | Calculer score prospect |
| GET | `/health` | Vérifier état du service |

============================

##  Modèle de données
Role ──< User ──< Prospect ──< Interaction
│
└──< ProspectBien >──< BienImmobilier ──< Photo
User ──< TicketSAV ──< Message

============================

##  Score IA — Fonctionnement
Score 0-39  → FROID   (peu intéressé)
Score 40-69 → TIÈDE   (intérêt modéré)
Score 70-100→ CHAUD   (prêt à acheter)

Critères de calcul :
- Budget disponible
- Nombre d'interactions (LIKE, DISLIKE, QUESTION, VISITE)
- Statut du prospect
- Localisation recherchée

============================

##  Équipe :

| Nom | Rôle |
|-----|------|
| Anas Boudil | Développeur Full Stack |
| Nassim Lazaar | Développeur Full Stack |
| Imane Hachefi | Développeur Full Stack |

**Encadrant :** Pr. Driss Essabar

**Année universitaire :** 2025 – 2026
