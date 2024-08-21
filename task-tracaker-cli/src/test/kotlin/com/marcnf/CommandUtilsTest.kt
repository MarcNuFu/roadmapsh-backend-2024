package com.marcnf

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CommandUtilsTest {
    private val generator: EasyRandom = EasyRandom()
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        System.setOut(originalOut)
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN addTask should display usage message if args size < 2
        """
    )
    fun addTaskTest0() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.addTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.generateTask(
                        existingTasks = tasks,
                        description = any()
                    )
                }
            },
            { assert(outputStream.toString().contains("Usage: add <task_description>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN addTask should get description and save new task
        """
    )
    fun addTaskTest1() {
        // GIVEN
        val args = generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()
        val task = generator.nextObject(Task::class.java)

        val description = args.drop(1).joinToString(" ")
        mockkObject(TaskUtils)

        every {
            TaskUtils.generateTask(
                existingTasks = tasks,
                description = description
            )
        } returns task

        every {
            TaskUtils.saveTasks(
                tasks = tasks + task,
            )
        } just Runs

        // WHEN
        CommandUtils.addTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 1) {
                    TaskUtils.generateTask(
                        existingTasks = tasks,
                        description = description
                    )
                    TaskUtils.saveTasks(
                        tasks = tasks + task,
                    )
                }
            },
            { assert(outputStream.toString().contains("Task added successfully (ID: ${task.id})")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN updateTask should display usage message if args size < 3
        """
    )
    fun updateTaskTest0() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.updateTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getUpdatedTask(
                        task = any(),
                        description = any()
                    )
                }
            },
            { assert(outputStream.toString().contains("Usage: update <task_id> <new_description>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN updateTask should display error if id is not an Int
        """
    )
    fun updateTaskTest1() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(String::class.java),
            generator.nextObject(String::class.java),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.updateTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getUpdatedTask(
                        task = any(),
                        description = any()
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN updateTask should display error if id is not in task list
        """
    )
    fun updateTaskTest2() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(Int::class.java).toString(),
            generator.nextObject(String::class.java),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.updateTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getUpdatedTask(
                        task = any(),
                        description = any()
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN updateTask should update task if all is valid
        """
    )
    fun updateTaskTest3() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val args = arrayOf(
            generator.nextObject(String::class.java),
            tasks.first().id.toString(),
        ) + generator.objects(String::class.java, 5).toList().toTypedArray()

        val updatedTask = generator.nextObject(Task::class.java)

        val description = args.drop(2).joinToString(" ")

        mockkObject(TaskUtils)

        every {
            TaskUtils.getUpdatedTask(
                task = tasks.first(),
                description = description
            )
        } returns updatedTask

        every {
            TaskUtils.saveTasks(
                tasks = tasks - tasks.first() + updatedTask,
            )
        } just Runs

        // WHEN
        CommandUtils.updateTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 1) {
                    TaskUtils.getUpdatedTask(
                        task = tasks.first(),
                        description = description
                    )
                    TaskUtils.saveTasks(
                        tasks = tasks - tasks.first() + updatedTask,
                    )
                }
            },
            { assert(outputStream.toString().isEmpty()) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN deleteTask should display usage message if args size != 2
        """
    )
    fun deleteTaskTest0() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.deleteTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.saveTasks(
                        tasks = any(),
                    )
                }
            },
            { assert(outputStream.toString().contains("Usage: delete <task_id>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN deleteTask should delete task if all is valid
        """
    )
    fun deleteTaskTest1() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val args = arrayOf(
            generator.nextObject(String::class.java),
            tasks.first().id.toString(),
        )

        mockkObject(TaskUtils)

        every {
            TaskUtils.saveTasks(
                tasks = tasks - tasks.first(),
            )
        } just Runs

        // WHEN
        CommandUtils.deleteTask(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 1) {
                    TaskUtils.saveTasks(
                        tasks = tasks - tasks.first(),
                    )
                }
            },
            { assert(outputStream.toString().isEmpty()) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskInProgress should display usage message if args size != 2
        """
    )
    fun markTaskInProgressTest0() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskInProgress(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.IN_PROGRESS,
                    )
                }
            },
            { assert(outputStream.toString().contains("Usage: mark-in-progress <task_id>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskInProgress should display error if id is not an Int
        """
    )
    fun markTaskInProgressTest1() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(String::class.java),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskInProgress(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.IN_PROGRESS,
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskInProgress should display error if id is not in task list
        """
    )
    fun markTaskInProgressTest2() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(Int::class.java).toString(),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskInProgress(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.IN_PROGRESS,
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskInProgress should mark in progress task if all is valid
        """
    )
    fun markTaskInProgressTest3() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val args = arrayOf(
            generator.nextObject(String::class.java),
            tasks.first().id.toString(),
        )

        val markedTask = generator.nextObject(Task::class.java)

        mockkObject(TaskUtils)

        every {
            TaskUtils.getMarkedTask(
                task = tasks.first(),
                status = TaskStatus.IN_PROGRESS,
            )
        } returns markedTask

        every {
            TaskUtils.saveTasks(
                tasks = tasks - tasks.first() + markedTask,
            )
        } just Runs

        // WHEN
        CommandUtils.markTaskInProgress(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 1) {
                    TaskUtils.getMarkedTask(
                        task = tasks.first(),
                        status = TaskStatus.IN_PROGRESS,
                    )
                    TaskUtils.saveTasks(
                        tasks = tasks - tasks.first() + markedTask,
                    )
                }
            },
            { assert(outputStream.toString().isEmpty()) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskDone should display usage message if args size != 2
        """
    )
    fun markTaskDoneTest0() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskDone(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.DONE,
                    )
                }
            },
            { assert(outputStream.toString().contains("Usage: mark-done <task_id>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskDone should display error if id is not an Int
        """
    )
    fun markTaskDoneProgressTest1() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(String::class.java),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskDone(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.DONE,
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskDone should display error if id is not in task list
        """
    )
    fun markTaskDoneTest2() {
        // GIVEN
        val args = arrayOf(
            generator.nextObject(String::class.java),
            generator.nextObject(Int::class.java).toString(),
        )
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.markTaskDone(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 0) {
                    TaskUtils.getMarkedTask(
                        task = any(),
                        status = TaskStatus.DONE,
                    )
                }
            },
            { assert(outputStream.toString().contains("Task not found")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN markTaskDone should mark in progress task if all is valid
        """
    )
    fun markTaskDoneProgressTest3() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val args = arrayOf(
            generator.nextObject(String::class.java),
            tasks.first().id.toString(),
        )

        val markedTask = generator.nextObject(Task::class.java)

        mockkObject(TaskUtils)

        every {
            TaskUtils.getMarkedTask(
                task = tasks.first(),
                status = TaskStatus.DONE,
            )
        } returns markedTask

        every {
            TaskUtils.saveTasks(
                tasks = tasks - tasks.first() + markedTask,
            )
        } just Runs

        // WHEN
        CommandUtils.markTaskDone(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            {
                verify(exactly = 1) {
                    TaskUtils.getMarkedTask(
                        task = tasks.first(),
                        status = TaskStatus.DONE,
                    )
                    TaskUtils.saveTasks(
                        tasks = tasks - tasks.first() + markedTask,
                    )
                }
            },
            { assert(outputStream.toString().isEmpty()) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN listTasks should display usage message if args size > 2
        """
    )
    fun listTasksTest0() {
        // GIVEN
        val args = generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        // WHEN
        CommandUtils.listTasks(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assertAll(
            { assert(outputStream.toString().contains("Usage: list <status>")) },
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN listTasks should display all tasks when args has no filter
        """
    )
    fun listTasksTest1() {
        // GIVEN
        val args = generator.objects(String::class.java, 1).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        // WHEN
        CommandUtils.listTasks(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        tasks.forEach {
            assert(outputStream.toString().contains(it.toString()))
        }
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN listTasks should display filtered tasks when args has filter
        """
    )
    fun listTasksTest2() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val args = arrayOf(
            generator.nextObject(String::class.java),
            tasks.first().status
        )

        // WHEN
        CommandUtils.listTasks(
            args = args,
            existingTasks = tasks,
        )

        // THEN
        assert(outputStream.toString().contains(tasks.first().toString()))
        (tasks - tasks.first()).forEach {
            assert(!outputStream.toString().contains(it.toString()))
        }
    }
}
