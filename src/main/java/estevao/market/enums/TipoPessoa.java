package estevao.market.enums;

public enum TipoPessoa {

    JURIDICA("Jurídica"),
    JURIDICA_FORNECEDOR("Jurídica e Fornecedor"),
    FISICA("Física");

    private String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return "TipoPessoa{" +
                "descricao='" + descricao + '\'' +
                '}';
    }
}
