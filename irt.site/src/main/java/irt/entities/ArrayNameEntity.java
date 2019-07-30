package irt.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "array_names")
public class ArrayNameEntity implements Serializable {
	private static final long serialVersionUID = -7272054911477188078L;

	@Id @Column(name = "id_array_names")
	private Long id;  public Long getId() { return id; } public void setId(Long id) { this.id = id; }

    @Column(name = "array_name") @Basic(optional = false)  @NotNull  @Size(min = 1, max = 20)
    private String arrayName;  public String getArrayName() { return arrayName; } public void setArrayName(String arrayName) { this.arrayName = arrayName; }

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "arrayNames", fetch = FetchType.EAGER) @OrderBy("sequence, description")
	private List<ArrayEntity>  arrayEntities; public List<ArrayEntity> getArrayEntities() { return arrayEntities; } public void setArrayEntities(List<ArrayEntity> arrayEntities) { this.arrayEntities = arrayEntities; }
}
