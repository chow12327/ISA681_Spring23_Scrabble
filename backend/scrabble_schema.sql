-- -----------------------------------------------------
-- Schema scrabble
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `scrabble`;

CREATE SCHEMA `scrabble`;
USE `scrabble` ;



-- -----------------------------------------------------
-- Table `scrabble`.`letters`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`letters` (
  `letterID` BIGINT NOT NULL AUTO_INCREMENT,
  `alphabet` CHAR(1) NOT NULL,
  `letter_score` TINYINT UNSIGNED NOT NULL,
  `count` TINYINT UNSIGNED NOT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`letterID`)
) 
AUTO_INCREMENT = 1;

-- -----------------------------------------------------
-- Table `scrabble`.`grid`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`grid` (
  `indexID` BIGINT NOT NULL AUTO_INCREMENT,
  `x_coordinate` TINYINT UNSIGNED NOT NULL,
  `y_coordinate` TINYINT UNSIGNED NOT NULL,
  `count` SMALLINT NOT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`indexID`)
) 
AUTO_INCREMENT = 1;




-- -----------------------------------------------------
-- Table `scrabble`.`player`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`player` (
  `playerID` BIGINT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(25) NULL DEFAULT NULL,
  `last_name` VARCHAR(25) NULL DEFAULT NULL,
  `username` VARCHAR(25) NULL DEFAULT NULL,
  `emailID` VARCHAR(320) NULL DEFAULT NULL,
  `password` BINARY(60) NULL DEFAULT NULL,
  `salt` BINARY(64) NULL DEFAULT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`playerID`),
  KEY(`username`))
AUTO_INCREMENT = 1;

-- -----------------------------------------------------
-- Table `scrabble`.`game`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`game` (
  `gameID` BIGINT NOT NULL AUTO_INCREMENT,
  `is_finished` BOOLEAN NOT NULL DEFAULT FALSE,
  `is_draw` BOOLEAN NOT NULL DEFAULT FALSE,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`gameID`)
 
) 
AUTO_INCREMENT = 1;



-- -----------------------------------------------------
-- Table `scrabble`.`GamePlayers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`gameplayers` (
  `gameplayerID` BIGINT NOT NULL AUTO_INCREMENT,
  `gameID` BIGINT NOT NULL,
  `playerID`BIGINT NOT NULL,
  `is_winner` BOOLEAN NULL DEFAULT NULL,
  `is_turn` BOOLEAN NULL DEFAULT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`gameplayerID`),
  CONSTRAINT `fk_gameplayersgameID` FOREIGN KEY (`gameID`) REFERENCES `game` (`gameID`),
  CONSTRAINT `fk_gameplayersplayerID` FOREIGN KEY (`playerID`) REFERENCES `player` (`playerID`)
);



-- -----------------------------------------------------
-- Table `scrabble`.`GameMoves`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`gamemoves` (
  `moveID` BIGINT NOT NULL AUTO_INCREMENT,
  `gameID` BIGINT NOT NULL,
  `playerID` BIGINT NOT NULL,
  `total_score` SMALLINT NULL DEFAULT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`moveID`),
  CONSTRAINT `fk_gamemovesgameID` FOREIGN KEY (`gameID`) REFERENCES `game` (`gameID`),
  CONSTRAINT `fk_gamemovesplayerID` FOREIGN KEY (`playerID`) REFERENCES `player` (`playerID`)
)
AUTO_INCREMENT = 1;


-- -----------------------------------------------------
-- Table `scrabble`.`movelocation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`movelocation` (
  `moveLocationID` BIGINT NOT NULL AUTO_INCREMENT,
  `moveID` BIGINT NOT NULL,
  `gridIndexID` BIGINT NOT NULL,
  `letterID` BIGINT NOT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`moveLocationID`),
  CONSTRAINT `fk_movelocationmoveID` FOREIGN KEY (`moveID`) REFERENCES `gamemoves` (`moveID`),
  CONSTRAINT `fk_movelocationgridIndexID` FOREIGN KEY (`gridIndexID`) REFERENCES `grid` (`indexID`),
  CONSTRAINT `fk_movelocationletterID` FOREIGN KEY (`letterID`) REFERENCES `letters` (`letterID`)
) 
AUTO_INCREMENT = 1;



-- -----------------------------------------------------
-- Table `scrabble`.`movewords`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`movewords` (
  `wordID` BIGINT NOT NULL AUTO_INCREMENT,
  `moveID` BIGINT NOT NULL,
  `word` VARCHAR(6) NOT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`wordID`),
  CONSTRAINT `fk_movewordsmoveID` FOREIGN KEY (`moveID`) REFERENCES `gamemoves` (`moveID`)
) 
AUTO_INCREMENT = 1;


-- -----------------------------------------------------
-- Table `scrabble`.`gameplayerletters`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `scrabble`.`gameplayerletters` (
  `gameplayerletterID` BIGINT NOT NULL AUTO_INCREMENT,
  `gameplayerID` BIGINT NOT NULL,
  `letterID` BIGINT NOT NULL,
  `moveID` BIGINT NOT NULL,
  `is_used` BOOLEAN NOT NULL,
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`gameplayerletterID`),
  CONSTRAINT `fk_gameplayerlettersgameplayerID` FOREIGN KEY (`moveID`) REFERENCES `gameplayers` (`gameplayerID`),
  CONSTRAINT `fk_gameplayerlettersletterID` FOREIGN KEY (`letterID`) REFERENCES `letters` (`letterID`),
  CONSTRAINT `fk_gameplayerlettersmoveID` FOREIGN KEY (`moveID`) REFERENCES `gamemoves` (`moveID`)
) 
AUTO_INCREMENT = 1;

