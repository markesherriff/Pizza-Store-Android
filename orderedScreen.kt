package com.example.pizza_order_responsive

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ordered_screen.*

class orderedScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ordered_screen)

        val newIntent = getIntent()

        val size = newIntent.getStringExtra("size")
        val toppings = newIntent.getStringExtra("toppings")
        val deliveryDetails = newIntent.getStringExtra("deliveryDetails")
        val totalPrice = newIntent.getStringExtra("totalPrice")
        val currentUsername = newIntent.getStringExtra("state")
        val currentEmail = newIntent.getStringExtra("email")
        val currentAddress = newIntent.getStringExtra("address")

        if (currentUsername != "guest") {
            setTitle("Thank you " + currentUsername)
            or_orderReview.text = "Order Receipt\n--------------------" + size + "\n" + toppings + "\n" + deliveryDetails + "\n--------------------" + "\nTotal Price: $" + totalPrice + "\n\nreceipt sent to: " + currentEmail;
        } else {
            setTitle("Thank you")
            or_orderReview.text = "Order Receipt\n--------------------" + size + "\n" + toppings + "\n" + deliveryDetails + "\n--------------------" + "\nTotal Price: $" + totalPrice;
        }

        or_orderAgainBtn.setOnClickListener() {
            val orderAgainIntent = Intent(this, MainActivity::class.java)
            startActivity(orderAgainIntent)
        }

        or_accountBtn.setOnClickListener() {
            val or_loginPageIntent = Intent(this, loginScreen::class.java)
            or_loginPageIntent.putExtra("state", "newLogin")
            startActivity(or_loginPageIntent)
        }
    }
}
