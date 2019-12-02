package com.roger.ifoodaula.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.roger.ifoodaula.R;
import com.roger.ifoodaula.helper.ConfiguracaoFirebase;
import com.roger.ifoodaula.helper.UsuarioFirebase;

public class AutentificacaoActivity extends AppCompatActivity {

    private Button botaoAcessar;
    private EditText campoEmail, campoSenha;
    private Switch tipoAcesso, tipoUsuario;
    private LinearLayout linearTipoUsuario;

    private FirebaseAuth autentificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificacao);
        getSupportActionBar().hide(); // esconde a barra de suporte

        // chamo o método incializarComponentes()
        inicializarComponentes();
        autentificacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //verifica o usuário logado
        verificarUsuarioLogado();

        tipoAcesso.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ){ // empresa
                    linearTipoUsuario.setVisibility(View.VISIBLE);
                }else{ // cliente
                    linearTipoUsuario.setVisibility(View.GONE);
                }
            }
        });

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if ( !email.isEmpty() ){
                    if ( !senha.isEmpty() ){

                        // verifica o estado do switch
                        if ( tipoAcesso.isChecked() ){
                            // cadastro
                            autentificacao.createUserWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if ( task.isSuccessful() ){

                                        Toast.makeText(AutentificacaoActivity.this,
                                                "Cadastro efetuado com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        String tipoUsuario = getTipoUsuario();
                                        UsuarioFirebase.atualizarTipoUsuario(tipoUsuario);
                                        abrirTelaPrincipal();

                                    }else{

                                        String erroExcecao = "";
                                        try{

                                            throw task.getException();

                                        }catch(FirebaseAuthWeakPasswordException e){
                                            erroExcecao = "Digite uma senha mais forte!";
                                        }catch(FirebaseAuthInvalidCredentialsException e){
                                            erroExcecao = "Por favor digite um e-mail válido";
                                        }catch(FirebaseAuthUserCollisionException e){
                                            erroExcecao = "Esta conta já foi cadastrada";
                                        }catch (Exception e) {
                                            erroExcecao = "ao cadastrar usuário " + e.getMessage();
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(AutentificacaoActivity.this,
                                                "Erro: " + erroExcecao,
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else{
                            autentificacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if ( task.isSuccessful() ){

                                        Toast.makeText(AutentificacaoActivity.this,
                                                "Logado com sucesso!",
                                                Toast.LENGTH_SHORT).show();
                                        abrirTelaPrincipal();

                                    }else{
                                        Toast.makeText(AutentificacaoActivity.this,
                                                "Erro ao fazer login: " + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }


                    }else{
                        Toast.makeText(AutentificacaoActivity.this,
                                "Preencha a Senha",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AutentificacaoActivity.this,
                            "Preencha o E-mail",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void verificarUsuarioLogado(){
        FirebaseUser usuarioAtual = autentificacao.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

    private String getTipoUsuario(){
        return tipoUsuario.isChecked() ? "E"  : "U";
    }

    public void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    // método responsável por inicializar componentes
    private void inicializarComponentes(){
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        tipoAcesso = findViewById(R.id.switchAcesso);
        tipoUsuario = findViewById(R.id.switchTipoUsuario);
        linearTipoUsuario = findViewById(R.id.linearTipoUsuario);
    }
}
