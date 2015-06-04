package irt.web.entities.all;

import irt.web.entities.component.MovementEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Oleksandr
 */
@Entity
@Table(name = "users", catalog = "irt", schema = "")
@XmlRootElement
public class UsersEntity implements Serializable {
    private static final long serialVersionUID = 1L;

	public enum Permission implements GrantedAuthority{
		ACCOUNT_EXPIRED		(1),
		LOCKED				(1<<1),
		CREDENTIALS_EXPIRED	(1<<2),
		DISABLED			(1<<3),
		DEFAULT				(1<<4),
		WORK_ORDER			(1<<9),//512),
		DEVICE_TYPE_UPDATE	(1<<10),//1024),
		USER_EDIT			(1<<11),//2048),
		SCHEMATIC_LETTER	(1<<12),//4096),
		ALT_PART_NUMBER		(1<<13),//8192),
		EDIT_COST			(1<<14),//16384),
		DEVICE_TYPE			(1<<15),//32768),
		SCHEMATIC_PART		(1<<16),//65536),
		STOCK				(1<<17),//131072),
		EDIT_COMPANIES		(1<<18),//262144),
		USER				(1<<19),//524288),
		ADMIN				(1<<20),//1048576),
		EDITING				(1<<21),//2097152),
		SELLERS				(1<<22),//4194304),
		DATABASE			(1<<23);//8388608);

		private long permission;

		private Permission(long permission){
			this.permission = permission;
		}

		public long toLong(){
			return permission;
		}

		public static Long toLong(Set<Permission> permissionsList){
			Long permissions;
			if(permissionsList==null || permissionsList.isEmpty())
				permissions = null;
			else{
				permissions = 0L;
				for(Permission p:permissionsList)
					permissions += Long.MAX_VALUE & p.permission;
			}

			return permissions;
		}

		public static Set<Permission> toPermissions(Long permissions){
			SortedSet<Permission> set;

			if(permissions==null || permissions==0)
				set = null;
			else {

				if((permissions & 15) > 0)//if disabled or expired (ACCOUNT_EXPIRED, LOCKED, CREDENTIALS_EXPIRED, DISABLED)
					permissions = permissions & 15;

				set = new TreeSet<Permission>();
				for(Permission p:Permission.values())
					if((p.permission&permissions)!=0)
						set.add(p);
			}

			return set;
		}

		public static boolean hasPermission(Long permissions, Permission permissionToCheckFor){
			return permissions!=null && (permissions&permissionToCheckFor.permission)>0;
		}

		public static Long addPermission(Long permissions, Permission permissionToAdd){
			return permissions!=null ? permissions|permissionToAdd.permission : permissionToAdd.permission;
		}

		public static Long removePermission(Long permissions, Permission permissionToAdd){
			return permissions!=null ? permissions&(-1l ^ permissionToAdd.permission) : null;
		}
		@Override
		public String getAuthority() {
			return name();
		}
    	
    }

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    private String password;

    @Size(max = 20)
    private String firstname;

    @Size(max = 20)
    private String lastname;

    private Long permission;

    @Size(max = 3)
    private String extension;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 45)
    private String eMail;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "who", fetch = FetchType.EAGER)
    private List<MovementEntity> movementEntityList;

    public UsersEntity() {
    }

    public UsersEntity(Long id) {
        this.id = id;
    }

    public UsersEntity(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public Set<Permission> getPermissions() {
        return Permission.toPermissions(permission);
    }

    public Long getPermission() {
        return permission;
    }

    public void setPermission(Long permission) {
        this.permission = permission;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersEntity)) {
            return false;
        }
        UsersEntity other = (UsersEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "irt.web.entities.UsersEntity[ id=" + id + " ]";
    }

    @XmlTransient
    public List<MovementEntity> getMovementEntityList() {
        return movementEntityList;
    }

    public void setMovementEntityList(List<MovementEntity> movementEntityList) {
        this.movementEntityList = movementEntityList;
    }
    
}
