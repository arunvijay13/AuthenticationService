

CREATE TABLE flames (
  id BIGINT AUTO_INCREMENT NOT NULL,
   username VARCHAR(255) NULL,
   partner1 VARCHAR(255) NULL,
   partner2 VARCHAR(255) NULL,
   result VARCHAR(255) NULL,
   created_at TIMESTAMP,
   CONSTRAINT pk_user PRIMARY KEY (id)
);