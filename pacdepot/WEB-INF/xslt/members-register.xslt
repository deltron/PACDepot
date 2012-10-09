<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<h3>Inscription</h3>
		<font color="#FF0000"><b>
			<xsl:if test="errors/*">
				<font size="+1"><b>Corriger les champs avec un</b><font size="+2">*</font></font>
			</xsl:if>
		</b></font>	

		<form method="post" action="/members/registerUser">
			<table width="100%">
				<tr>
					<td rowspan="2"> <!-- left side -->
						<table>
							<tr> 
								<td>
									<xsl:if test="errors/username"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Nom d'usager:
								</td>
								<td><input name="username" type="text" size="40" 
										maxlength="40" class="input" value="{item/user/@username}"/></td>
							</tr>
							<tr> 
								<td>
									<xsl:if test="errors/name"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Nom:
								</td>
								<td><input name="name" type="text" size="40" 
										maxlength="40" class="input" value="{item/user/@name}"/></td>
							</tr>
							<tr> 
								<td>
									<xsl:if test="errors/telephone">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Téléphone:
									<br/>
									<font size="-2">
										(514) 555-1212 poste 555
									</font>
								</td>
								<td>
									<input name="telephone" type="text" 
										size="40" maxlength="40" class="input" value="{item/user/@telephone}" />
								</td>
							</tr>
							<tr> 
								<td>
									<xsl:if test="errors/email">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Courriel:
									<br/>
									<font size="-2">
										info@pacdepot.com
									</font>
								</td>
								<td><input name="email" type="text" 
										size="40" maxlength="40" class="input" value="{item/user/@email}" />
								</td>
							</tr>
						</table>

						<table>
							<tr> 
								<td><img src="images/spacer.gif" width="150" height="1"/></td>
								<td><img src="images/spacer.gif" width="150" height="1"/></td>
								<td><img src="images/spacer.gif" width="150" height="1"/></td>
							</tr>
							<tr>
								<td>
									<xsl:if test="errors/cityid">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Endroit: <br/>
									<select name="cityid" size="5" class="body">
										<xsl:for-each select="cities/city">
											<option value="{@id}">
												<xsl:value-of select="@name"/>
											</option>
										</xsl:for-each>
									</select>
								</td>
								<td>
									<xsl:if test="errors/sponsorid">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Promoteur: <br/>
									<select size="5" name="sponsorid" class="body">
										<xsl:choose>
											<xsl:when test="count(sponsors/sponsor) = 1">
												<option value="{sponsors/sponsor/@id}" selected="true">
													<xsl:value-of select="sponsors/sponsor/@name"/>
												</option>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="sponsors/sponsor">
													<option value="{@id}">
														<xsl:value-of select="@name"/>
													</option>
												</xsl:for-each>
											</xsl:otherwise>
										</xsl:choose>
									</select>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<br/>
			<center>
				<table>
					<tr>
						<td>
							<input type="submit" value="Valider le formulaire"/>
						</td>
						<td>
							<input type="reset" value="Annuler les modifications"/>
						</td>
					</tr>
				</table>
			</center>
		</form>
	</xsl:template>
</xsl:stylesheet>
