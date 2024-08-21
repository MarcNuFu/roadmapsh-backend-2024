# Task Tracker CLI

This project is a simple CLI application to track and manage tasks.

This is a solution for [Task Tracker](https://roadmap.sh/projects/task-tracker) challenge.

## Run locally

1. **Clone the repository:**
```bash
git clone https://github.com/MarcNuFu/roadmapsh-backend-2024.git
cd roadmapsh-backend-2024/1-task-tracaker-cli
```

2. **Compile the source code:**
```bash
mvn clean package
```

3. **Run:**
```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar <command> [arguments]
```

## Usage

### Add Task

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar add "New Task"
```

### Update Task

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar update 1 "New Task 2"
```

### Delete Task

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar delete 1
```

### Mark Task as 'in progress'

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar mark-in-progress 1
```

### Mark Task as 'done'

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar mark-done 1
```

### List all Task

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar list
```

### List all Task that are done / not done / in progress

```bash
java -jar target/task-tracaker-cli-1.0-SNAPSHOT-jar-with-dependencies.jar list [done|todo|in-progress]
```