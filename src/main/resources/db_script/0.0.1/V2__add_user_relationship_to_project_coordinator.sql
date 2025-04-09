-- Add user_id column to project_coordinator table
ALTER TABLE project_coordinator 
ADD COLUMN user_id BIGINT;

ALTER TABLE project_coordinator_aud
    ADD COLUMN user_id BIGINT;

-- Add foreign key constraint
ALTER TABLE project_coordinator
ADD CONSTRAINT fk_project_coordinator_user
FOREIGN KEY (user_id) 
REFERENCES users(id)
ON DELETE CASCADE;

-- Add comment to the column
COMMENT ON COLUMN project_coordinator.user_id IS 'Foreign key reference to users table';

-- Add index for better query performance
CREATE INDEX idx_project_coordinator_user_id ON project_coordinator(user_id);

