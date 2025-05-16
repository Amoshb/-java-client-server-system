# java-client-server-system
This project is the final coursework submission for COMP1549: Advanced Programming at the University of Greenwich (2023/24). It demonstrates the implementation of a **networked distributed system** using **Java**, following object-oriented programming principles, design patterns, and testing via JUnit.

## Objective

The goal of this project is to implement a **CLI-based distributed group communication system** where clients can join a group through a central server, send broadcast and private messages, and automatically handle coordinator selection, disconnection, and fault tolerance.

---

## Key Features

- **Client-Server Architecture:** Centralised server handling multiple client connections
- **Coordinator Management:** Automatic selection of a coordinator among clients
- **Messaging System:** Support for both broadcast and private messages
- **State Maintenance:** Coordinator tracks active clients and responds to membership queries
- **Command-Line Interface (CLI):** Inputs for client ID, server IP, and port
- **JUnit Tests:** Included to verify core application logic (e.g., coordinator reassignment)
- **Design Patterns:** Applied across key classes (Observer, Singleton, etc.)

---

## Technologies Used
- Java
- JUnit (Unit Testing)
- Socket Programming
- Multithreading
- Command-Line Interface (CLI)
