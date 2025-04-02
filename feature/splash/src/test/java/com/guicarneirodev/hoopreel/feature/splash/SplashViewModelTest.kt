package com.guicarneirodev.hoopreel.feature.splash

import app.cash.turbine.test
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.Player
import com.guicarneirodev.hoopreel.feature.highlights.domain.model.VideoHighlight
import com.guicarneirodev.hoopreel.feature.highlights.domain.repository.HighlightsRepository
import com.guicarneirodev.hoopreel.feature.splash.presentation.SplashViewModel
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.floats.shouldBeGreaterThan
import io.kotest.matchers.floats.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import io.kotest.runner.junit4.KotestTestRunner

@RunWith(KotestTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest : FunSpec() {

    init {
        // Configurar coroutines de teste
        val testDispatcher = StandardTestDispatcher()

        // Criamos um mock do repositório para isolar o teste
        val mockRepository = mockk<HighlightsRepository>(relaxed = true)

        // Declaramos nossa variável para o ViewModel que será testado
        lateinit var viewModel: SplashViewModel

        beforeTest {
            // Substituímos o dispatcher principal para os testes
            Dispatchers.setMain(testDispatcher)

            // Configuramos o comportamento padrão do repositório mock
            coEvery { mockRepository.getPlayers() } returns createTestPlayers()

            // Criamos uma nova instância do ViewModel antes de cada teste
            viewModel = SplashViewModel(mockRepository, testDispatcher)
        }

        afterTest {
            // Resetamos o dispatcher principal após o teste
            Dispatchers.resetMain()
        }

        // GRUPO 1: ESTADOS INICIAIS
        context("Estado inicial") {
            test("progress deve começar em 0") {
                viewModel.progress.value shouldBe 0f
            }

            test("isLoadingComplete deve começar como false") {
                viewModel.isLoadingComplete.value shouldBe false
            }
        }

        // GRUPO 2: CARREGAMENTO NORMAL
        context("Comportamento durante carregamento normal") {
            test("startLoading deve incrementar o progresso gradualmente") {
                // Ação: Iniciar o carregamento
                viewModel.startLoading()

                // Avançamos um pouco no tempo (500ms)
                testDispatcher.scheduler.advanceTimeBy(500)
                testDispatcher.scheduler.runCurrent()

                // Verificamos que o progresso aumentou, mas ainda não chegou a 100%
                viewModel.progress.value shouldBeGreaterThan 0f
                viewModel.progress.value shouldBeLessThan 1f

                // Verificamos que o carregamento ainda não está completo
                viewModel.isLoadingComplete.value shouldBe false
            }

            test("repositório deve ser chamado durante o carregamento") {
                // Ação: Iniciar o carregamento
                viewModel.startLoading()

                // Avançamos o tempo até que todas as coroutines completem
                testDispatcher.scheduler.advanceUntilIdle()
                testDispatcher.scheduler.runCurrent()

                // Verificamos que o repositório foi chamado para buscar os jogadores
                coVerify { mockRepository.getPlayers() }
            }

            test("o progresso deve eventualmente chegar a 100% e completar") {
                // Ação: Iniciar o carregamento
                viewModel.startLoading()

                // Avançamos o tempo até que todas as coroutines completem
                testDispatcher.scheduler.advanceUntilIdle()
                testDispatcher.scheduler.runCurrent()

                // Verificamos que o progresso chegou a 100%
                viewModel.progress.value shouldBe 1f

                // Verificamos que o carregamento foi marcado como completo
                viewModel.isLoadingComplete.value shouldBe true
            }

            test("a transição de estados deve ocorrer na ordem correta") {
                // Criar duas instâncias separadas do ViewModel para cada flow
                // Isso garante que os testes são independentes e não afetam um ao outro

                val viewModel1 = SplashViewModel(mockRepository, testDispatcher)
                val viewModel2 = SplashViewModel(mockRepository, testDispatcher)

                // Testamos cada flow separadamente com sua própria instância de ViewModel

                suspend fun verifyProgressFlow() {
                    viewModel1.progress.test {
                        // Verificamos que o valor inicial é 0
                        awaitItem() shouldBe 0f

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
                            currentValue shouldBeGreaterThan prevValue
                            prevValue = currentValue
                        }

                        // Avançamos para concluir o carregamento
                        testDispatcher.scheduler.advanceUntilIdle()
                        testDispatcher.scheduler.runCurrent()

                        // Verificamos que o valor final é 1 (100%)
                        expectMostRecentItem() shouldBe 1f

                        // Limpeza do teste
                        cancelAndConsumeRemainingEvents()
                    }
                }

                suspend fun verifyCompletionFlow() {
                    viewModel2.isLoadingComplete.test {
                        // O primeiro valor deve ser false
                        awaitItem() shouldBe false

                        // Iniciar carregamento
                        viewModel2.startLoading()

                        // Avançar até o final
                        testDispatcher.scheduler.advanceUntilIdle()
                        testDispatcher.scheduler.runCurrent()

                        // O último valor deve ser true
                        expectMostRecentItem() shouldBe true

                        // Limpeza
                        cancelAndConsumeRemainingEvents()
                    }
                }

                // Primeiro verificamos o fluxo de conclusão
                verifyCompletionFlow()

                // Depois verificamos o fluxo de progresso
                verifyProgressFlow()
            }
        }

        // GRUPO 3: CENÁRIOS DE ERRO
        context("Cenários de erro") {
            test("deve completar graciosamente mesmo quando o repositório falha") {
                // Configuramos o mock para lançar uma exceção
                coEvery { mockRepository.getPlayers() } throws Exception("Erro de rede simulado")

                // Ação: Iniciar o carregamento
                viewModel.startLoading()

                // Avançamos o tempo até que todas as coroutines completem
                testDispatcher.scheduler.advanceUntilIdle()
                testDispatcher.scheduler.runCurrent()

                // Verificamos que, mesmo com o erro, o carregamento é concluído
                viewModel.progress.value shouldBe 1f
                viewModel.isLoadingComplete.value shouldBe true
            }
        }

        // GRUPO 4: COMPORTAMENTO DE TEMPO
        context("Comportamento de tempo") {
            test("deve levar pelo menos 1 segundo para completar mesmo com dados rápidos") {
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
                (endTime - startTime) shouldBeGreaterThan 1000
            }
        }
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