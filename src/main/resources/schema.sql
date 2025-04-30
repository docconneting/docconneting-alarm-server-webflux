CREATE TABLE alarm_histories (
                id          BIGINT       AUTO_INCREMENT PRIMARY KEY,
                content     VARCHAR(255) NOT NULL,
                to_id       BIGINT       NOT NULL,
                alarm_type  VARCHAR(50)  NOT NULL,
                created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);