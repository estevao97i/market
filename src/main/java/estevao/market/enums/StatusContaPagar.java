package estevao.market.enums;

public enum StatusContaPagar {

    COBRANCA("Pagar"),
    VENCIDA("Vencida"),
    ABERTA("Aberta"),
    QUITADA("Quitada"),
    RENEGOCIADA("Renegociada");

    private String descricao;

    StatusContaPagar(String descricao) {
        this.descricao = descricao;
    }
}
