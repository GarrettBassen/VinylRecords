-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema oldSkoolDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema oldSkoolDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `oldSkoolDB` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `oldSkoolDB` ;

-- -----------------------------------------------------
-- Table `oldSkoolDB`.`band`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`band` (
  `band_id` INT NOT NULL,
  `name` VARCHAR(127) NOT NULL,
  PRIMARY KEY (`band_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `oldSkoolDB`.`genre`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`genre` (
  `genre_id` INT NOT NULL,
  `name` VARCHAR(63) NOT NULL,
  PRIMARY KEY (`genre_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `oldSkoolDB`.`media`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`media` (
  `media_id` INT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `medium` ENUM('vinyl', 'CD', 'cassette') NOT NULL,
  `album_format` ENUM('Single', 'EP', 'LP', 'DLP') NOT NULL,
  `year` SMALLINT NOT NULL,
  `band_id` INT NOT NULL,
  PRIMARY KEY (`media_id`),
  INDEX `MEDIA_TO_BAND_idx` (`band_id` ASC) VISIBLE,
  CONSTRAINT `MEDIA_TO_BAND`
    FOREIGN KEY (`band_id`)
    REFERENCES `oldSkoolDB`.`band` (`band_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `oldSkoolDB`.`genre_linker`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`genre_linker` (
  `media_id` INT NOT NULL,
  `genre_id` INT NOT NULL,
  PRIMARY KEY (`media_id`, `genre_id`),
  INDEX `GENRE_LINKER_TO_GENRE_idx` (`genre_id` ASC) VISIBLE,
  CONSTRAINT `GENRE_LINKER_TO_GENRE`
    FOREIGN KEY (`genre_id`)
    REFERENCES `oldSkoolDB`.`genre` (`genre_id`),
  CONSTRAINT `GENRE_LINKER_TO_MEDIA`
    FOREIGN KEY (`media_id`)
    REFERENCES `oldSkoolDB`.`media` (`media_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `oldSkoolDB`.`inventory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`inventory` (
  `media_id` INT NOT NULL,
  `front_good` TINYINT(1) NULL DEFAULT NULL,
  `front_fair` TINYINT(1) NULL DEFAULT NULL,
  `front_poor` TINYINT(1) NULL DEFAULT NULL,
  `back_good` TINYINT(1) NULL DEFAULT NULL,
  `back_fair` TINYINT(1) NULL DEFAULT NULL,
  `back_poor` TINYINT(1) NULL DEFAULT NULL,
  PRIMARY KEY (`media_id`),
  INDEX `INVENTORY_TO_MEDIA_idx` (`media_id` ASC) VISIBLE,
  CONSTRAINT `INVENTORY_TO_MEDIA`
    FOREIGN KEY (`media_id`)
    REFERENCES `oldSkoolDB`.`media` (`media_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `oldSkoolDB`.`request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `oldSkoolDB`.`request` (
  `request_id` INT NOT NULL AUTO_INCREMENT,
  `customer` VARCHAR(63) NOT NULL,
  `phone_number` BIGINT NULL DEFAULT NULL,
  `email` VARCHAR(63) NULL DEFAULT NULL,
  `date` SMALLINT NOT NULL,
  `request` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`request_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO media(media_id, title, medium, album_format, year, band_id)
VALUES(0, 'null', 'CD', 'EP', 0, 0);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;