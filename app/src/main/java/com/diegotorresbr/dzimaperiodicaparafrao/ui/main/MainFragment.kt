package com.diegotorresbr.dzimaperiodicaparafrao.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.util.Rational
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.diegotorresbr.dzimaperiodicaparafrao.R
import kotlinx.android.synthetic.main.fragmento.*
import kotlin.math.pow

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragmento, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        var parte_inteira= p_inteira.text
        var parte_nao_repete = p_n_repete.text
        var dizima = p_repete.text

        botao.setOnClickListener(View.OnClickListener {
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


                texto_numerador.text = fracao.numerator.toString()
                texto_denominador.text = fracao.denominator.toString()

            }else{

                numero = n1 +(n3/denominador(dizima.toString()).toInt())

                var denomindador_ = denominador(dizima.toString()).toInt()
                var numerador = (denomindador_*n1)+n3
                var fracao  = Rational(numerador.toInt(), denomindador_)
                texto_numerador.text = fracao.numerator.toString()
                texto_denominador.text = fracao.denominator.toString()
            }


            var aaa = Rational(2,4)



            entrada.text = numero.toString()


        })
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
