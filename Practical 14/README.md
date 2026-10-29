# Practical 14: Layered Application with JUnit Testing

## Overview
This practical demonstrates building a tiny 3-layer Java application and testing it with **JUnit 4** before testing the larger MiniBank project.

### Architecture Layers
1. **Model Layer (`Item.java`)**: Represents an entity with encapsulation and input validation (`id`, `name`, `price`, `quantity`).
2. **Service Layer (`ItemService.java`)**: Encapsulates business logic and uses an `ArrayList<Item>` as an in-memory data store. Validates operations and throws `IllegalArgumentException` on invalid actions.
3. **Menu / Presentation Layer (`ItemApp.java`)**: Interactive command-line interface with a formatted menu loop driven by `Scanner`.
4. **Test Layer (`ItemServiceTest.java`)**: Automated unit test suite using JUnit `@Test` annotations to verify that adding increases the count and invalid actions throw appropriate exceptions.

---

## File Structure

```
Practical 14/
├── Item.java             # Model Layer
├── ItemService.java      # Service Layer (uses ArrayList)
├── ItemApp.java          # Menu Main (UI / Presentation Layer)
├── ItemServiceTest.java  # JUnit 4 Test Suite
├── README.md             # Documentation and usage guide
└── lib/                  # Bundled dependencies
    ├── hamcrest-core-1.3.jar
    ├── junit-4.13.2.jar
    ├── junit-jupiter-api-5.10.3.jar
    ├── junit-jupiter-engine-5.10.3.jar
    └── opentest4j-1.3.0.jar
```

---

## Compilation & Execution

Open PowerShell or terminal in the `Practical 14` directory:

```powershell
cd "d:\Java\Practical 14"
```

### 1. Compile All Files
```powershell
javac -cp ".;lib/*" Item.java ItemService.java ItemApp.java ItemServiceTest.java
```

### 2. Run the Interactive Menu Application
```powershell
java -cp . ItemApp
```

### 3. Run the JUnit Unit Tests
You can run the test suite directly using its built-in runner:
```powershell
java -cp ".;lib/*" ItemServiceTest
```
Or execute it via JUnit's `JUnitCore`:
```powershell
java -cp ".;lib/*" org.junit.runner.JUnitCore ItemServiceTest
```

---

## Unit Tests Implemented

| Test Method | Description | Assertion / Expected Behavior |
| :--- | :--- | :--- |
| `testAddIncreasesCount()` | Adding items increases inventory count | Verified count starts at 0, becomes 1, then 2 |
| `testAddNullItemThrowsException()` | Passing a `null` item | Throws `IllegalArgumentException` |
| `testAddDuplicateItemThrowsException()` | Adding an item with an existing ID | Throws `IllegalArgumentException` |
| `testFindNonExistentItemThrowsException()` | Searching for an ID not present | Throws `IllegalArgumentException` |
| `testRemoveNonExistentItemThrowsException()`| Removing an item that does not exist | Throws `IllegalArgumentException` |
| `testRemoveDecreasesCount()` | Removing a valid existing item | Verified count decrements |
