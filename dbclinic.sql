-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 23, 2016 at 11:35 AM
-- Server version: 5.6.12-log
-- PHP Version: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `dbclinic`
--
CREATE DATABASE IF NOT EXISTS `dbclinic` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `dbclinic`;

-- --------------------------------------------------------

--
-- Table structure for table `docter`
--

CREATE TABLE IF NOT EXISTS `docter` (
  `Name` varchar(30) NOT NULL,
  `NIC` varchar(30) NOT NULL,
  `Specialty` varchar(30) NOT NULL,
  PRIMARY KEY (`NIC`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `docter`
--

INSERT INTO `docter` (`Name`, `NIC`, `Specialty`) VALUES
('Miss.Shamali', '776786754v', 'General'),
('Mr.Gunawardan', '879876756v', 'Eye');

-- --------------------------------------------------------

--
-- Table structure for table `drug`
--

CREATE TABLE IF NOT EXISTS `drug` (
  `DrugId` int(11) NOT NULL,
  `DrugName` varchar(30) NOT NULL,
  `ExpiryDate` varchar(30) NOT NULL,
  PRIMARY KEY (`DrugId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `drug`
--

INSERT INTO `drug` (`DrugId`, `DrugName`, `ExpiryDate`) VALUES
(1, 'AMOXILINE', '2017-05-31'),
(2, 'DOMPERIDONE', 'Dec 30, 2017'),
(3, 'LOSARTAN', 'Dec 31, 2016');

-- --------------------------------------------------------

--
-- Table structure for table `incompatibledrug`
--

CREATE TABLE IF NOT EXISTS `incompatibledrug` (
  `DrugId` int(11) NOT NULL,
  `IncompatibleDrugId` int(11) NOT NULL,
  PRIMARY KEY (`DrugId`,`IncompatibleDrugId`),
  KEY `IncompatibleDrugId` (`IncompatibleDrugId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `incompatibledrug`
--

INSERT INTO `incompatibledrug` (`DrugId`, `IncompatibleDrugId`) VALUES
(1, 2);

-- --------------------------------------------------------

--
-- Table structure for table `patient`
--

CREATE TABLE IF NOT EXISTS `patient` (
  `PatientNIC` varchar(11) NOT NULL,
  `Status` varchar(30) NOT NULL,
  `FirstName` varchar(30) NOT NULL,
  `LastName` varchar(30) NOT NULL,
  `Address` varchar(50) NOT NULL,
  `Age` int(3) NOT NULL,
  PRIMARY KEY (`PatientNIC`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `patient`
--

INSERT INTO `patient` (`PatientNIC`, `Status`, `FirstName`, `LastName`, `Address`, `Age`) VALUES
('111111111v', 'Miss', 'Nimal', 'Sirisena', 'Karawanell', 23),
('232342341v', 'Miss.', 'Kamali', 'Amarthunga', 'Avisawella', 14),
('768756784v', 'Mr.', 'Kamal', 'Karunarathna', 'Kelaniya', 25),
('789999999v', 'Mr', 'Saman', 'Gunarathna', 'Avissawell', 67),
('888888888v', 'Miss', 'Shamin', 'Kaumaru', 'Yatiyantota', 32),
('897867564v', 'Mrs', 'Kamal', 'Gunarathna', 'Avissawella', 31),
('927913534v', 'Ms.', 'Amali', 'Gunarathna', 'Avissawella', 31);

-- --------------------------------------------------------

--
-- Table structure for table `prescription`
--

CREATE TABLE IF NOT EXISTS `prescription` (
  `PrescriptionID` int(11) NOT NULL,
  `PatientNIC` varchar(30) NOT NULL,
  `DocterNIC` varchar(30) NOT NULL,
  `Date` varchar(30) NOT NULL,
  PRIMARY KEY (`PrescriptionID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prescription`
--

INSERT INTO `prescription` (`PrescriptionID`, `PatientNIC`, `DocterNIC`, `Date`) VALUES
(2, '888888888v', '879876756v', '06:29:30AM'),
(3, '927913534v', '879876756v', '06:41:14AM'),
(4, '888888888v', '879876756v', '06:48:23AM'),
(5, '897867564v', '776786754v', '04:53:51PM'),
(6, '927913534v', '776786754v', '04:55:28PM'),
(7, '927913534v', '776786754v', '04:55:33PM'),
(8, '927913534v', '776786754v', '04:55:35PM'),
(9, '927913534v', '776786754v', '04:56:08PM'),
(10, '927913534v', '776786754v', '04:56:12PM'),
(11, '927913534v', '879876756v', '04:58:35PM'),
(12, '927913534v', '776786754v', '04:59:23PM'),
(13, '927913534v', '776786754v', '04:59:27PM'),
(14, '927913534v', '776786754v', '04:59:52PM'),
(15, '927913534v', '776786754v', '04:59:56PM'),
(16, '111111111v', '776786754v', '05:00:44PM'),
(17, '111111111v', '776786754v', '05:01:16PM'),
(18, '927913534v', '776786754v', '05:01:44PM'),
(19, '927913534v', '776786754v', '05:02:06PM'),
(20, '927913534v', '776786754v', '05:02:12PM'),
(21, '111111111v', '776786754v', '05:02:54PM'),
(22, '927913534v', '776786754v', '05:03:25PM'),
(23, '927913534v', '776786754v', '05:03:41PM'),
(24, '927913534v', '776786754v', '05:04:07PM'),
(25, '927913534v', '776786754v', '05:04:12PM'),
(26, '111111111v', '776786754v', '05:04:30PM'),
(27, '111111111v', '776786754v', '05:04:35PM');

-- --------------------------------------------------------

--
-- Table structure for table `prescriptionrecord`
--

CREATE TABLE IF NOT EXISTS `prescriptionrecord` (
  `PrescriptionID` int(11) NOT NULL,
  `DrugId` int(11) NOT NULL,
  `Duration` int(11) NOT NULL,
  `Dosage` int(11) NOT NULL,
  `TimesPerDay` int(11) NOT NULL,
  `TimeTaken` varchar(30) NOT NULL,
  `IncompatibleFlag` int(2) NOT NULL,
  PRIMARY KEY (`PrescriptionID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `prescriptionrecord`
--

INSERT INTO `prescriptionrecord` (`PrescriptionID`, `DrugId`, `Duration`, `Dosage`, `TimesPerDay`, `TimeTaken`, `IncompatibleFlag`) VALUES
(1, 1, 1, 1, 2, 'Before Meal', 0),
(2, 1, 2, 2, 2, 'After Meal', 0),
(3, 1, 3, 3, 1, 'After Meal', 0),
(4, 1, 2, 4, 4, 'Before Meal', 0),
(5, 1, 3, 25, 3, 'Before Meal', 0),
(6, 1, 12, 2, 1, 'After Meal', 0),
(7, 2, 12, 2, 1, 'After Meal', 0),
(8, 2, 12, 2, 1, 'After Meal', 0),
(9, 1, 12, 2, 1, 'After Meal', 0),
(10, 2, 12, 2, 1, 'After Meal', 0),
(11, 1, 12, 2, 1, 'After Meal', 0),
(12, 1, 1, 2, 1, 'After Meal', 0),
(13, 2, 1, 2, 1, 'After Meal', 0),
(14, 1, 2, 3, 1, 'After Meal', 0),
(15, 2, 2, 3, 1, 'After Meal', 0),
(16, 1, 2, 2, 1, 'After Meal', 0),
(17, 1, 2, 2, 1, 'After Meal', 0),
(18, 1, 2, 2, 1, 'After Meal', 0),
(19, 1, 2, 2, 1, 'After Meal', 0),
(20, 2, 2, 2, 1, 'After Meal', 0),
(21, 1, 2, 2, 1, 'After Meal', 0),
(22, 1, 2, 2, 1, 'After Meal', 0),
(23, 2, 2, 2, 1, 'After Meal', 0),
(24, 1, 2, 2, 1, 'After Meal', 0),
(25, 2, 2, 2, 1, 'After Meal', 0),
(26, 1, 2, 2, 1, 'After Meal', 0),
(27, 2, 2, 2, 1, 'After Meal', 0);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `incompatibledrug`
--
ALTER TABLE `incompatibledrug`
  ADD CONSTRAINT `incompatibledrug_ibfk_1` FOREIGN KEY (`DrugId`) REFERENCES `drug` (`DrugId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `incompatibledrug_ibfk_2` FOREIGN KEY (`IncompatibleDrugId`) REFERENCES `drug` (`DrugId`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
