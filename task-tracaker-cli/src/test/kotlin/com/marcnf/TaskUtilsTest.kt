package com.marcnf

import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.time.LocalDateTime

class TaskUtilsTest {
    private val generator: EasyRandom = EasyRandom()
    private val originalOut = System.out
    private val outputStream = ByteArrayOutputStream()

    @BeforeEach
    fun setUp() {
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
        System.setOut(originalOut)
    }

    @Test
    @DisplayName(
        """
        GIVEN empty tasks and description
        THEN generateTask should return new Task with id = 1
        """
    )
    fun generateTaskTest0() {
        // GIVEN
        val tasks = emptyList<Task>()
        val description = generator.nextObject(String::class.java)
        val now = LocalDateTime.now()

        val expectedResult = Task(
            id = 1,
            description = description,
            TaskStatus.TODO.value,
            createdAt = now.toString(),
            updatedAt = now.toString(),
        )

        mockkStatic(LocalDateTime::class)
        every {
            LocalDateTime.now()
        } returns now

        // WHEN
        val result = TaskUtils.generateTask(
            existingTasks = tasks,
            description = description,
        )

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName(
        """
        GIVEN empty tasks and description
        THEN generateTask should return new Task with id = greatest + 1
        """
    )
    fun generateTaskTest1() {
        // GIVEN
        val tasks = generator.objects(Task::class.java, 5).toList()
        val description = generator.nextObject(String::class.java)
        val now = LocalDateTime.now()

        val expectedResult = Task(
            id = tasks.maxOf(Task::id).plus(1),
            description = description,
            TaskStatus.TODO.value,
            createdAt = now.toString(),
            updatedAt = now.toString(),
        )

        mockkStatic(LocalDateTime::class)
        every {
            LocalDateTime.now()
        } returns now

        // WHEN
        val result = TaskUtils.generateTask(
            existingTasks = tasks,
            description = description,
        )

        // THEN
        assertEquals(expectedResult, result)
    }

    @Test
    @DisplayName(
        """
        GIVEN task and status
        THEN getUpdateTask should updated task
        """
    )
    fun getUpdateTaskTest1() {
        // GIVEN
        val task = generator.nextObject(Task::class.java)
        val newStatus = generator.nextObject(TaskStatus::class.java)
        val now = LocalDateTime.now()

        val expectedResult = task.copy(
            status = newStatus.value,
            updatedAt = now.toString(),
        )

        mockkStatic(LocalDateTime::class)
        every {
            LocalDateTime.now()
        } returns now

        // WHEN
        val result = TaskUtils.getUpdateTask(
            task = task,
            status = newStatus,
        )

        // THEN
        assertEquals(expectedResult, result)
    }
}
