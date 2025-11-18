🐳 Local PostgreSQL with Docker

This project includes a Docker Compose setup for running a local PostgreSQL database.
You do not need to install PostgreSQL on your machine — Docker handles everything.

🚀 Requirements

Docker Desktop (macOS): https://www.docker.com/products/docker-desktop/

⚙️ Setup
1. Create a .env file

Copy the template:

cp .env.example .env


Update the values in .env:

POSTGRES_USER=myuser
POSTGRES_PASSWORD=mypassword
POSTGRES_DB=mydatabase
POSTGRES_PORT=5432

▶️ Start the Database

Run inside the directory containing docker-compose.yml:

docker compose up -d


Check that it’s running:

docker ps

🔌 Connect to the Database

Use your preferred SQL client (e.g., DBeaver) with:

Setting	Value
Host	localhost
Port	value from POSTGRES_PORT
Database	value from POSTGRES_DB
Username	value from POSTGRES_USER
Password	value from POSTGRES_PASSWORD
⏹ Stop the Database
docker compose down


Data is preserved using the postgres_data Docker volume.