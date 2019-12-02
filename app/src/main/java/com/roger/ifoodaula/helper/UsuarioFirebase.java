package com.roger.ifoodaula.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.roger.ifoodaula.helper.ConfiguracaoFirebase.getFirebaseAutenticacao;

public class UsuarioFirebase {
    public static String getIdUsuario(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return autenticacao.getCurrentUser().getUid();

    }


    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarTipoUsuario(String tipo){
        try{

            

        }catch(Exception e ){
            e.printStackTrace();
            return false;
        }
    }

}
