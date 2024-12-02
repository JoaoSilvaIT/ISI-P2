<p align="left">
    <a href="https://www.isel.pt/en" target="_blank">
        <img width="25%" src="https://www.isel.pt/sites/default/files/SCI/Identidade/logo_ISEL_simplificado_cor.png" alt="ISEL logo">
    </a>
</p>

# Scooter Sharing System

[![version](https://img.shields.io/badge/version-1.0-green)](https://img.shields.io/badge/version-1.0-green)

## Introduction to Information Systems - ISEL-DEETC

This repository contains the implementation of a Scooter Sharing System, developed as didactic material to support the Introduction to Information Systems course at ISEL-DEETC.

## Overview

This project implements a basic scooter sharing system. The system is designed for teaching purposes and demonstrates fundamental concepts of database interaction and a simple object-oriented programming.

## Project Structure

The code is organized into several core classes:

- **Scooter**: Base class containing attributes matching the SCOOTER table
- **ScooterModel**: Extension for scooters model
- **Person**: Base class containing attributes matching the PERSON table
- **User**: Extension for users (CLIENT table)
- **Card**: Base class containing attributes matching the CARD and TYPEOF tables
- **Model**: Core class containing application methods
- **Restriction**: Handles data model constraints and validation

## Important Notes

1. **Code Organization**
   - The existing class structure should be maintained
   - No additional classes should be created
   - The initial configuration should remain unchanged

2. **Class Purposes**
   - Scooter classes (Scooter, ScooterModel) map to database tables
   - Person classes (Person, User, Card) map to database tables
   - Model class implements all application logic
   - Restriction class manages data integrity and validation

3. **Database Configuration**
   - Database connection settings must be configured in the designated file (see below)
   - Required settings:
     - Database Name
     - Database User
     - Password

## Setup Instructions

1. Clone the repository
2. Configure database connection settings
3. Compile the project
4. Run the application

## Database Configuration

Update the following settings in your connection configuration:

```java
db.url = "jdbc:postgresql://[IP_ADDRESS]:[PORT]/[DATABASE]";
db.user = "[USERNAME]";
db.password = "[PASSWORD]";
```

Use the file `database.properties` to change the values.

## Usage Restrictions

This code is provided for educational purposes and may:

- Not be complete in all aspects
- Contain deliberate simplifications
- Include points for classroom discussion
- Serve as a basis for teaching specific concepts

## License

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](https://choosealicense.com/licenses/mit/)
![maintained](https://img.shields.io/badge/Maintained%3F-yes-green.svg)

[![Linux](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)](https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)
[![macOS](https://img.shields.io/badge/mac%20os-000000?style=for-the-badge&logo=apple&logoColor=white)](https://img.shields.io/badge/mac%20os-000000?style=for-the-badge&logo=apple&logoColor=white)
[![docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)

MIT License

Copyright (c) 2024, Matilde Pato, ISEL-DEETC

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

## Educational Context

This material is designed to support the Introduction to Information Systems course. The examples may:

- Be intentionally simplified for teaching purposes
- Include points for classroom discussion
- Serve as practical exercises
- Demonstrate specific programming concepts

## Contributing

As this is educational material, modifications should be discussed with the course instructor. The current structure should be maintained to align with teaching objectives.

## Contact

For questions or clarifications, please contact:

- Matilde Pato, ISEL-DEETC

---
*Note: This code is meant for educational purposes and any inaccuracies are subject to discussion in classes.*