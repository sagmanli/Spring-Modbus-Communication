<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="monitorDevice">
		<div class="registers">
		<h3>INPUT REGISTERS</h3>
		<table>
			<c:forEach items="${inputRegisters}" var="input">
				<tr class="registerBox">
					<td><c:out value="${input.key}" /> : </td>
					<td><span>${input.value}</span></td>
				</tr>
			</c:forEach>
		</table>
		</div>
		<div class="registers">
		
		<h3>HOLDING REGISTERS</h3>
		<table>
			<c:forEach items="${holdingRegisters}" var="holding">
				<tr class="registerBox">
					<td style="width:60%"><c:out value="${holding.key}" /> : </td>
					<td style="width:10%"><span>${holding.value}</span></td>
					<td style="width:30%"><input style="float:left" type="button" onclick="editRegisterValue('${holding.key}', '${holding.value}');" value="Değiştir"></td>
				</tr>
			</c:forEach>
		</table>
		</div>
</div>
