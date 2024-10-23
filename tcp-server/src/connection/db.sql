CREATE TABLE BTL_LTM.users (
  userId INT IDENTITY(1,1) NOT NULL,
  username VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  score FLOAT NOT NULL,
  win INT NOT NULL,
  draw INT NOT NULL,
  lose INT NOT NULL,
  avgCompetitor FLOAT NOT NULL,
  avgTime FLOAT NOT NULL,
  PRIMARY KEY (userId)
);
