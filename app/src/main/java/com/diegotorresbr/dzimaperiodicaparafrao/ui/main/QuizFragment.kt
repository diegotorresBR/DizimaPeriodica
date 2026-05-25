package com.diegotorresbr.dzimaperiodicaparafrao.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.diegotorresbr.dzimaperiodicaparafrao.MainActivity
import com.diegotorresbr.dzimaperiodicaparafrao.R
import com.diegotorresbr.dzimaperiodicaparafrao.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    // Modelo de Questão
    private data class Question(
        val text: String,
        val options: List<String>,
        val correctIndex: Int
    )

    // Lista de Questões Divertidas e Educativas
    private val questions = listOf(
        Question(
            "Na dízima periódica 0,5555..., qual é a parte que se repete (o período)?",
            listOf("A) O número 5 🎈", "B) O número 55", "C) O número 0", "D) Nenhum número"),
            0
        ),
        Question(
            "Como se chama o número que fica se repetindo infinitamente na dízima?",
            listOf("A) Intruso", "B) Período 🔁", "C) Fração", "D) Inteiro"),
            1
        ),
        Question(
            "Qual das seguintes dízimas é uma Dízima Simples (o repeteco começa logo após a vírgula)? 🎈",
            listOf("A) 0,1222...", "B) 0,7777... ⭐", "C) 1,3444...", "D) 0,0555..."),
            1
        ),
        Question(
            "Qual fração mágica representa a dízima 0,333...?",
            listOf("A) 3/10", "B) 1/9", "C) 1/3 🍕", "D) 3/3"),
            2
        ),
        Question(
            "Na dízima composta 0,1666..., quem é o intruso (o número antes do repeteco)?",
            listOf("A) O número 6", "B) O número 1 🚂", "C) O número 0", "D) O número 16"),
            1
        )
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnQuizBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnQuizMenu.setOnClickListener {
            (activity as? MainActivity)?.navigateTo(MainMenuFragment.newInstance(), false)
        }

        binding.btnQuizReplay.setOnClickListener {
            restartQuiz()
        }

        // Iniciar Jogo
        loadQuestion()
    }

    private fun loadQuestion() {
        if (currentQuestionIndex >= questions.size) {
            showResults()
            return
        }

        // Resetar botões para o estado padrão
        resetOptionButtons()

        val currentQuestion = questions[currentQuestionIndex]
        
        // Atualizar textos e progresso
        binding.txtQuestionCounter.text = "Questão ${currentQuestionIndex + 1} de ${questions.size} 🎯"
        binding.txtQuizScore.text = "Pontos: $score"
        
        val progressPercent = ((currentQuestionIndex + 1) * 100) / questions.size
        binding.progressQuiz.progress = progressPercent

        binding.txtQuestionText.text = currentQuestion.text

        // Setar opções nos botões
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        for (i in buttons.indices) {
            buttons[i].text = currentQuestion.options[i]
            buttons[i].setOnClickListener {
                onOptionSelected(i)
            }
        }
    }

    private fun onOptionSelected(selectedIndex: Int) {
        // Desabilitar cliques nos botões para evitar múltipla seleção
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { it.isEnabled = false }

        val currentQuestion = questions[currentQuestionIndex]
        val correctIndex = currentQuestion.correctIndex

        if (selectedIndex == correctIndex) {
            // Acertou!
            score++
            binding.txtQuizScore.text = "Pontos: $score"
            setButtonCorrect(buttons[selectedIndex])
        } else {
            // Errou!
            setButtonIncorrect(buttons[selectedIndex])
            setButtonCorrect(buttons[correctIndex])
        }

        // Avançar com atraso de 1.5s para a criança ver o feedback visual
        handler.postDelayed({
            currentQuestionIndex++
            loadQuestion()
        }, 1500)
    }

    private fun setButtonCorrect(button: Button) {
        button.setBackgroundResource(R.drawable.input_correct)
        button.setTextColor(resources.getColor(R.color.color_card_learn_text, null))
    }

    private fun setButtonIncorrect(button: Button) {
        button.setBackgroundResource(R.drawable.input_incorrect)
        button.setTextColor(resources.getColor(R.color.color_incorrect, null))
    }

    private fun resetOptionButtons() {
        val buttons = listOf(binding.btnOption1, binding.btnOption2, binding.btnOption3, binding.btnOption4)
        buttons.forEach { button ->
            button.isEnabled = true
            button.setBackgroundResource(R.drawable.input_background)
            button.setTextColor(resources.getColor(R.color.color_dark_text, null))
        }
    }

    private fun showResults() {
        binding.layoutQuizPlay.visibility = View.GONE
        binding.layoutQuizResult.visibility = View.VISIBLE

        binding.txtQuizResultScore.text = "Você acertou $score de ${questions.size}!"

        // Lógica lúdica de estrelas
        when (score) {
            5 -> {
                binding.txtQuizStars.text = "⭐⭐⭐"
                binding.txtQuizCongrats.text = "UAU! Você é um verdadeiro Super Herói da Matemática! 👑🚀"
            }
            4 -> {
                binding.txtQuizStars.text = "⭐⭐⭐"
                binding.txtQuizCongrats.text = "Incrível! Quase gabaritou, você foi excelente! 🚀🌟"
            }
            3 -> {
                binding.txtQuizStars.text = "⭐⭐"
                binding.txtQuizCongrats.text = "Muito bem! Você está no caminho certo! 👍🎈"
            }
            2, 1 -> {
                binding.txtQuizStars.text = "⭐"
                binding.txtQuizCongrats.text = "Continue treinando! Com mais esforço você vai longe! 💖"
            }
            else -> {
                binding.txtQuizStars.text = "👀"
                binding.txtQuizCongrats.text = "Vamos reler a teoria? Você é muito inteligente e vai conseguir! 💪🌟"
            }
        }
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        binding.layoutQuizResult.visibility = View.GONE
        binding.layoutQuizPlay.visibility = View.VISIBLE
        loadQuestion()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        fun newInstance() = QuizFragment()
    }
}
