# codetest-trelleborg

Java coding test of Trelleborg company

## System Requirements

- Java 20
- Gradle
- Intellij IDEA

## How to build?

./gradlew clean build

## How to run?

Import project to Intellij Idea and run Application class

## Main Components

- TripImportJob: run import data and export data to all required csv.

- TripProcessor: process import trip data into database

- TripValidator: validate touch and trip data

- TripDB: storage for trip, company bus trip and invalid touch data
  - MemoryTripDB: light weigth version, in-memory database
  
- FileExporter: contract to export data to specific output file
  - AbstractCsvExporter: abstract base class for all CSV file format exporter
    - TripExporter: export trip data to csv file
    - CompanyTripExporter: export summary of each company and bus trip
    - TouchErrorExporter: export all invalid touch data
