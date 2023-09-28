package estevao.market.service;

import estevao.market.model.Usuario;
import estevao.market.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaAutomatizadaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SendServiceEmail sendServiceEmail;

    // @Scheduled(initialDelay = 2000, fixedDelay = 86400000) // subiu projeto ele roda em 2seg - roda a cada 24h
    @Scheduled(cron = "0 0 11 * * * *", zone = "America/Sao_Paulo") // vai rodar tod dia as 11h da manha
    public void notificarUserTrocaSenha() {
        List<Usuario> usuariosSenhasVencidas = this.usuarioRepository.listaUsuariosMaior90Dias();

        for (Usuario usuario: usuariosSenhasVencidas) {
            StringBuilder messageHtml = new StringBuilder();

            messageHtml.append("<b>Sua senha expirou!! Atualizar por gentileza</b>");

            try {
                sendServiceEmail.enviarEmailHtml("Senha expirada", messageHtml.toString(), usuario.getPessoa().getEmail());
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
