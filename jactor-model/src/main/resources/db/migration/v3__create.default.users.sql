INSERT INTO T_PERSON (DESCRIPTION) VALUES ('jactor.desc');
INSERT INTO T_PERSON (DESCRIPTION) VALUES ('tip.desc');

INSERT INTO T_USER (USER_NAME, EMAIL, PERSON_ID) VALUES ('jactor', 'tor.egil.jacobsen@gmail.com', (
  SELECT ID
  FROM T_PERSON
  WHERE DESCRIPTION = 'jactor.desc'
));

INSERT INTO T_USER (USER_NAME, EMAIL, PERSON_ID) VALUES ('tip', 'suthatip.jacobsen@gmail.com', (
  SELECT ID
  FROM T_PERSON
  WHERE DESCRIPTION = 'tip.desc'
));
