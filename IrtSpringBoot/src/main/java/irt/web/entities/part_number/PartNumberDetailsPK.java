package irt.web.entities.part_number;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class PartNumberDetailsPK implements Serializable {
	private static final long serialVersionUID = 6488991322477151444L;

    @Size(max = 3)
    @Column(name = "first_three_chars", length = 3)
    private String firstThreeChars;

    @Basic(optional = false)
    @NotNull
    @Column(name = "array_sequence", nullable = false)
    private Integer sequence;

    @Size(max = 20)
    @Column(name = "detail_id", length = 20)
    private String detailId;

    public PartNumberDetailsPK() {
	}

	public PartNumberDetailsPK(String firstThreeChars, Integer arraySequence, String detailId) {
		this.firstThreeChars = firstThreeChars;
		this.sequence = arraySequence;
		this.detailId = detailId;
	}

	public String getFirstThreeChars() {
        return firstThreeChars;
    }

    public void setFirstThreeChars(String firstThreeChars) {
        this.firstThreeChars = firstThreeChars;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer arraySequence) {
        this.sequence = arraySequence;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

	@Override
	public String toString() {
		return "\n\t\tPartNumberDetailsPK [firstThreeChars=" + firstThreeChars
				+ ", arraySequence=" + sequence + ", detailId=" + detailId
				+ "]\n\t";
	}
}
