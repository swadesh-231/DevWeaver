INSERT INTO users (username, password, email, image_url, created_at, updated_at)
VALUES
    ('john_doe', 'Password@123', 'john@example.com', 'https://example.com/john.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('jane_smith', 'Secure@456', 'jane@example.com', 'https://example.com/jane.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('admin_user', 'Admin@789', 'admin@example.com', 'https://example.com/admin.jpg', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);