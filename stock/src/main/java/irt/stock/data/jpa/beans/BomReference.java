package irt.stock.data.jpa.beans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bom_ref")
public class BomReference {

	protected BomReference() { }
	public BomReference(String references) {
		this.references = references;
	}

	@Id @GeneratedValue
	private Long id;
	@Column(name="ref")
	private String references;

	public Long getId() { return id; }
	public String getReferences() { return references; }

	public long getQty() {

		Map<Boolean, List<String>> map = Optional
										.ofNullable(references)
										.map(r->r.split(" "))
										.map(split->Arrays.stream(split))
										.orElse(Stream.empty())
										.filter(ref->!ref.trim().isEmpty())
										.collect(Collectors.partitioningBy(s->s.contains("-")));
		
		long count = map.get(false).size();

		count += map.get(true).stream().map(s->s.split("-")).mapToLong(split->Integer.parseInt(split[1]) - Integer.parseInt(split[0]) + 1).sum();

		return count;
	}

	@Override
	public int hashCode() {
		return 31 + ((id == null) ? 0 : id.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BomReference other = (BomReference) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BomReference [id=" + id + ", references=" + references + "]";
	}
}
