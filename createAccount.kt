package com.example.pizza_order_responsive

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_create_account.*
import java.io.*
import java.lang.Exception


class createAccount : AppCompatActivity() {
    var currentUsername = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val newIntent = getIntent()
        currentUsername = newIntent.getStringExtra("state")
        val currentEmail = newIntent.getStringExtra("email")
        val currentAddress = newIntent.getStringExtra("address")

        if (currentUsername!="newAccount"){
            setTitle("Edit " + currentUsername)
            create_EditUsername.isVisible = false;
            create_EditUsername.setText("editUser")
            create_editEmail.setText(currentEmail)
            create_editAddress.setText(currentAddress)
        }else{
            setTitle("Create New Account")
        }

        create_submitButton.setOnClickListener(){

            //get form text
            var formErrorText = ""
            var usernameText = ""
            if (currentUsername=="newAccount") {
                usernameText = create_EditUsername.text.toString()
            }else{
                usernameText = currentUsername
            }
            var passwordText = create_EditPassword.text.toString()
            var reTypePassword = create_editRetypePassword.text.toString()
            var emailText = create_editEmail.text.toString()
            var addressText = create_editAddress.text.toString()
            /*verify form*/
            var verifyNewForm = verifyNewAccount(usernameText, passwordText, reTypePassword, emailText)
            if (verifyNewForm==""){
                try {
                    var filename = ""
                    if (currentUsername!="newAccount") {
                        filename = currentUsername + "_properties.txt"
                    }else {
                        filename = usernameText + "_properties.txt"
                    }
                    val fileContents = passwordText + "\n" + emailText + "\n" + addressText
                    var fileOut: FileOutputStream
                    fileOut = openFileOutput(filename, Context.MODE_PRIVATE)
                    fileOut.write(fileContents.toByteArray())

                    val pref = getPreferences(Context.MODE_PRIVATE)
                    val editor = pref.edit()

                    editor.putString("currentUser_username", usernameText)
                    editor.putString("currentUser_password", passwordText)
                    editor.putString("currentUser_email", emailText)
                    editor.putString("currentUser_address", addressText)
                    editor.commit()

                    val pizzaOrder = Intent(this,loginScreen::class.java)
                    pizzaOrder.putExtra("state", "newLogin")
                    pizzaOrder.putExtra("email", currentEmail)
                    pizzaOrder.putExtra("address", currentAddress)
                    startActivity(pizzaOrder)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    errorToast("unknown")
                } catch (e: Exception) {
                    e.printStackTrace()
                    errorToast("unknown")
                }
                Toast.makeText(this, "successful", Toast.LENGTH_SHORT).show()
            }else{
                errorToast(verifyNewForm)
            }
            /**/


        }

        create_loginBtn.setOnClickListener(){
            val backToLogin = Intent(this,loginScreen::class.java)
            backToLogin.putExtra("state", "newLogin")
            startActivity(backToLogin)
        }
    }
    fun errorToast(errorText: String){
        Toast.makeText(this, "ERROR\n"+errorText, Toast.LENGTH_SHORT).show()
    }
    fun verifyNewAccount(username: String, password: String, reTypePassword: String, email: String): String{
        var formErr = ""
        if(username==null||username==""){
            formErr += "\nusername is required"
        }else if (ifUserExists(username)&&currentUsername=="newAccount"){
            formErr += "\nusername already exists"
            create_EditUsername.setHint("username already exists")
        }

        if(password==null||password==""){
            formErr += "\npassword is required"
        }else if(reTypePassword==null||reTypePassword=="") {
            formErr += "\nretype password is required"
        }else if (password!=reTypePassword){
            formErr += "\npassword does not match"
            create_editRetypePassword.setText("")
            create_editRetypePassword.setHint("password does not match")
        }

        if (email=="") {
            formErr += "\nemail is required"
        }

        return formErr
    }
    fun ifUserExists(username: String): Boolean{
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
            return true //end of verification, return true
            /**/
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return  false
        } catch (e: Exception) {
            e.printStackTrace()
            return  false
        }
        /**/
    }

}