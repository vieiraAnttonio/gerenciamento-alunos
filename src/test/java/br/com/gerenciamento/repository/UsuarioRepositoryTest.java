package br.com.gerenciamento.repository;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Before
    public void limparBanco() {
        usuarioRepository.deleteAll();
    }

    @Test
    public void salvarUsuarioEConsultarPorId() {
        Usuario usuario = criarUsuario(
                "antonio@email.com",
                "antonio",
                "123456"
        );

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        Optional<Usuario> resultado =
                usuarioRepository.findById(usuarioSalvo.getId());

        Assert.assertTrue(resultado.isPresent());
        Assert.assertEquals(
                "antonio@email.com",
                resultado.get().getEmail()
        );
        Assert.assertEquals(
                "antonio",
                resultado.get().getUser()
        );
    }

    @Test
    public void findByEmailDeveRetornarUsuarioCadastrado() {
        Usuario usuario = criarUsuario(
                "maria@email.com",
                "maria",
                "senha123"
        );

        usuarioRepository.save(usuario);

        Usuario resultado =
                usuarioRepository.findByEmail("maria@email.com");

        Assert.assertNotNull(resultado);
        Assert.assertEquals(
                "maria",
                resultado.getUser()
        );
    }

    @Test
    public void buscarLoginComCredenciaisValidasDeveRetornarUsuario() {
        Usuario usuario = criarUsuario(
                "carlos@email.com",
                "carlos",
                "123456"
        );

        usuarioRepository.save(usuario);

        Usuario resultado =
                usuarioRepository.buscarLogin(
                        "carlos",
                        "123456"
                );

        Assert.assertNotNull(resultado);
        Assert.assertEquals(
                "carlos@email.com",
                resultado.getEmail()
        );
    }

    @Test
    public void buscarLoginComCredenciaisInvalidasDeveRetornarNull() {
        Usuario usuario = criarUsuario(
                "ana@email.com",
                "ana",
                "senha123"
        );

        usuarioRepository.save(usuario);

        Usuario resultado =
                usuarioRepository.buscarLogin(
                        "ana",
                        "senha-incorreta"
                );

        Assert.assertNull(resultado);
    }

    private Usuario criarUsuario(
            String email,
            String user,
            String senha
    ) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setUser(user);
        usuario.setSenha(senha);
        return usuario;
    }
}