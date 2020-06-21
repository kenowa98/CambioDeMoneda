package com.kenowa.cambiodemoneda

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar las características de vista del spinner 1
        val spinner1: Spinner = findViewById(R.id.sp_moneda_origen)
        ArrayAdapter.createFromResource(
            this,
            R.array.lista_monedas,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner1.adapter = adapter
        }
        // Cambiar la bandera de imageView según el spinner 1
        spinner1.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(pos)
                val bandera: ImageView = findViewById(R.id.iv_origen)
                showBandera(bandera, item as String)
                tv_cantidad_final.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Configurar las características de vista del spinner 2
        val spinner2: Spinner = findViewById(R.id.sp_moneda_destino)
        ArrayAdapter.createFromResource(
            this,
            R.array.lista_monedas,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            spinner2.adapter = adapter
        }
        // Cambiar la bandera de imageView según el spinner 2
        spinner2.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                pos: Int,
                id: Long
            ) {
                val item = parent.getItemAtPosition(pos)
                val bandera: ImageView = findViewById(R.id.iv_destino)
                showBandera(bandera, item as String)
                tv_cantidad_final.text = ""
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Convertir moneda una vez se oprima el botón de guardar
        btn_convertir.setOnClickListener {
            val cantidad = et_cantidad_inicial.text.toString()
            val pos1 = spinner1.selectedItemPosition
            val pos2 = spinner2.selectedItemPosition

            if (cantidad.isEmpty()) {
                tv_cantidad_final.text = "Ingrese la cantidad a convertir"
            } else {
                val final = convert(pos1, pos2, cantidad.toDouble()).toBigDecimal()
                    .setScale(2, RoundingMode.HALF_EVEN).toString()
                when (pos2) {
                    0 -> {
                        tv_cantidad_final.text = "$final COP"
                    } 1 -> {
                        tv_cantidad_final.text = "$final USD"
                    } 2 -> {
                        tv_cantidad_final.text = "$final EUR"
                    } else -> {
                        tv_cantidad_final.text = "$final JPY"
                    }
                }
            }
        }
    }

    private fun showBandera(iv: ImageView, item: String) {
        when (item) {
            "peso colombiano" -> {
                iv.setImageResource(R.drawable.colombia)
            } "euro" -> {
                iv.setImageResource(R.drawable.unioneuropea)
            } "yen" -> {
                iv.setImageResource(R.drawable.japon)
            } else -> {
                iv.setImageResource(R.drawable.estadosunidos)
            }
        }
    }

    private fun convert(moneda1: Int, moneda2: Int, cantidad: Double) : Double {
        return when (moneda1) {
            0 -> {
                when (moneda2) {
                    1 -> { cantidad*0.00027
                    } 2 -> { cantidad*0.00024
                    } 3 -> { cantidad*0.028
                    } else -> {cantidad}
                }
            } 1 -> {
                when (moneda2) {
                    0 -> { cantidad*3751.20
                    } 2 -> { cantidad*0.89
                    } 3 -> { cantidad*106.9
                    } else -> {cantidad}
                }
            } 2 -> {
                when (moneda2) {
                    0 -> { cantidad*4208.02
                    } 1 -> { cantidad*1.12
                    } 3 -> { cantidad*119.90
                    } else -> {cantidad}
                }
            } else -> {
                when (moneda2) {
                    0 -> { cantidad*35.09
                    } 1 -> { cantidad*0.0094
                    } 2 -> { cantidad*0.0083
                    } else -> {cantidad}
                }
            }
        }
    }

}