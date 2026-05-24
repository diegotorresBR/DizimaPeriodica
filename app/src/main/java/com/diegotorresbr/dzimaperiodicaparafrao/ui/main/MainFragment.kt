package com.diegotorresbr.dzimaperiodicaparafrao.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.diegotorresbr.dzimaperiodicaparafrao.R
import com.diegotorresbr.dzimaperiodicaparafrao.databinding.FragmentoBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import kotlin.math.pow
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView


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
        // TODO: Use the ViewModel
        MobileAds.initialize(requireContext()){}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.adView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                Log.i("ADS", "Carregou")
                super.onAdLoaded()

            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.e("ADS", "Falhou"+p0.toString())
                super.onAdFailedToLoad(p0)
            }
        }

        var parte_inteira= binding.pInteira.text
        var parte_nao_repete = binding.pNRepete.text
        var dizima = binding.pRepete.text

        binding.botao.setOnClickListener(View.OnClickListener {
            var n1 = 0.0
            var n2 = 0.0
            var n3 = dizima.toString().toDouble()
            var numero = 0.0
            if(parte_inteira.isNotBlank()){
                n1 = parte_inteira.toString().toDouble()
            }
            if(parte_nao_repete.isNotBlank()){
                n2 = parte_nao_repete.toString().toDouble()/denominador_nao_repete(parte_nao_repete.toString())
                var nn = parte_inteira.toString().plus(parte_nao_repete.toString()).toDouble()
                n3 = n3/denominador(dizima.toString()).toInt()
                numero = (nn + n3)/denominador_nao_repete(parte_nao_repete.toString())

                var denomindadoro = denominador(dizima.toString()).toInt()

                Log.i("Numerador: ", (denomindadoro*nn+dizima.toString().toInt()).toString())
                var numerador = (denomindadoro*nn)+dizima.toString().toInt()

                var parte_fracao = denominador_nao_repete(parte_nao_repete.toString()).toInt()

                var fracao  = Rational(numerador.toInt(), denomindadoro*parte_fracao)

                binding.textoNumerador.text = fracao.numerator.toString()
                binding.textoDenominador.text = fracao.denominator.toString()

            }else{

                numero = n1 +(n3/denominador(dizima.toString()).toInt())

                var denomindador_ = denominador(dizima.toString()).toInt()
                var numerador = (denomindador_*n1)+n3
                var fracao  = Rational(numerador.toInt(), denomindador_)
                binding.textoNumerador.text = fracao.numerator.toString()
                binding.textoDenominador.text = fracao.denominator.toString()
            }
            binding.entrada.text = numero.toString()

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun denominador(numero:String): String {
        var n = "9".repeat(numero.length)
        return n
    }
    fun denominador_nao_repete(numero: String):Double{
        var n = 10.0.pow(numero.length)
        return n
    }
}
