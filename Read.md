### Pourquoi des DTO et un Mapper ?

- **DTO** (Data Transfer Object) : objets d’échange exposés par l’API. Ils sont différents des entités JPA.
  - Sécurisent le contrat d’API (on ne renvoie que ce qui est nécessaire).
  - Permettent la **validation** des entrées (`spring-boot-starter-validation`).
  - Découplent l’API de la couche persistence.

- **Mapper** : convertit DTO ⇄ Entité au même endroit.
  - Simplifie les contrôleurs/services.
  - Évite la duplication et facilite les tests.



###  pourquoi utiliser CORS dans notre Microservice
# Dans ce microservice, on a activé une configuration CORS personnalisée dans SecurityConfig.java, avec les règles suivantes 

Origines autorisées : http://localhost:3000 (Nuxt.js en dev)
* Méthodes autorisées : GET, POST, PUT, DELETE, OPTIONS
* En-têtes acceptés : Authorization, Content-Type, etc.
* Stateless : aucune session, tout passe par le token JWT.

Cela garantit que seul un client front-end connu peut communiquer avec l’API, tout en respectant les standards de sécurité modernes.



### Docker : conteneurisation et construction de nos images :
* Compiler le jar   :     .\mvnw.cmd clean package -DskipTests
* Construire l’image Docker  :   docker compose build
* Lancer le conteneur :  docker compose -up d


### eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBTEVYQU5EUkUiLCJpYXQiOjE3NTQ5MDA3NjQsImV4cCI6MTc1NDkwNDM2NCwicm9sZXMiOlsiUk9MRV9VU0VSIl19.KwyUjkks8Amh0e0GEnKR0kgQ95bvJiMqGlMNYiE2BVM


### tests unitaires avec Junit 5 et Mockito

- Parfait — on va tester ton ProduitRepository avec un slice JPA.

Ce que les tests vont vérifier

save persiste bien un Produit (et génère id + createdAt via @PrePersist).

findById retrouve l’entité persistée.

findAll renvoie plusieurs lignes.

delete supprime bien l’entité.
