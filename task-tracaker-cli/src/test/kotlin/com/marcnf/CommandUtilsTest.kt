package com.marcnf

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
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
        System.setOut(originalOut)
    }

    @Test
    @DisplayName(
        """
        GIVEN args and tasks
        THEN should display usage message if args size < 2
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
        THEN should get description and save new task
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
}
