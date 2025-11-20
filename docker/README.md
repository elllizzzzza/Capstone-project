🐳 Local PostgreSQL with Docker

This project includes a Docker Compose setup for running a local PostgreSQL database.
You do not need to install PostgreSQL manually — Docker handles everything.

🚀 Requirements

Docker Desktop (macOS): https://www.docker.com/products/docker-desktop/

⚙️ Setup
1. Create a .env file

Copy the template:
```bash
cp .env.example .env
```

Then update the values inside .env:
```bash
POSTGRES_USER=myuser
POSTGRES_PASSWORD=mypassword
POSTGRES_DB=mydatabase
POSTGRES_PORT=5432
```

▶️ Start the Database

Run inside the directory containing docker-compose.yml:
```bash
docker compose up -d
```

Check that PostgreSQL is running:
```bash
docker ps
```

On the first startup with a new volume, PostgreSQL will automatically run all SQL scripts included in the project under the configured initialization directory. These scripts create your database schema and prepare the database.
If you add new SQL scripts later, you must reset the volume to rerun them (see “Reset the Database” below).

🔌 Connect to the Database

Use your preferred SQL client (DBeaver, DataGrip, TablePlus, etc.) with the following settings:

Setting	Value
```bash
Host	        localhost
Port	        value from POSTGRES_PORT
Database	value from POSTGRES_DB
Username	value from POSTGRES_USER
Password	value from POSTGRES_PASSWORD
```

🗃 Using the Database Schema

Your database schema is automatically applied on the first database startup.

Option A — Using psql:
```bash
docker exec -it local-postgres psql -U $POSTGRES_USER -d $POSTGRES_DB
```

Then list tables:
```bash
\dt
```

Query example:

```bash
SELECT * FROM users;
```

Option B — Using a SQL client:

Open a connection → expand the schema → view tables.

⏹ Stop the Database
```bash
docker compose down
```

Data is preserved in the Docker volume postgres_data.

♻️ Reset the Database (rerun scripts)

If you need PostgreSQL to execute the included SQL scripts again:
```bash
docker compose down -v
docker compose up -d
```

The -v flag removes all stored data in the volume.

On next startup, PostgreSQL will rerun all initialization scripts.

## 💡 Quick Command Cheatsheet
```bash
# Start PostgreSQL
docker compose up -d

# Stop PostgreSQL
docker compose down

# Reset DB (rerun init scripts)
docker compose down -v
docker compose up -d

# Connect via psql
docker exec -it local-postgres psql -U $POSTGRES_USER -d $POSTGRES_DB

# List tables inside psql
\dt

# Sample query inside psql
SELECT * FROM users;
```