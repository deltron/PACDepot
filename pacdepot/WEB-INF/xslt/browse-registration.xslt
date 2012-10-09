<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<center>
			<table width="75%">
				<tr>
					<xsl:if test="/page/registrationBitmap/@hasImage = 'true'">
						<td valign="top">
							<img src="/images/registrationBitmap?id={/page/registrationBitmap/@id}" border="0"/>
						</td>
					</xsl:if>
					<td width="15">
						<img src="/images/spacer.gif" width="15" height="1"/>
					</td>
					<td width="75%" valign="top">
						<form method="post" action="/browse/registerUser">
							<input type="hidden" name="itemid" value="{/page/registrationBitmap/@id}"/>
							<table cellpadding="5" width="100%" style="border: 2px solid; border-color: #000000;" bgcolor="#EEEEEE">
								<tr>
									<td>
										<div class="text">
											<b><xsl:value-of select="/page/registrationBitmap/@title"/></b><br/><br/>
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/description = 'null'"/>
												<xsl:when test="/page/registrationBitmap/description">
													<xsl:copy-of select="/page/registrationBitmap/description/" />
												</xsl:when>
											</xsl:choose>
											<br/><br/>

											<table>
												<xsl:if test="/page/registrationBitmap/@lastname = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@lastname">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>
															Nom 
														</td>
														<td> 
															<input type="text" name="lastname" value="{/page/user/@lastname}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@firstname = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@firstname">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>
															Prénom 
														</td>
														<td> 
															<input type="text" name="firstname" value="{/page/user/@firstname}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@telephone = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@telephone">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>														
															Téléphone 
														</td>
														<td> 
															<input type="text" name="telephone" value="{/page/user/@telephone}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@address = 'true'">
													<tr>
														<td>
															<xsl:if test="/page/user/errors/@address">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>														
															Addresse 
														 </td>
														<td> 
															<input type="text" name="address" value="{/page/user/@address}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@city = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@city">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>																					
															Ville 
														</td>
														<td> 
															<input type="text" name="city" value="{/page/user/@city}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@postalcode = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@postalcode">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>																					
															Code postale
														</td>
														<td> 
															<input type="text" name="postalcode" value="{/page/user/@postalcode}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@country = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@country">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>																			
															Pays 
														</td>
														<td> 
															<input type="text" name="country" value="{/page/user/@country}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@province = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@province">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>														
															Province 
														</td>
														<td> 
															<input type="text" name="province" value="{/page/user/@province}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@email = 'true'">
													<tr>
														<td>
															<xsl:if test="/page/user/errors/@email">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>
															Courriel 
														</td>
														<td> 
															<input type="text" name="email" value="{/page/user/@email}" size="32"/> 
														</td>
													</tr>
												</xsl:if>
												<xsl:if test="/page/registrationBitmap/@sex = 'true'">
													<tr>
														<td> 
															<xsl:if test="/page/user/errors/@sex">
																<font color="#FF0000" size="+2"><b>*</b></font>
															</xsl:if>															
															Sexe 
														</td>
														<td> 
															<select name="sex">
																<xsl:choose>
																	<xsl:when test="/page/user/@sex = 'male'">
																		<option value="male" selected="true">Male</option>
																	</xsl:when>
																	<xsl:otherwise>
																		<option value="male">Male</option>
																	</xsl:otherwise>
																</xsl:choose>
																<xsl:choose>
																	<xsl:when test="/page/user/@sex = 'female'">
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
												<tr>
													<td></td>
													<td>
														<xsl:if test="/page/@section = 'browse'">
															<input type="submit" value="Continuer &gt;&gt;"/>
														</xsl:if>
													</td>
												</tr>
											</table>
										</div>
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</center>
	

		<xsl:if test="/page/@state = 'preview'">
			<xsl:call-template name="inputsection">
				<xsl:with-param name="body">
					<table cellpadding="0" cellspacing="0">
						<tr>
							<form method="post" action="/{/page/@section}/questionSummary">
								<td>
									<input type="submit" value="Confirmer"/>
								</td>	
							</form>									
							<form method="post" action="/{/page/@section}/modifyRegistration">
								<td>
									<input type="submit" value="Modifier"/>
								</td>
							</form>
							<form method="post" action="/{/page/@section}/cancelRegistration">
								<td>
									<input type="submit" value="Détruire"/>
								</td>
							</form>
						</tr>
					</table>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
