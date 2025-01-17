# Documentation du Système de Gestion de Produits

## Vue d'ensemble
Ce projet implémente un système complet de gestion de produits avec une architecture moderne frontend/backend. Il permet la création, la lecture, la mise à jour et la suppression (CRUD) de produits, avec une interface utilisateur réactive et une validation robuste des données.

## Guide de Démarrage

### Prérequis
- Node.js (v18 ou supérieur)
- Docker
- Git

### Installation et Configuration

1. **Clone du projet**
```bash
git clone https://github.com/Adzaba3/AlshadowsSoftProduct.git
cd AlshadowsSoftProduct
```

2. **Base de données (PostgreSQL + PgAdmin)**
- Lancer la base de données avec Docker :
```bash
docker run --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=mydatabase -p 5432:5432 -d postgres

docker run --name pgadmin-container -e PGADMIN_DEFAULT_EMAIL=admin@example.com -e PGADMIN_DEFAULT_PASSWORD=admin -p 5050:80 -d dpage/pgadmin4

```

3. **Backend**

Le serveur démarre sur `http://localhost:8086`

4. **Frontend**
```bash
cd product-frontend
npm install
npm run dev
```
L'application est accessible sur `http://localhost:5173`

### Première Connexion
Un utilisateur par défaut est créé au démarrage de l'application :
- Username: `user`
- Password: `user123`

⚠️ Il est nécessaire de se connecter avec ces identifiants pour accéder aux fonctionnalités de l'application.

## Architecture Technique

### Frontend (Client)
L'application frontend est construite avec React et TypeScript, offrant une expérience utilisateur moderne et type-safe. 

#### Technologies Principales
- **React & TypeScript**: Framework UI principal avec typage statique
- **React Query (Tanstack Query)**: Gestion de l'état serveur et du cache
- **React Hook Form**: Gestion des formulaires avec validation
- **Zod**: Schéma de validation type-safe
- **Axios**: Client HTTP pour les appels API
- **Shadcn/ui**: Bibliothèque de composants UI

#### Fonctionnalités Clés
- Liste paginée des produits avec recherche
- Formulaire de création/modification de produit
- Validation des données en temps réel
- Feedback utilisateur via notifications
- Interface responsive et accessible

### Backend (Serveur)
Le backend expose une API RESTful pour la gestion des produits.

#### Points d'Entrée API
- `GET /api/v1/products`: Liste des produits (paginée)
- `GET /api/v1/products/{id}`: Détails d'un produit
- `POST /api/v1/products`: Création d'un produit
- `PUT /api/v1/products/{id}`: Mise à jour d'un produit
- `DELETE /api/v1/products/{id}`: Suppression d'un produit
- `GET /api/v1/products/search`: Recherche de produits

## Flux de Données

1. Les données sont validées côté client via Zod avant l'envoi au serveur
2. React Query gère automatiquement le cache et les mises à jour d'UI
3. Les réponses du serveur sont typées via TypeScript
4. Les erreurs sont gérées de manière centralisée avec feedback utilisateur

## Gestion d'État
- État local: Géré via React hooks (useState)
- État formulaire: Géré via React Hook Form
- État serveur: Géré via React Query avec cache automatique

## UI/UX
- Design system cohérent via shadcn/ui
- Feedback immédiat sur les actions utilisateur
- Gestion des états de chargement
- Modales de confirmation pour les actions destructives

## Sécurité
- Validation des données côté client et serveur
- Protection CSRF
- Gestion des erreurs robuste
- Sanitization des entrées utilisateur


## Performance
- Mise en cache optimisée via React Query
- Pagination côté serveur
- Chargement optimisé des données
- Mises à jour optimistes de l'UI
