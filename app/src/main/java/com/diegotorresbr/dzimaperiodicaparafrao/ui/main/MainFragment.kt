package com.diegotorresbr.dzimaperiodicaparafrao.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.diegotorresbr.dzimaperiodicaparafrao.R
import com.diegotorresbr.dzimaperiodicaparafrao.databinding.FragmentoBinding
import kotlin.math.pow

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Navegação de Volta ao Menu
        binding.btnCalcBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Tratamento do Clique de Cálculo
        binding.botao.setOnClickListener(View.OnClickListener {
            val parte_inteira = binding.pInteira.text.toString().trim()
            val parte_nao_repete = binding.pNRepete.text.toString().trim()
            val dizima = binding.pRepete.text.toString().trim()

            // Validação lúdica para evitar travamentos
            if (dizima.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Ei! Você esqueceu de colocar a parte que repete (o período)! 🔁",
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            }

            try {
                var n1 = 0.0
                var n2 = 0.0
                var n3 = dizima.toDouble()
                var numero = 0.0

                if (parte_inteira.isNotEmpty()) {
                    n1 = parte_inteira.toDouble()
                }

                if (parte_nao_repete.isNotEmpty()) {
                    n2 = parte_nao_repete.toDouble() / denominador_nao_repete(parte_nao_repete)
                    val nn = parte_inteira.plus(parte_nao_repete).toDouble()
                    n3 = n3 / denominador(dizima).toInt()
                    numero = (nn + n3) / denominador_nao_repete(parte_nao_repete)

                    val denomindadoro = denominador(dizima).toInt()

                    Log.i("Numerador: ", (denomindadoro * nn + dizima.toInt()).toString())
                    val numerador = (denomindadoro * nn) + dizima.toInt()

                    val parte_fracao = denominador_nao_repete(parte_nao_repete).toInt()

                    val fracao = Rational(numerador.toInt(), denomindadoro * parte_fracao)

                    binding.textoNumerador.text = fracao.numerator.toString()
                    binding.textoDenominador.text = fracao.denominator.toString()

                } else {
                    numero = n1 + (n3 / denominador(dizima).toInt())

                    val denomindador_ = denominador(dizima).toInt()
                    val numerador = (denomindador_ * n1) + n3
                    val fracao = Rational(numerador.toInt(), denomindador_)
                    binding.textoNumerador.text = fracao.numerator.toString()
                    binding.textoDenominador.text = fracao.denominator.toString()
                }

                // Formatar a dízima de entrada no formato escolar (Ex: 0,333... ou 1,2555...)
                val decimalFormatado = if (parte_inteira.isEmpty() && parte_nao_repete.isEmpty()) {
                    "0,$dizima$dizima$dizima..."
                } else if (parte_inteira.isEmpty()) {
                    "0,$parte_nao_repete$dizima$dizima$dizima..."
                } else if (parte_nao_repete.isEmpty()) {
                    "$parte_inteira,$dizima$dizima$dizima..."
                } else {
                    "$parte_inteira,$parte_nao_repete$dizima$dizima$dizima..."
                }

                binding.entrada.text = decimalFormatado

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Ops! Verifique se os números digitados estão certinhos! 🧐",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("CALCULAR", "Erro de cálculo: ${e.message}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun denominador(numero: String): String {
        return "9".repeat(numero.length)
    }

    fun denominador_nao_repete(numero: String): Double {
        return 10.0.pow(numero.length)
    }
}
