package estevao.market.model;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "item_venda_loja")
@SequenceGenerator(name = "seq_item_venda_loja", sequenceName = "seq_item_venda_loja", allocationSize = 1)
public class ItemVendaLoja implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_venda_loja")
    private Long id;

    @ManyToOne(targetEntity = Produto.class)
    @JoinColumn(name = "produto_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "produto_fk"))
    private Produto produto;

    @ManyToOne(targetEntity = VendaCompraLoja.class)
    @JoinColumn(name = "venda_compra_loja_id", nullable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "venda_compra_loja_fk"))
    private VendaCompraLoja vendaCompraLoja;

    @Column(nullable = false)
    private Double quantidade;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private Pessoa empresa;

    public Pessoa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Pessoa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemVendaLoja)) return false;
        ItemVendaLoja that = (ItemVendaLoja) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public VendaCompraLoja getVendaCompraLoja() {
        return vendaCompraLoja;
    }

    public void setVendaCompraLoja(VendaCompraLoja vendaCompraLoja) {
        this.vendaCompraLoja = vendaCompraLoja;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
