package estevao.market.service;

import estevao.market.exception.MarketException;
import estevao.market.model.ContaPagar;
import estevao.market.repository.ContaPagarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaPagarService {

    private final ContaPagarRepository repository;

    public List<ContaPagar> buscarPorDescricao(String descricao) {
        return repository.buscarPorDescricao(descricao.toUpperCase().trim());
    }

    public List<ContaPagar> buscarPorPessoa(Long pessoaId) {
        return repository.buscarPorPessoa(pessoaId);
    }

    public List<ContaPagar> buscarPorFornecedor(Long fornecedorId) {
        return repository.buscarPorFornecedor(fornecedorId);
    }

    public List<ContaPagar> buscarPorEmpresa(Long empresaId) {
        return repository.buscarPorEmpresa(empresaId);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public ContaPagar findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new MarketException("Conta a pagar não encontrada"));
    }

    public ContaPagar salvar(ContaPagar contaPagar) {
        return repository.save(contaPagar);
    }

}
