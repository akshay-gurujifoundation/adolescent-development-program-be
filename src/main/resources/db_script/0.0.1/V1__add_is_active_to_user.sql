-- Add is_active column to user table
ALTER TABLE users 
ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE users_aud
    ADD COLUMN is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- Update existing records to have is_active as true
UPDATE users SET is_active = TRUE WHERE is_active IS NULL;

-- Add comment to the column
COMMENT ON COLUMN users.is_active IS 'Flag to indicate if the user account is active (true) or deactivated (false)';