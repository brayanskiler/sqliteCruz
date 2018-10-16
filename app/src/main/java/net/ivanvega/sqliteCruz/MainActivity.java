package net.ivanvega.sqliteCruz;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import net.ivanvega.sqliteCruz.db.DAOUsuarios;
import net.ivanvega.sqliteCruz.db.MiAdaptadorUsuariosConexion;
import net.ivanvega.sqliteCruz.db.Usuario;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText txtN,txtE,txtT,txtF,txtRS;
    ListView lsv ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtN = findViewById(R.id.txtNombre);
        txtT = findViewById(R.id.txtTelefono);
        txtE = findViewById(R.id.txtEmail);
        txtRS = findViewById(R.id.txtRedSocial);
        txtF = findViewById(R.id.txtFechaNacimiento);
        lsv = findViewById(R.id.lsv);
        btnCargar();
    }

    public void btnI_click(View v){
        DAOUsuarios ado = new DAOUsuarios(getApplicationContext());
        if( !txtN.getText().toString().isEmpty() && !txtT.getText().toString().isEmpty() &&
                !txtE.getText().toString().isEmpty() && !txtF.getText().toString().isEmpty()){

            long result = ado.add(
                    new Usuario(0, txtN.getText().toString(), txtT.getText().toString(), txtE.getText().toString(),txtRS.getText().toString() , txtF.getText().toString())
            );

            if (result>0){
                Toast.makeText(this, "Adición exitosa",
                        Toast.LENGTH_LONG).show();
                btnCargar();
                txtN.setText("");
                txtT.setText("");
                txtE.setText("");
                txtRS.setText("");
                txtF.setText("");
            }
        }else{
            Toast.makeText(this, "Los datos no deben de estar vacios",Toast.LENGTH_LONG).show();
        }


    }

    public void btnCargar(){

            DAOUsuarios dao = new DAOUsuarios(this);
            List<Usuario> lst =  dao.getAll();
            for (Usuario item: lst
                    ) {
                Log.d("USUARIO: " ,  String.valueOf( item.getId()));
                Log.d("USUARIO: " , item.getNombre());
            }
            Cursor c =  dao.getAllC();
      if(c.moveToFirst()){
          SimpleCursorAdapter adp = new SimpleCursorAdapter(
                  this, android.R.layout.simple_list_item_2 ,
                  c , MiAdaptadorUsuariosConexion.COLUMNS_USUARIOS,
                  new int[]{android.R.id.text1, android.R.id.text2},
                  SimpleCursorAdapter.NO_SELECTION

          );
          lsv.setAdapter(adp);
      }else{
          lsv.setAdapter(null);
       Toast.makeText(this,"Ella te dejo vacio bro",Toast.LENGTH_LONG).show();
      }


    }

    public void btnBuscar_click(View view) {
        Cursor C;
        DAOUsuarios ado = new DAOUsuarios(getApplicationContext());
        String Nombre =txtN.getText().toString();
        if(!Nombre.isEmpty()){
            C=ado.getBusqueda(new Usuario(Nombre));
            if(C.moveToFirst()){
             txtN.setText(C.getString(1));
                txtT.setText(C.getString(2));
               txtE.setText(C.getString(3));
               txtRS.setText(C.getString(4));
               txtF.setText(C.getString(5));
            }else{
                Toast.makeText(this, "Error al buscar",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "El dato nombre no debe de estar vacio para buscar",Toast.LENGTH_LONG).show();
        }
    }

    public void btnEliminar_click(View view) {
        DAOUsuarios ado = new DAOUsuarios(getApplicationContext());
        String Nombre =txtN.getText().toString();
        if(!Nombre.isEmpty()){
              if (ado.getBorar(new Usuario(Nombre))==1){
                  txtN.setText("");
                  Toast.makeText(this, "Exito al borar",Toast.LENGTH_LONG).show();
                  btnCargar();
              }else{
                  Toast.makeText(this, "Error al borar",Toast.LENGTH_LONG).show();
              }
        }else{
            Toast.makeText(this, "El dato nombre no debe de estar vacio para eliminar",Toast.LENGTH_LONG).show();
        }
    }
}
