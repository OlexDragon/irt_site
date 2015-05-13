/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package irt.web.entities.bom;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "bom_ref", catalog = "irt", schema = "")
@NamedQueries({
    @NamedQuery(name = "BomRefEntity.findAll", query = "SELECT b FROM BomRefEntity b")})
public class BomRefEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String ref;
    private int qty;

    public BomRefEntity() {
    }

    public BomRefEntity(Integer id) {
        this.id = id;
    }

    public BomRefEntity(Integer id, String ref) {
        this.id = id;
        this.ref = ref;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "ref")
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
        new CountQty();
    }

    @Transient
	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        return o!=null ? o.hashCode()==hashCode() : false;
    }

	@Override
	public String toString() {
		return "BomRefEntity [\n\t\tid=" + id +
				",\n\t\tref=" + ref  + "]";
	}

	private class CountQty implements Runnable{

		private final Logger logger = LogManager.getLogger();
		private CountQty(){
			logger.entry();
			Thread t = new Thread(this);
			int p = t.getPriority();
			if(p > Thread.MIN_PRIORITY)
				t.setPriority(--p);
			t.setDaemon(true);
			t.start();
		}

		@Override
		public void run() {
			if(ref!=null){
				for(String s:ref.trim().split(" ")){
					logger.trace(s);
					if(s.contains("-"))
						qty += difference(s);
					else
						qty++;
				}
			}
			logger.exit(ref);
		}

		private int difference(String str) {
			logger.entry(str);
			int dif = 0;
			String[] split = str.split("-");
				if(split.length==2)
					dif = Integer.parseInt(split[1]) - Integer.parseInt(split[0]);
				return dif;
		}
	}
}
