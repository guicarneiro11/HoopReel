package com.guicarneirodev.hoopreel.feature.splash

import app.cash.turbine.test
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import com.guicarneirodev.hoopreel.feature.splash.presentation.SplashViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class JUnitSplashViewModelTest {

    // Configurar coroutines de teste
    private val testDispatcher = StandardTestDispatcher()

    // Criamos um mock do repositório para isolar o teste
    private val mockRepository = mockk<HighlightsRepository>(relaxed = true)

    // Declaramos nossa variável para o ViewModel que será testado
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        // Substituímos o dispatcher principal para os testes
        Dispatchers.setMain(testDispatcher)

        // Configuramos o comportamento padrão do repositório mock
        coEvery { mockRepository.getPlayers() } returns createTestPlayers()

        // Criamos uma nova instância do ViewModel antes de cada teste
        viewModel = SplashViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        // Resetamos o dispatcher principal após o teste
        Dispatchers.resetMain()
    }

    // ===== GRUPO 1: ESTADOS INICIAIS =====

    @Test
    fun `progress deve começar em 0`() {
        assertEquals(0f, viewModel.progress.value, 0.001f)
    }

    @Test
    fun `isLoadingComplete deve começar como false`() {
        assertFalse(viewModel.isLoadingComplete.value)
    }

    // ===== GRUPO 2: CARREGAMENTO NORMAL =====

    @Test
    fun `startLoading deve incrementar o progresso gradualmente`() = runTest {
        // Ação: Iniciar o carregamento
        viewModel.startLoading()

        // Avançamos um pouco no tempo (500ms)
        testDispatcher.scheduler.advanceTimeBy(500)
        testDispatcher.scheduler.runCurrent()

        // Verificamos que o progresso aumentou, mas ainda não chegou a 100%
        assertTrue(viewModel.progress.value > 0f)
        assertTrue(viewModel.progress.value < 1f)

        // Verificamos que o carregamento ainda não está completo
        assertFalse(viewModel.isLoadingComplete.value)
    }

    @Test
    fun `repositório deve ser chamado durante o carregamento`() = runTest {
        // Ação: Iniciar o carregamento
        viewModel.startLoading()

        // Avançamos o tempo até que todas as coroutines completem
        testDispatcher.scheduler.advanceUntilIdle()
        testDispatcher.scheduler.runCurrent()

        // Verificamos que o repositório foi chamado para buscar os jogadores
        coVerify { mockRepository.getPlayers() }
    }

    @Test
    fun `o progresso deve eventualmente chegar a 100 pct e completar`() = runTest {
        // Ação: Iniciar o carregamento
        viewModel.startLoading()

        // Avançamos o tempo até que todas as coroutines completem
        testDispatcher.scheduler.advanceUntilIdle()
        testDispatcher.scheduler.runCurrent()

        // Verificamos que o progresso chegou a 100%
        assertEquals(1f, viewModel.progress.value, 0.001f)

        // Verificamos que o carregamento foi marcado como completo
        assertTrue(viewModel.isLoadingComplete.value)
    }

    @Test
    fun `a transição de estados de progresso deve ocorrer na ordem correta`() = runBlocking {
        val viewModel1 = SplashViewModel(mockRepository, testDispatcher)

        viewModel1.progress.test {
            // Verificamos que o valor inicial é 0
            assertEquals(0f, awaitItem(), 0.001f)

            // Ação: Iniciar o carregamento
            viewModel1.startLoading()

            // Verificamos que o progresso aumenta gradualmente
            var prevValue = 0f
            repeat(5) {
                // Avançamos um pouco no tempo
                testDispatcher.scheduler.advanceTimeBy(200)
                testDispatcher.scheduler.runCurrent()

                // Verificamos que o valor aumentou
                val currentValue = awaitItem()
                assertTrue(currentValue > prevValue)
                prevValue = currentValue
            }

            // Avançamos para concluir o carregamento
            testDispatcher.scheduler.advanceUntilIdle()
            testDispatcher.scheduler.runCurrent()

            // Verificamos que o valor final é 1 (100%)
            assertEquals(1f, expectMostRecentItem(), 0.001f)

            // Limpeza do teste
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `a transição de estados de conclusão deve ocorrer na ordem correta`() = runBlocking {
        val viewModel2 = SplashViewModel(mockRepository, testDispatcher)

        viewModel2.isLoadingComplete.test {
            // O primeiro valor deve ser false
            assertFalse(awaitItem())

            // Iniciar carregamento
            viewModel2.startLoading()

            // Avançar até o final
            testDispatcher.scheduler.advanceUntilIdle()
            testDispatcher.scheduler.runCurrent()

            // O último valor deve ser true
            assertTrue(expectMostRecentItem())

            // Limpeza
            cancelAndConsumeRemainingEvents()
        }
    }

    // ===== GRUPO 3: CENÁRIOS DE ERRO =====

    @Test
    fun `deve completar graciosamente mesmo quando o repositório falha`() = runTest {
        // Configuramos o mock para lançar uma exceção
        coEvery { mockRepository.getPlayers() } throws Exception("Erro de rede simulado")

        // Ação: Iniciar o carregamento
        viewModel.startLoading()

        // Avançamos o tempo até que todas as coroutines completem
        testDispatcher.scheduler.advanceUntilIdle()
        testDispatcher.scheduler.runCurrent()

        // Verificamos que, mesmo com o erro, o carregamento é concluído
        assertEquals(1f, viewModel.progress.value, 0.001f)
        assertTrue(viewModel.isLoadingComplete.value)
    }

    // ===== GRUPO 4: COMPORTAMENTO DE TEMPO =====

    @Test
    fun `deve levar pelo menos 1 segundo para completar mesmo com dados rápidos`() = runTest {
        // Configuramos o mock para retornar rapidamente (sem delay interno)
        coEvery { mockRepository.getPlayers() } returns createTestPlayers()

        // Registramos o tempo inicial
        val startTime = testDispatcher.scheduler.currentTime

        // Ação: Iniciar o carregamento
        viewModel.startLoading()

        // Avançamos o tempo até que todas as coroutines completem
        testDispatcher.scheduler.advanceUntilIdle()
        testDispatcher.scheduler.runCurrent()

        // Registramos o tempo final
        val endTime = testDispatcher.scheduler.currentTime

        // Verificamos que levou pelo menos 1000ms (1 segundo)
        assertTrue(endTime - startTime > 1000)
    }

    // Funções auxiliares
    private fun createTestPlayers() = listOf(
        Player(
            id = "test-player",
            name = "Test Player",
            searchTerms = "test highlights",
            imageUrl = "https://example.com/player.jpg",
            highlights = listOf(
                VideoHighlight(
                    id = "test-video",
                    title = "Test Highlight",
                    thumbnailUrl = "https://example.com/thumb.jpg",
                    views = "1000",
                    publishedAt = "2025-01-01"
                )
            )
        )
    )
}