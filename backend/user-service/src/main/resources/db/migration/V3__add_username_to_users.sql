ALTER TABLE users ADD COLUMN username VARCHAR(50);

UPDATE users
SET username = SPLIT_PART(email, '@', 1)
WHERE username IS NULL;

ALTER TABLE users ALTER COLUMN username SET NOT NULL;

ALTER TABLE users ADD CONSTRAINT uk_users_username UNIQUE (username);