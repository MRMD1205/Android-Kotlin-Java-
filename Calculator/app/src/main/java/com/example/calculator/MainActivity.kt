package com.example.calculator

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnClick.setOnClickListener {
            var bool = Validator.isValid(evText.text.toString())
            tvText.setText(bool.toString())
        }
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        tvOne.setOnClickListener { appendOnExpression("1", true) }
//        tvTwo.setOnClickListener { appendOnExpression("2", true) }
//        tvThree.setOnClickListener { appendOnExpression("3", true) }
//        tvFour.setOnClickListener { appendOnExpression("4", true) }
//        tvFive.setOnClickListener { appendOnExpression("5", true) }
//        tvSix.setOnClickListener { appendOnExpression("6", true) }
//        tvSeven.setOnClickListener { appendOnExpression("7", true) }
//        tvEight.setOnClickListener { appendOnExpression("8", true) }
//        tvNine.setOnClickListener { appendOnExpression("9", true) }
//        tvZero.setOnClickListener { appendOnExpression("0", true) }
//        tvDot.setOnClickListener { appendOnExpression(".", true) }
//
//        tvPlus.setOnClickListener { appendOnExpression("+", false) }
//        tvMinus.setOnClickListener { appendOnExpression("-", false) }
//        tvDivision.setOnClickListener { appendOnExpression("/", false) }
//        tvMultiplication.setOnClickListener { appendOnExpression("*", false) }
//        tvModulo.setOnClickListener { appendOnExpression("%", false) }
//
//        tvClear.setOnClickListener {
//            tvInput.text = ""
//            tvResult.text = ""
//        }
//
//        imgBack.setOnClickListener {
//            val string = tvInput.text.toString()
//            if (string.isNotEmpty())
//                tvInput.text = string.substring(0, string.length - 1)
//            tvResult.text = ""
//        }
//
//        tvAnswer.setOnClickListener {
//            try {
//                val expression = ExpressionBuilder(tvInput.text.toString()).build()
//                val result = expression.evaluate()
//                val longResult = result.toLong()
//                if (result == longResult.toDouble())
//                    tvResult.text = longResult.toString()
//                else
//                    tvResult.text = result.toString()
//
//            } catch (e: Exception) {
//                Log.d("Exception", "Message : " + e.message)
//            }
//        }
//    }
//
//    private fun appendOnExpression(string: String, canClear: Boolean) {
//        if (tvResult.text.isNotEmpty())
//            tvInput.text = ""
//
//        if (canClear) {
//            tvResult.text = ""
//            tvInput.append(string)
//        } else {
//            tvInput.append(tvResult.text)
//            tvInput.append(string)
//            tvResult.text = ""
//        }
//    }
}