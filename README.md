# FluentLift

FluentLift is a language-learning platform consisting of:

- Web frontend
- Backend API
- PostgreSQL & Neo4j databases
- Mobile application (only for Android)

The core application runs fully in Docker and is intended for local development.

## Architecture Overview

- **Frontend**: Web application for learners and course creators
- **Backend**: REST API handling authentication, managing courses, Q&A, progress and more
- **Databases**:
  - PostgreSQL - relational data (courses; users; questions; decks and cards; etc.)
  - Neo4j - linguistic graph data (cognitive synonymy, aka synsets; conjugations)

## Running the Web Application (Local Development)

This runs the **main FluentLift platform** (backend + frontend + databases).

### Prerequisites

- Docker (with Docker Compose)

### Environment Configuration
> [!IMPORTANT]
> Docker Compose relies on environment variables defined in the `.env` file.
> 
> Without it, required configuration values (such as database credentials and secrets) will be missing, and the containers will fail to start.

```sh
cp .env.dev .env
```

The default credentials are intended for local development.
For security reasons, consider changing:

- **POSTGRES_PASSWORD**
- **NEO4J_PASSWORD**
- **SECURITY_JWT_SECRET**

The **DEEPL_API_KEY** value is a placeholder and must be replaced if you plan to use DeepL-related features (currently only used by backend runners for translating selected synset fields).

### Start the Application

From the project root, run:

```sh
docker compose up
```

On first run, the system will:

- Pull prebuilt backend and frontend images from Docker Hub
- Start PostgreSQL and Neo4j
- Seed Neo4j with initial linguistic data

> [!NOTE]
> The backend and frontend services use prebuilt images from Docker Hub.
>
> If you want to modify the source code and rebuild locally, update `docker-compose.yml` to use `build:` instead of `image:`.
> 
> Dockerfiles for both services are provided in their respective directories.
### Verifying the Setup

#### Backend

You can test the backend API using **Postman**:

- Import `docs/backend-postman-test-collections.json`
- Create a user using a request from the `User` folder called `addUser`
- Copy the JWT token from the response and assign it to the `{{Jwt}}` variable in the collection

To log in to an existing account, you can use the `generateToken` request with the credentials of a previously created user, then copy the JWT token from the response and assign it to the `{{Jwt}}` variable in the collection.

After this, you can test any other endpoint in the collection as an authenticated user.

#### Frontend

Open the web application in your browser:

```
http://localhost:5173
```

You should see the sign-up screen:

<img width="368" height="237" alt="image" src="https://github.com/user-attachments/assets/72d0d89f-8721-4f4c-af5e-1510d00c79a4" />

## Mobile Application (Android)

The Android mobile application is located in the `mobile/` directory.

The mobile app **communicates directly with the backend API**, so the backend **must be running and reachable** for the mobile client to function correctly.

### Backend Requirement

Before running the mobile application, make sure the backend and databases are running by following the instructions in section [Running the Web Application (Local Development)](#running-the-web-application-local-development).

This approach is **strongly recommended**, as it automatically:

- Starts the backend
- Configures PostgreSQL and Neo4j
- Applies correct environment variables and networking

### Backend Reachability (Important)

For the mobile app to communicate with the backend, the backend must be reachable from the Android device.

The backend base URL is currently **hardcoded** in the following file `mobile/app/src/main/java/com/example/navigation/data/RetrofitClient.kt` in this line:

```kotlin
private const val BASE_URL = "http://192.168.0.185:8888/"
```

To make this work:

1. Ensure that:
   - Your backend is running (preferably via Docker Compose)
   - Your Android device (or emulator) and backend machine are on the same network (e.g. connected to the same WiFi)

2. Check the local IP address of the computer running the backend.

3. Replace the IP address in `BASE_URL` with that value.

For example, if the computer running the backend has IP address `10.1.1.1`, update the variable to:

```kotlin
private const val BASE_URL = "http://10.1.1.1:8888/"
```

The port (`8888`) must match the backend’s exposed port.

### Alternative Backend Setup (Not Recommended)

You may also run the backend and both databases **manually** (outside of Docker Compose), but this is **not recommended**.

Doing so requires you to:

- Configure PostgreSQL and Neo4j yourself
- Manually assign environment variables
- Ensure correct networking and port exposure
- Keep backend configuration in sync with the mobile app

Using the provided Docker Compose setup avoids these issues and ensures a consistent and predictable development environment.

### Running the Mobile App

To work on the Android application:

1. Install Android Studio with the recommended dependencies
2. Open the `mobile/` directory as a project in Android Studio
3. Ensure the backend is running and reachable before launching the app

## Tooling & Web Scraping

### Web Scraper (BabelNet)

The web scraper is used to collect linguistic data and populate the Neo4j database.
It is intended for development and data preparation purposes only.

**Requirements**:

- Python 3
- pip

Install dependencies:

```sh
pip install -r web_scraper/requirements.txt
```

Run the scraper:

```sh
cd web_scraper/babelNetScraper
scrapy crawl babelnet
```

BabelNet enforces request limits (~200/day).
Adjust `number_of_words` and `toSkip` in the spider configuration accordingly.

Scraped data is appended to:

```
web_scraper/data/synsets/synsets.json
```
