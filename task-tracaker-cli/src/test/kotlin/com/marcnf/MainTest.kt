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

class MainTest {
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
        GIVEN empty args
        THEN main should display usage message
        """
    )
    fun mainTest0() {
        // GIVEN
        val args = emptyArray<String>()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { assert(outputStream.toString().contains("Usage: <command> [<args>]")) },
            { assert(outputStream.toString().contains("Commands: add, update, delete, mark-in-progress ...")) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN wrong args
        THEN main should display usage message
        """
    )
    fun mainTest1() {
        // GIVEN
        val args = arrayOf(generator.nextObject(String::class.java))
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { assert(outputStream.toString().contains("Usage: <command> [<args>]")) },
            { assert(outputStream.toString().contains("Commands: add, update, delete, mark-in-progress ...")) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to add task
        THEN main should load task and add one
        """
    )
    fun mainTest2() {
        // GIVEN
        val args = arrayOf(Commands.ADD.cliValue) + generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.addTask(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.addTask(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to update task
        THEN main should update task
        """
    )
    fun mainTest3() {
        // GIVEN
        val args = arrayOf(Commands.UPDATE.cliValue) + generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.updateTask(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.updateTask(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to delete task
        THEN main should delete task
        """
    )
    fun mainTest4() {
        // GIVEN
        val args = arrayOf(Commands.DELETE.cliValue) + generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.deleteTask(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.deleteTask(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to mark in progress task
        THEN main should mark task
        """
    )
    fun mainTest5() {
        // GIVEN
        val args = arrayOf(Commands.MARK_IN_PROGRESS.cliValue) +
            generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.markTaskInProgress(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.markTaskInProgress(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to mark done task
        THEN main should mark task
        """
    )
    fun mainTest6() {
        // GIVEN
        val args = arrayOf(Commands.MARK_DONE.cliValue) +
            generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.markTaskDone(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.markTaskDone(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }

    @Test
    @DisplayName(
        """
        GIVEN args to list tasks
        THEN main should list tasks
        """
    )
    fun mainTest8() {
        // GIVEN
        val args = arrayOf(Commands.LIST.cliValue) +
                generator.objects(String::class.java, 5).toList().toTypedArray()
        val tasks = generator.objects(Task::class.java, 5).toList()

        mockkObject(TaskUtils)
        mockkObject(CommandUtils)

        every {
            TaskUtils.loadTasks()
        } returns tasks

        every {
            CommandUtils.listTasks(
                args = args,
                existingTasks = tasks,
            )
        } just Runs

        // WHEN
        main(args)

        // THEN
        assertAll(
            { verify(exactly = 1) { TaskUtils.loadTasks() } },
            { verify(exactly = 1) { CommandUtils.listTasks(args = args, existingTasks = tasks) } },
            { assert(outputStream.toString().isEmpty()) }
        )
    }
}
