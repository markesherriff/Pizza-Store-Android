package com.example.pizza_order_responsive

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newIntent = getIntent()
        val currentUsername = newIntent.getStringExtra("state")
        val currentEmail = newIntent.getStringExtra("email")
        val currentAddress = newIntent.getStringExtra("address")

        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.getString("currentUser_address", "")
        //get the status of the current user...is it guest or somebody is already signed in
        if (currentUsername!="guest") {
            setTitle(currentUsername)
        }else{
            setTitle("guest")
        }

        mainView_orderPizzaBtn.setOnClickListener(){
            /*show/hide fields, labels or buttons, then make it possible to order a pizza*/
            op_deliveryAdressLabel.isVisible = true;
            op_editAddress.isVisible = true;
            op_isDeliverySwitch.isVisible = true;
            op_sizeLabel.isVisible = true;
            op_sizeLargeRad.isVisible = true;
            op_sizeMediumRad.isVisible = true;
            op_toppingsCheeseChk.isVisible = true;
            op_toppingsLabel.isVisible = true;
            op_toppingsMeatChk.isVisible = true;
            op_toppingsVeggiesChk.isVisible = true;
            op_totalPrice.isVisible = true;



        }
        //initialize all the buttons
        val sizeSmallRad = findViewById<RadioButton>(R.id.sizeSmallRad)
        val sizeMediumRad = findViewById<RadioButton>(R.id.op_sizeMediumRad)
        val sizeLargeRad = findViewById<RadioButton>(R.id.op_sizeLargeRad)
        val toppingsMeatChk = findViewById<CheckBox>(R.id.op_toppingsMeatChk)
        val toppingsVeggieChk = findViewById<CheckBox>(R.id.op_toppingsVeggiesChk)
        val toppingsCheeseChk = findViewById<CheckBox>(R.id.op_toppingsCheeseChk)
        val isDeliverySwitch = findViewById<Switch>(R.id.op_isDeliverySwitch)
        val deliveryAddressET = findViewById<EditText>(R.id.op_editAddress)
        val orderNowBtn = findViewById<Button>(R.id.mainView_orderPizzaBtn)

        //variables
        var totalPrice=0;
        var previousSize=0;
        deliveryAddressET.isVisible = false


        op_sizeGroup.setOnCheckedChangeListener{ group, checkedId ->
            //check radio group
            if (checkedId == R.id.sizeSmallRad){
                totalPrice-=previousSize
                totalPrice+=9
                previousSize=9
            }
            if (checkedId == R.id.op_sizeMediumRad){
                totalPrice-=previousSize
                totalPrice+=10
                previousSize=10
            }
            if (checkedId == R.id.op_sizeLargeRad){
                totalPrice-=previousSize
                totalPrice+=11
                previousSize=11
            }

            op_totalPrice.setText("Order Total: $" + totalPrice)
        }

        toppingsMeatChk.setOnCheckedChangeListener{ group, checkedId ->
            //check radio group
            if (toppingsMeatChk.isChecked){
                totalPrice+=2
            }else {
                totalPrice-=2
            }

            op_totalPrice.setText("Order Total: $" + totalPrice)
        }

        toppingsVeggieChk.setOnCheckedChangeListener { group, checkedId ->
            if (toppingsVeggieChk.isChecked){
                totalPrice+=2
            }else {
                totalPrice-=2
            }

            op_totalPrice.setText("Order Total: $" + totalPrice)
        }


        toppingsCheeseChk.setOnCheckedChangeListener { group, checkedId ->
            if (toppingsCheeseChk.isChecked){
                totalPrice+=2
            }else{
                totalPrice-=2
            }

            op_totalPrice.setText("Order Total: $" + totalPrice)
        }
        isDeliverySwitch.setOnCheckedChangeListener { compoundButton, dR ->
            if (isDeliverySwitch.isChecked){
                deliveryAddressET.isVisible = true
                isDeliverySwitch.text = "delivery"
                if (currentUsername!="guest")  {
                    op_editAddress.setText(currentAddress)
                }
            }else{
                deliveryAddressET.isVisible = false
                isDeliverySwitch.text = "pick up"
            }
        }

        orderNowBtn.setOnClickListener() {
            var size = "\nSize: "
            var toppings = "\nToppings: \n"
            var deliveryDetails = "\nDelivery address: "
            var errorDetails = "not ordered, error details: "

            //check radio group
            if (sizeSmallRad.isChecked) {
                size += "small"
            } else if (sizeMediumRad.isChecked) {
                size += "medium"
            } else if (sizeLargeRad.isChecked) {
                size += "large"
            }

            //check checkboxes
            if (toppingsMeatChk.isChecked) {
                toppings += " meat\n"
            }
            if (toppingsVeggieChk.isChecked) {
                toppings += " veggie\n"
            }
            if (toppingsCheeseChk.isChecked) {
                toppings += " cheese\n"
            }
            if (toppingsMeatChk.isChecked == false && toppingsVeggieChk.isChecked == false && toppingsCheeseChk.isChecked == false) {
                toppings = " no toppings\n"
            }


            if (isDeliverySwitch.isChecked) {
                if (deliveryAddressET.text.toString() == "" || deliveryAddressET.text.toString().isEmpty()) {
                    errorDetails += "\n no address entered"
                } else {
                    deliveryDetails += deliveryAddressET.text.toString()
                }
            } else {
                deliveryDetails = "Pick up at location"
            }

            if (errorDetails != "not ordered, error details: ") {
                var errorMessage = AlertDialog.Builder(this)
                errorMessage.setMessage(errorDetails)
                errorMessage.setCancelable(false)
                errorMessage.setPositiveButton(
                    "Okay",
                    DialogInterface.OnClickListener { errorM, id ->
                        errorM.cancel()
                    })

                //show message box
                var errorAlert = errorMessage.create()
                errorAlert.setTitle("Invalid Input")
                errorAlert.show()
            } else {
                var receiptMessage = AlertDialog.Builder(this)
                receiptMessage.setMessage("Are you sure that you'd like to order?")
                receiptMessage.setCancelable(false)
                receiptMessage.setPositiveButton(
                    "Order",
                    DialogInterface.OnClickListener { receipt, id ->
                        /* val topToast = Toast.makeText(this, "Ordered, here is your reciept: \n" + size + toppings + deliveryDetails + "\nTotal Price: $" + totalPrice, Toast.LENGTH_LONG)
                        topToast.setGravity(Gravity.TOP, 0, 0)
                        topToast.show()*/
                        /*sizeSmallRad.isChecked=false
                        sizeMediumRad.isChecked=false
                        sizeLargeRad.isChecked=false
                        toppingsMeatChk.isChecked=false
                        toppingsVeggieChk.isChecked=false
                        toppingsCheeseChk.isChecked=false
                        isDeliverySwitch.isChecked=false
                        deliveryAddressET.setText("")
                        totalPrice=0
                        totalPriceText.setText("Order Total: $0")*/

                        val orderInfoIntent = Intent(this, orderedScreen::class.java)
                        orderInfoIntent.putExtra("size", size)
                        orderInfoIntent.putExtra("toppings", toppings)
                        orderInfoIntent.putExtra("deliveryDetails", deliveryDetails)
                        orderInfoIntent.putExtra("totalPrice", totalPrice.toString())
                        orderInfoIntent.putExtra("state", currentUsername)
                        orderInfoIntent.putExtra("email", currentEmail)
                        orderInfoIntent.putExtra("address", currentAddress)
                        startActivity(orderInfoIntent)
                    })
                receiptMessage.setNegativeButton(
                    "Cancel",
                    DialogInterface.OnClickListener { receipt, id ->
                        receipt.cancel()

                    })

                //show message box
                var receiptAlert = receiptMessage.create()
                receiptAlert.setTitle("Confirm Order")
                receiptAlert.show()
            }
        }
        op_accountBtn.setOnClickListener(){
            val or_loginPageIntent = Intent(this, loginScreen::class.java)
            or_loginPageIntent.putExtra("state", "newLogin")
            startActivity(or_loginPageIntent)
        }
    }
}
