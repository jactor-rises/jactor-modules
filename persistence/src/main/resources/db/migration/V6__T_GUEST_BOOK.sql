CREATE TABLE T_GUEST_BOOK
(
  ID            BIGINT PRIMARY KEY,
  CREATION_TIME TIMESTAMP   NOT NULL,
  CREATED_BY    VARCHAR(50) NOT NULL,
  UPDATED_TIME  TIMESTAMP   NOT NULL,
  UPDATED_BY    VARCHAR(50) NOT NULL,
  TITLE         VARCHAR(50) NOT NULL,
  USER_ID       BIGINT      NOT NULL
);

CREATE SEQUENCE T_GUEST_BOOK_SEQ INCREMENT BY 1 START WITH 100000;

ALTER TABLE T_GUEST_BOOK
  ADD CONSTRAINT fk_gb_user_id FOREIGN KEY (USER_ID)
    REFERENCES T_USER (ID);