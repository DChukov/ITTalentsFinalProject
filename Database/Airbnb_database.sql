-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema AirbnbDatabase
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema AirbnbDatabase
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `AirbnbDatabase` DEFAULT CHARACTER SET utf8 ;
USE `AirbnbDatabase` ;

-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `pasword` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `birth_date` DATE NULL,
  `phone` VARCHAR(20) NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Cities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Cities` (
  `city_id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`city_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Rooms`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Rooms` (
  `room_id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(45) NOT NULL,
  `guests` INT NULL,
  `bedrooms` INT NULL,
  `beds` INT NULL,
  `baths` INT NULL,
  `photos` VARCHAR(45) NULL,
  `price` VARCHAR(45) NOT NULL,
  `details` VARCHAR(100) NOT NULL,
  `city_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`room_id`),
  INDEX `fk_Rooms_Cities1_idx` (`city_id` ASC) VISIBLE,
  INDEX `fk_Rooms_Users1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_Rooms_Cities1`
    FOREIGN KEY (`city_id`)
    REFERENCES `AirbnbDatabase`.`Cities` (`city_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rooms_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `AirbnbDatabase`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Amenities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Amenities` (
  `amenity_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`amenity_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Bookings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Bookings` (
  `booking_id` INT NOT NULL AUTO_INCREMENT,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `user_id` INT NOT NULL,
  `room_id` INT NOT NULL,
  PRIMARY KEY (`booking_id`),
  INDEX `fk_Bookings_Users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_Bookings_Rooms1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_Bookings_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `AirbnbDatabase`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Bookings_Rooms1`
    FOREIGN KEY (`room_id`)
    REFERENCES `AirbnbDatabase`.`Rooms` (`room_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`RoomsAmenities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`RoomsAmenities` (
  `amenity_id` INT NOT NULL,
  `room_id` INT NOT NULL,
  INDEX `fk_RoomsAmenities_Amenities1_idx` (`amenity_id` ASC) VISIBLE,
  INDEX `fk_RoomsAmenities_Rooms1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_RoomsAmenities_Amenities1`
    FOREIGN KEY (`amenity_id`)
    REFERENCES `AirbnbDatabase`.`Amenities` (`amenity_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_RoomsAmenities_Rooms1`
    FOREIGN KEY (`room_id`)
    REFERENCES `AirbnbDatabase`.`Rooms` (`room_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `AirbnbDatabase`.`Review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AirbnbDatabase`.`Review` (
  `review_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `text` VARCHAR(100) NULL,
  `user_id` INT NOT NULL,
  `room_id` INT NOT NULL,
  ` stars` INT NOT NULL,
  PRIMARY KEY (`review_id`),
  INDEX `fk_Review_Users1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_Review_Rooms1_idx` (`room_id` ASC) VISIBLE,
  CONSTRAINT `fk_Review_Users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `AirbnbDatabase`.`Users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Review_Rooms1`
    FOREIGN KEY (`room_id`)
    REFERENCES `AirbnbDatabase`.`Rooms` (`room_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
