<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%" >
			<tr>
				<xsl:if test="/page/registrationBitmap/@hasImage = 'true'">
					<td width="33%" valign="top" >
						<img src="/images/registrationBitmap?id={/page/registrationBitmap/@id}" border="0"/>
					</td>
				</xsl:if>
				<td width="67%" valign="top">
					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Identification du participant
						</xsl:with-param>
						<xsl:with-param name="body">
							<font size="+1"><b><xsl:value-of select="/page/registrationBitmap/@title"/></b></font>
							<br/>
							<xsl:choose>
								<xsl:when test="/page/registrationBitmap/description = 'null'"/>
								<xsl:when test="/page/registrationBitmap/description">
									<xsl:copy-of select="/page/registrationBitmap/description/" />
								</xsl:when>
							</xsl:choose>
							<table>
								<xsl:if test="/page/registrationBitmap/@lastname = 'true'">
									<tr>
										<td> Nom </td>
										<td> 
											<input type="text" name="lastname" value="{/page/registration/@name}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@firstname = 'true'">
									<tr>
										<td> Prénom </td>
										<td> 
											<input type="text" name="firstname" value="{/page/registration/@firstname}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@telephone = 'true'">
									<tr>
										<td> Téléphone </td>
										<td> 
											<input type="text" name="telephone" value="{/page/registration/@telephone}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@address = 'true'">
									<tr>
										<td> Addresse </td>
										<td> 
											<input type="text" name="address" value="{/page/registration/@address}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@city = 'true'">
									<tr>
										<td> Ville </td>
										<td> 
											<input type="text" name="city" value="{/page/registration/@city}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@postalcode = 'true'">
									<tr>
										<td> Code postale </td>
										<td> 
											<input type="text" name="postalcode" value="{/page/registration/@postalcode}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@country = 'true'">
									<tr>
										<td> Pays </td>
										<td> 
											<input type="text" name="country" value="{/page/registration/@country}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@province = 'true'">
									<tr>
										<td> Province </td>
										<td> 
											<input type="text" name="province" value="{/page/registration/@province}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@email = 'true'">
									<tr>
										<td> Courriel </td>
										<td> 
											<input type="text" name="email" value="{/page/registration/@email}" size="32"/> 
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="/page/registrationBitmap/@sex = 'true'">
									<tr>
										<td> Sexe </td>
										<td> 
											<select name="sex">
												<xsl:choose>
													<xsl:when test="/page/registration/@sex = 'male'">
														<option value="male" selected="true">Male</option>
													</xsl:when>
													<xsl:otherwise>
														<option value="male">Male</option>
													</xsl:otherwise>
												</xsl:choose>
												<xsl:choose>
													<xsl:when test="/page/registration/@sex = 'female'">
														<option value="female" selected="true">Female</option>
													</xsl:when>
													<xsl:otherwise>
														<option value="female">Female</option>
													</xsl:otherwise>
												</xsl:choose>
											</select>
										</td>
									</tr>
								</xsl:if>
							</table>
							<xsl:call-template name="inputsection">
								<xsl:with-param name="body">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<form method="post" action="/{/page/@section}/modifyRegistration?id={/page/body/item/@id}">
												<td>
													<input type="submit" value="Modifier"/>
												</td>
											</form>
											<form method="post" action="/{/page/@section}/cancelRegistration?id={/page/body/item/@id}">
												<td>
													<input type="submit" value="Détruire"/>
												</td>
											</form>
										</tr>
									</table>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
		
		<xsl:for-each select="/page/question">
			<table width="100%">
				<tr>
					<xsl:if test="@hasImage = 'true'">
						<td width="33%" valign="top">
							<img src="/images/question?id={@id}" border="0"/>
						</td>
					</xsl:if>
					<td width="67%" valign="top">
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Question #<xsl:value-of select="@sequence"/>
							</xsl:with-param>
							<xsl:with-param name="body">
								<font size="+1"><b><xsl:value-of select="@title"/></b></font><br/>
								<a href="{@href}"><xsl:value-of select="@href"/></a><br/>
								<xsl:choose>
									<xsl:when test="description = 'null'"/>
									<xsl:when test="description">
										<br/>
										<xsl:copy-of select="description/" />
										<br/>
									</xsl:when>
								</xsl:choose>
								<xsl:choose>
									<xsl:when test="@type = 'none'"/>
									<xsl:when test="@type = 'open'">
										<textarea name="openAnswer" />
									</xsl:when>
									<xsl:when test="@type = 'scale'"> 
										<xsl:value-of select="@minLabel"/>
										<input type="radio" name="scaleValue" value="1"/>
										<input type="radio" name="scaleValue" value="2"/>
										<input type="radio" name="scaleValue" value="3"/>
										<input type="radio" name="scaleValue" value="4"/>
										<input type="radio" name="scaleValue" value="5"/>
										<xsl:value-of select="@maxLabel"/>
									</xsl:when>
									<xsl:when test="@type = 'multichoice'">
										<select>
											<xsl:if test="@choice1">
												<option>
													<xsl:value-of select="@choice1"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice2">
												<option>
													<xsl:value-of select="@choice2"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice3">
												<option>
													<xsl:value-of select="@choice3"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice4">
												<option>
													<xsl:value-of select="@choice4"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice5">
												<option>
													<xsl:value-of select="@choice5"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice6">
												<option>
													<xsl:value-of select="@choice6"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice7">
												<option>
													<xsl:value-of select="@choice7"/>
												</option>
											</xsl:if>
											<xsl:if test="@choice8">
												<option>
													<xsl:value-of select="@choice8"/>
												</option>
											</xsl:if>
										</select>
									</xsl:when>
								</xsl:choose>
								<xsl:call-template name="inputsection">
									<xsl:with-param name="body">
										<table cellpadding="0" cellspacing="0">
											<tr>
												<form method="post" action="/{/page/@section}/modifyQuestion?id={@id}">
													<td>
														<input type="submit" value="Modifier"/>
													</td>
												</form>
												<form method="post" action="/{/page/@section}/cancelQuestion?id={@id}">
													<td>
														<input type="submit" value="Détruire"/>
													</td>
												</form>
											</tr>
										</table>
									</xsl:with-param>
								</xsl:call-template>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</table>
		</xsl:for-each>

		<xsl:call-template name="inputsection">
			<xsl:with-param name="body">
				<table cellpadding="0" cellspacing="0">
					<tr>
						<form method="post" action="/{/page/@section}/question">
							<td>
								<input type="submit" value="Ajouter une question"/>
							</td>
						</form>
						<xsl:if test="/page/@section = 'members'">
							<form method="post" action="/members/previewItem?id={/page/body/item/@id}">
								<td>
									<input type="submit" value="Retour à l'item"/>
								</td>
							</form>
						</xsl:if>
					</tr>
				</table>
			</xsl:with-param>
		</xsl:call-template>

	</xsl:template>
</xsl:stylesheet>
