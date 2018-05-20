CREATE SCHEMA IF NOT EXISTS JACTOR;

CREATE SEQUENCE guest_book_entry_seq
  START WITH 100000
  INCREMENT BY 1;

CREATE TABLE T_GUEST_BOOK_ENTRY (
  ID            BIGINT (19) DEFAULT guest_book_entry_seq.nextval PRIMARY KEY,
  CREATION_TIME TIMESTAMP   NOT NULL,
  CREATED_BY    VARCHAR(50) NOT NULL,
  UPDATED_TIME  TIMESTAMP   NOT NULL,
  UPDATED_BY    VARCHAR(50) NOT NULL,
  CREATED_TIME  VARCHAR(50) NOT NULL,
  GUEST_NAME    VARCHAR(50) NOT NULL,
  ENTRY         CLOB        NOT NULL,
  GUEST_BOOK_ID BIGINT (19) NOT NULL
);

ALTER TABLE T_GUEST_BOOK_ENTRY
  ADD CONSTRAINT fk_gb_bookentry_id FOREIGN KEY (GUEST_BOOK_ID)
REFERENCES T_GUEST_BOOK (ID);
