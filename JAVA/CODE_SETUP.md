# Project Installation Guide

## Prerequisites

### 1. Java JDK 20

Download and install the Java Development Kit (JDK) 20 from [Oracle](https://www.oracle.com/java/technologies/javase/jdk20-archive-downloads.html).

### 2. Lombok

Download Lombok from [projectlombok.org](https://projectlombok.org/download).

---

## Integration with IntelliJ IDEA

### i. Install Lombok Plugin

1. Open IntelliJ IDEA and navigate to `Settings` (or `File` -> `Settings` on Windows/Linux, or `IntelliJ IDEA` -> `Preferences` on macOS).

2. In the settings window, go to `Plugins`.

3. Click on `Marketplace` and search for "Lombok."

4. Install the Lombok plugin.

### ii. Add Lombok JAR to Dependencies

1. In the settings window, navigate to `Project Structure` (or `File` -> `Project Structure` on Windows/Linux, or `IntelliJ IDEA` -> `Project Structure` on macOS).

2. Under `Modules`, select your project.

3. Go to the `Dependencies` tab.

4. Click the '+' icon to add a new dependency.

5. Choose to add a JAR file and select the Lombok JAR file (`lombok-1.18.28.jar`) you downloaded.

6. Click `OK` to confirm.

### iii. Enable Annotation Processing

1. In the settings window, go to `Build, Execution, Deployment` -> `Compiler` -> `Annotation Processors`.

2. Check "Enable annotation processing."

3. Click `OK` to apply the changes.

---

## Integration with Eclipse

### i. Run the Lombok Installer

1. Open a terminal and execute the following command, replacing `lombok.jar` with the actual name of the Lombok JAR file:

    ```bash
    java -jar lombok.jar
    ```

2. Point the installer to your Eclipse installation directory.

### ii. Restart Eclipse

Restart Eclipse to ensure Lombok is properly integrated.

### iii. Enable Annotation Processing (Per Project)

1. Right-click on your project in Eclipse.

2. Select `Properties`.

3. Navigate to `Java Compiler` -> `Annotation Processing`.

4. Check "Enable project-specific settings."

5. Check "Enable annotation processing."

6. Select "Obtain processors from project classpath."

7. Click `OK` to apply the changes.

