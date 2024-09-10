-- Connect to PostgreSQL as a superuser or admin (e.g., postgres user)

-- Step 1: Create the Database
CREATE
DATABASE adolescent_dev_program;

-- Step 2: Create the User
CREATE
USER adolescent_user WITH PASSWORD 'Adolescent@123';

-- Step 3: Grant All Privileges on the Database to the User
GRANT ALL PRIVILEGES ON DATABASE
adolescent_dev_program TO adolescent_user;

-- Grant usage on the 'public' schema
GRANT USAGE ON SCHEMA
public TO adolescent_user;

-- Grant privileges to create objects in the 'public' schema
GRANT CREATE
ON SCHEMA public TO adolescent_user;

-- Optionally, grant all privileges on the schema
GRANT ALL
ON SCHEMA public TO adolescent_user;


-- Grant all privileges on all tables in the 'public' schema
GRANT ALL PRIVILEGES ON ALL
TABLES IN SCHEMA public TO adolescent_user;

-- Grant all privileges on all sequences in the 'public' schema
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA public TO adolescent_user;


ALTER
DATABASE adolescent_dev_program OWNER TO adolescent_user;


      -- School Table
CREATE TABLE school
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL,
    address              VARCHAR(255),
    phone_number         VARCHAR(15),
    principal_name       VARCHAR(255),
    principal_contact_no VARCHAR(15),
    managing_trustee     VARCHAR(255),
    trustee_contact_info VARCHAR(15),
    website              VARCHAR(255),
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at           TIMESTAMP
);