<%@page import="irt.data.HTMLWork"%>
<table id="inputs" class="sticker">
<tr>
	<td>Password: </td>
	<td colspan="3"><input type="password" id="u_<%=UserBean.PASSWORD%>" name="u_<%=UserBean.PASSWORD%>" value="<%=userToEdit.getPassword()%>" /></td>
</tr>
<tr>
	<td><input type="checkbox" name="u_flag_Edit" id="u_flag_Edit" <%=HTMLWork.getChecked(userToEdit.isEditing())%> /><label for="u_flag_Edit">Edit</label></td>
	<td><input type="checkbox" name="u_flag_user" id="u_flag_user" <%=HTMLWork.getChecked(userToEdit.isUser())%> /><label for="u_flag_user">User</label></td>
	<td><input type="checkbox" name="u_flag_userEdit" id="u_flag_userEdit" <%=HTMLWork.getChecked(userToEdit.isUserEdit())%> /><label for="u_flag_userEdit">Edit User</label></td>
	<td><input type="checkbox" name="u_flag_schemLetter" id="u_flag_schemLetter" <%=HTMLWork.getChecked(userToEdit.isSchematicLetter())%> /><label for="u_flag_schemLetter">Shematic Letter</label></td>
</tr>
<tr>
	<td><input type="checkbox" name="u_flag_stock" id="u_flag_stock" <%=HTMLWork.getChecked(userToEdit.isStock())%> /><label for="u_flag_stock">Stock</label></td>
	<td><input type="checkbox" name="u_flag_cost" id="u_flag_cost" <%=HTMLWork.getChecked(userToEdit.isEditCost())%> /><label for="u_flag_cost">Cost</label></td>
	<td><input type="checkbox" name="u_flag_seller" id="u_flag_seller" <%=HTMLWork.getChecked(userToEdit.isSellers())%> /><label for="u_flag_seller">Companies</label></td>
	<td><input type="checkbox" name="u_flag_part" id="u_flag_part" <%=HTMLWork.getChecked(userToEdit.isSchematicPart())%> /><label for="u_flag_part">Schematic Part</label></td>
</tr>
<tr>
	<td><input type="checkbox" name="u_flag_alt" id="u_flag_alt" <%=HTMLWork.getChecked(userToEdit.isAlt())%> /><label for="u_flag_alt">Alt</label></td>
	<td><input type="checkbox" name="u_flag_data" id="u_flag_data" <%=HTMLWork.getChecked(userToEdit.isDatabase()) %> /><label for="u_flag_data">Data</label></td>
	<td><input type="checkbox" name="u_flag_seller_edit" id="u_flag_seller_edit" <%=HTMLWork.getChecked(userToEdit.isEditCompanies())%> /><label for="u_flag_seller_edit">Edit Sellers</label></td>
	<td><input type="checkbox" name="u_flag_deviceType" id="u_flag_deviceType" <%=HTMLWork.getChecked(userToEdit.isDeviceType())%> /><label for="u_flag_deviceType">Device Type</label></td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td></td>
	<td><input type="checkbox" name="u_flag_deviceTypeUpdate" id="u_flag_deviceTypeUpdate" <%=HTMLWork.getChecked(userToEdit.isDeviceTypeUpdate())%> /><label for="u_flag_deviceTypeUpdate">Device Type Update</label></td>
</tr>
</table>