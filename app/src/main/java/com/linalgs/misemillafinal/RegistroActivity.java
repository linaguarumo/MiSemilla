package com.linalgs.misemillafinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class RegistroActivity extends AppCompatActivity {

    private String correo, contraseña, rcontraseña;
    EditText eCorreo, eCont, eRCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eCont = (EditText) findViewById(R.id.eCont);
        eRCont = (EditText) findViewById(R.id.eRCont);
    }
    public void registrese(View view) {
        correo = eCorreo.getText().toString();
        contraseña = eCont.getText().toString();
        rcontraseña = eRCont.getText().toString();

        if (correo.isEmpty()){
            Toast.makeText(getApplicationContext(),"Ingrese un un correo",Toast.LENGTH_SHORT).show();
            return;
        }if (contraseña.isEmpty()){
            Toast.makeText(getApplicationContext(),"Ingrese una contraseña",Toast.LENGTH_SHORT).show();
            return;
        }if (rcontraseña.isEmpty()){
            Toast.makeText(getApplicationContext(),"REepita la contraseña",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(contraseña.equals(rcontraseña))) {
            Toast.makeText(getApplicationContext(),"contraseñas no considen",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(correo.contains("@") && correo.contains("."))){
            Toast.makeText(getApplicationContext(),"Correo no valido",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("correo",correo);
        intent.putExtra("contraseña",contraseña);
        setResult(RESULT_OK, intent);
        finish();

    }
}

