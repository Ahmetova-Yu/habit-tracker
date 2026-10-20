CREATE TABLE habits (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    day_of_week VARCHAR(16),
    start_time TIME,
    frequency VARCHAR(16) NOT NULL DEFAULT 'DAILY',
    icon VARCHAR(16),
    current_streak INTEGER NOT NULL DEFAULT 0,
    best_streak INTEGER NOT NULL DEFAULT 0,
    status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
    last_completed_at TIMESTAMPTZ,
    archived_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_habits_user ON habits(user_id);
CREATE INDEX idx_habits_user_status ON habits(user_id, status);