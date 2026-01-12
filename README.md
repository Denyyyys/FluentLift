# FluentLift Application

This repository contains three interconnected projects: a mobile application, a frontend application, and a backend application. Together, they form a comprehensive platform designed to help people learn new languages, create their own courses, and discuss topics in the Q&A section.

## Setup
### Prerequisites
Since everything (frontend, backend, PostgreSQL, Neo4j), besides building mobile app for Android, runs inside docker containers - you have to have `Docker` and `Docker Compose` as well as `Git` installed on your machine. `Docker Desktop` contains `Docker` and `Docker Compose`, so you can install this program with `Git`.

Optionally, if you want to run web scraping scripts see the [Web Scraper](#web-scraper) section below for more info.

Also if you want to build locally mobile app located at `mobile/`, the best way for that is having installed Android Studio with recommended dependencies. Then you can open project in that directory.

### 1. `.env` setup
Firstly clone this project and move to the project directory:
```sh
git clone https://github.com/Denyyyys/FluentLift.git
cd FluentLift/
```

After that create in root directory file `.env` and paste content from `.env.dev`:
```sh
cp .env.dev .env
```

Credentials there are used for local development, so it's better to change at least values for `NEO4J_PASSWORD`, `POSTGRES_PASSWORD` and `SECURITY_JWT_SECRET`. value used for `DEEPL_API_KEY` is fake, so if you would use some methods which calls DeepL API, then it will fail until you paste valid API key there.

### 2. Running app
Then you can start docker compose:
```sh
docker compose -f docker-compose.yml up --build
```

Building of frontend and backend will begin as well as setting up PostgreSQL and Neo4j databases. Additionally, if it is first run, then data for synsets and conjugation of two Polish verbs (opuszczać, opuścić) will be seeded in Neo4j database.

### 3. Test if everyhing works

#### Backend
If you want to check if backend actually runs, you can import `docs/backend-postman-test-collections.json` into your Postman. And make some requests to backend. Almost every request requires JWT, so you can start by creating new user from `User` folder in that collection and making POST request called `generateToken` there. After that, copy response to the `{{Jwt}}` variable to make it accessible in every request in this collection.

#### Frontend
If you want to check frontend, you can go to the `http://localhost:5173/` in web browser and you should see sign up screen:

<img width="368" height="237" alt="image" src="https://github.com/user-attachments/assets/72d0d89f-8721-4f4c-af5e-1510d00c79a4" />


## Optional Setups
### Web Scraper
In order to use web scraping scripts to get data from BabelNet, you need to have `Python 3` with `pip` installed locally. Then install required packages from `/web_scraper/requirements.txt` file using:
```sh
pip install -r ./web_scraper/requirements.txt
```

Spider located in `web_scraper/babelNetScraper/babelNetScraper/spiders/babelnet.py` reads data from file with most popular English words, then try to find synsets which includes that word. Update `number_of_words` and `toSkip` variables in order to change range of words which will be used to scrape data. BabelNet has limit of approximately 200 requests per day, so don't try to run script with all words from file `web_scraper/babelNetScraper/babelNetScraper/spiders/words_english_all_filered.csv`.

After that you can start crawler using:
```sh
cd web_scraper/babelNetScraper

scrapy crawl babelnet
```

Results will be appended in file `web_scraper/data/synsets/synsets.json`.

## Projects
### 1. Mobile Application 
> [!CAUTION]
> Currently is under construction

- Provides a user-friendly interface for learning languages on the go.

- Features interactive lessons, quizzes, and progress tracking.
- Available for Android platform.

### 2. Front-End Application
> [!CAUTION]
> Currently is under construction

- A web-based interface where users can access all features of the language learning platform.
- Allows users to browse courses, take lessons, and participate in discussions.
- Built with modern web technologies for a seamless user experience.

### 3. Back-End Application
> [!WARNING]
> Not all functionality is implemented.

- Powers the platform with APIs and database management.
- Handles user authentication, course creation, and discussion forums.
- Ensures data security and smooth integration between the mobile and front-end applications.

## Features
- Language Learning: Access a wide range of courses to learn new languages effectively.
- Course Creation: Users can create their own courses and share them with the community.
- Discussion Forum: Engage with other learners by discussing topics and asking questions in the Q&A section.
