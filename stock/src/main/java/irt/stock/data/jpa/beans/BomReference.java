package irt.stock.data.jpa.beans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
	@Column(name="ref", unique=true, nullable=false, length=500)
	private String references;

	public Long getId() { return id; }
	public String getReferences() { return references; }

	public long getQty() {

		Map<Boolean, List<String>> map = serarateDashAndSpace(references);
		
		long count = map.get(false).size();

		count += map.get(true).stream().map(s->s.split("-")).mapToInt(split->Integer.parseInt(split[1]) - Integer.parseInt(split[0]) + 1).sum();

		return count;
	}

	public Set<Integer> referencesToSet() {

		final IntStream concat = referenceToIntStream(references);

		return concat.boxed().collect(Collectors.toCollection(TreeSet::new));
	}

	public IntStream referenceToIntStream() {
		return referenceToIntStream(references);
	}

	public static IntStream referenceToIntStream(String references) {
		Map<Boolean, List<String>> map = serarateDashAndSpace(references);
		final IntStream bySpace = map.get(false).parallelStream().mapToInt(Integer::parseInt);
		final IntStream byDash = map.get(true).parallelStream().map(s->s.split("-")).flatMapToInt(split->IntStream.range(Integer.parseInt(split[0]), Integer.parseInt(split[1]) + 1));
		return IntStream.concat(bySpace, byDash);
	}

	/**
	 * @return Map where true - with dashes; false - with spaces
	 */
	public static Map<Boolean, List<String>> serarateDashAndSpace(String references) {
		Map<Boolean, List<String>> map = Optional
										.ofNullable(references)
										.map(r->r.split(" "))
										.map(split->Arrays.stream(split))
										.orElse(Stream.empty())
										.filter(ref->!ref.trim().isEmpty())
										.collect(Collectors.partitioningBy(s->s.contains("-")));
		return map;
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
