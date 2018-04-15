package irt.stock.data.jpa.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

public enum UserRules implements GrantedAuthority{

	WORK_ORDER			(512),
	DEVICE_TYPE_UPDATE	(1024),
	USER_EDIT			(2048),
    SCHEMATIC_LETTER	(4096),
    ALT_PART_NUMBER		(8192),
    EDIT_COST			(16384),
    DEVICE_TYPE			(32768),
    SCHEMATIC_PART		(65536),
    STOCK				(131072),
    EDIT_COMPANIES		(262144),
    USER				(524288),
	ADMIN				(1048576),
	EDITING				(2097152),
	SELLERS				(4194304),
	DATABASE			(8388608);

	private long permission;

	private UserRules(long permission) {
		this.permission = permission;
	}

	@Override
	public String getAuthority() {
		return name();
	}

	public static Collection<? extends GrantedAuthority> getAuthorities(long permissions) {

		return Arrays.stream(values()).filter(ur->(ur.permission&permissions)!=0).collect(Collectors.toList());
	}
}
