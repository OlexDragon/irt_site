package irt.stock.data.jpa.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

public enum UserRoles implements GrantedAuthority{

	WORK_ORDER			(512, "Can work with work orders."),
//	DEVICE_TYPE_UPDATE	(1024, ""),
	USER_EDIT			(2048, "Can edit IRT worker's data."),
    SCHEMATIC_LETTER	(4096, "Can change schematic letter(AutoCAD)."),
    ALT_PART_NUMBER		(8192, "Can add alternative components."),
    EDIT_COST			(16384, "Can edit cost"),
//    DEVICE_TYPE			(32768, ""),
    SCHEMATIC_PART		(65536, "Can change schematic part(AutoCAD)."),
    STOCK				(131072, "Stok worker"),
    EDIT_COMPANIES		(262144, "Can add, edit, delete company"),
    USER				(524288, "Can see all IRT workers"),
	ADMIN				(1048576, "Admin"),
	EDITING				(2097152, "Edit component data."),
	SELLERS				(4194304, "Can add, edit, delete sellers"),
	DATABASE			(8388608, "Database"),
	EDIT_BOM			(16777216, "Can edit BOMs.");

	private final long permission;
	private String description;

	private UserRoles(long permission, String description) {
		this.permission = permission;
		this.description = description;
	}

	public static Collection<? extends GrantedAuthority> getAuthorities(long permissions) {
		return Arrays.stream(values()).filter(ur->(ur.permission&permissions)!=0).collect(Collectors.toList());
	}

	public long getPermission() {
		return permission;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasRole(Long roles) {
		return Optional.ofNullable(roles).map(r->r&permission).map(r->r>0).orElse(false);
	}

	public static Long toLong(UserRoles[] userRoles) {
		return Arrays.stream(userRoles).parallel().mapToLong(UserRoles::getPermission).sum();
	}

	@Override
	public String getAuthority() {
		return name();
	}
}
