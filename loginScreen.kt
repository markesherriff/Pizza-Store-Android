package com.example.pizza_order_responsive

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_login_screen.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.lang.Exception
import kotlin.math.log

class loginScreen : AppCompatActivity() {
    var currentEmail = ""
    var currentAddress = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        setTitle("Login")

        val pref = getPreferences(Context.MODE_PRIVATE) //shared preferences initialization
        val editor = pref.edit() //editor for above shared preferences
        var currentUser_username = pref.getString("currentUser_username", "guest").toString() //get the status of the current user...is it guest or somebody is already signed in?
        currentEmail = pref.getString("currentUser_email", "").toString()
        currentAddress = pref.getString("currentUser_address", "").toString()
        lg_editAccountBtn.isVisible = false;
        orderPizzaWithBtn.isVisible = false;
        val newIntent = getIntent()

        if (currentUser_username!="guest") {
            if (newIntent.getStringExtra("state") != "newLogin") {
                mainPage(currentUser_username)
            }//already signed in, go to main page automatically as that user
            lg_editAccountBtn.isVisible = true;
            orderPizzaWithBtn.isVisible = true;
            orderPizzaWithBtn.setText("order pizza with " + currentUser_username)
            setTitle(currentUser_username)
        }

        login_submitBtn.setOnClickListener(){
            var loginVerify = changeCurrentUser(loginEditUsername.text.toString(), loginEditPassword.text.toString())
            if (loginVerify=="successful"){ //if login credentials are correct
                mainPage(loginEditUsername.text.toString())
            }else{ //if there is a login credential error
                Toast.makeText(this, loginVerify, Toast.LENGTH_SHORT).show()
            }
        }
        createAccountPageBtn.setOnClickListener(){
            //go to create account page
            val makeOrderPage = Intent(this, createAccount::class.java)
            makeOrderPage.putExtra("state", "newAccount")
            startActivity(makeOrderPage)
        }

        lg_editAccountBtn.setOnClickListener(){
            val makeOrderPage = Intent(this, createAccount::class.java)
            makeOrderPage.putExtra("state", currentUser_username)
            makeOrderPage.putExtra("email", currentEmail)
            makeOrderPage.putExtra("address", currentAddress)
            startActivity(makeOrderPage)
        }

        continueGuestBtn.setOnClickListener(){
            //go to main page as guest
            mainPage("guest")
        }

        logOutBtn.setOnClickListener(){
            setTitle("Log In")
            currentEmail = ""
            currentAddress = ""
            currentUser_username = ""
            editor.putString("currentUser_username", "guest")
            editor.commit();
            val pref = getPreferences(Context.MODE_PRIVATE)
            pref.edit().clear();
            lg_editAccountBtn.isVisible = false;
            orderPizzaWithBtn.isVisible = false;
        }

        orderPizzaWithBtn.setOnClickListener(){
            mainPage(currentUser_username)
        }

    }
    fun mainPage(user: String){
        val makeOrderPage = Intent(this, MainActivity::class.java)
        makeOrderPage.putExtra("state",user)
        makeOrderPage.putExtra("email", currentEmail)
        makeOrderPage.putExtra("address", currentAddress)
        startActivity(makeOrderPage)
    }
    fun changeCurrentUser(username: String, password: String): String{
        val pref = getPreferences(Context.MODE_PRIVATE) //shared preferences initialization
        val editor = pref.edit() //editor for above shared preferences

        /*verify account, return successful if username and password in the parameters are valid & correct*/
        if ((username==null || username=="")||(password==null || password=="")) return  "ERROR\nusername and password required"

        val filename = username.toLowerCase() + "_properties.txt" //file syntax for the users properties
        /*file reading initialization*/
        try {
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(filename)
            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = "empty file"

            /*read the users properties.txt*/
            text = bufferedReader.readLine() //read the first line (password)
            if (text == password) { //if password is correct
                editor.putString("currentUser_password", text)
                editor.putString("currentUser_username", username.toLowerCase())
                text = bufferedReader.readLine() //read the second line (email)
                editor.putString("currentUser_email", text)
                text = bufferedReader.readLine() //read the third line (address)
                editor.putString("currentUser_address", text)
                editor.commit() //save all the changes made to currentUser
                return  "successful" //end of verification, return true
            } else return  "ERROR\npassword incorrect" //if password incorrect return false
            /**/
        } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return  "ERROR\nusername incorrect"
        } catch (e: Exception) {
                e.printStackTrace()
                return  "ERROR\nunknown error"
        }
        /**/
    }
}
