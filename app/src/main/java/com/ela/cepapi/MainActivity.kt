package com.ela.cepapi

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.ela.cepapi.api.Api
import com.ela.cepapi.databinding.ActivityMainBinding
import com.ela.cepapi.model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = Color.parseColor("#077A76")

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.btBuscaCEP.setOnClickListener(){
            val cep = binding.editCEP.text.toString()

            if(cep.isEmpty()){
                Toast.makeText(this, "Preencha o CEP", Toast.LENGTH_SHORT).show()
            }else{
                retrofit.setEndereco(cep).enqueue(object : Callback <Endereco>{
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        if (response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf = response.body()?.uf.toString()
                            setFormularios(logradouro, bairro, localidade, uf)
                        }else{
                            Toast.makeText(applicationContext, "CEP invalido!!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro inesperado!", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
    private fun setFormularios(logradouro: String, bairro: String, localidade: String, uf: String){
        binding.editLogradouro.setText(logradouro)
        binding.editBairro.setText(bairro)
        binding.editCidade.setText(localidade)
        binding.editEstado.setText(uf)
    }
}