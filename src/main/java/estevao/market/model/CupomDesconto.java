package estevao.market.model;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "cupom_desconto")
@SequenceGenerator(name = "seq_cupom_desconto", sequenceName = "seq_cupom_desconto", allocationSize = 1, initialValue = 1)
public class CupomDesconto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cupom_desconto")
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataValidadeCupom;

    @Column(nullable = false)
    private String codDesc;
    private BigDecimal valorRealDesc;
    private BigDecimal valorPorcentDesconto;

    @ManyToOne(targetEntity = Pessoa.class)
    @JoinColumn(name = "empresa_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT, name = "empresa_fk"))
    private Pessoa empresa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CupomDesconto)) return false;
        CupomDesconto that = (CupomDesconto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Pessoa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Pessoa empresa) {
        this.empresa = empresa;
    }

    public Date getDataValidadeCupom() {
        return dataValidadeCupom;
    }

    public void setDataValidadeCupom(Date dataValidadeCupom) {
        this.dataValidadeCupom = dataValidadeCupom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodDesc() {
        return codDesc;
    }

    public void setCodDesc(String codDesc) {
        this.codDesc = codDesc;
    }

    public BigDecimal getValorRealDesc() {
        return valorRealDesc;
    }

    public void setValorRealDesc(BigDecimal valorRealDesc) {
        this.valorRealDesc = valorRealDesc;
    }

    public BigDecimal getValorPorcentDesconto() {
        return valorPorcentDesconto;
    }

    public void setValorPorcentDesconto(BigDecimal valorPorcentDesconto) {
        this.valorPorcentDesconto = valorPorcentDesconto;
    }
}
