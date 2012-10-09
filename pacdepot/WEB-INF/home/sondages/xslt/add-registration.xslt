<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="/page/body">
		<table width="100%">
			<tr>
				<td width="33%" valign="top">
					<h1>Inscription</h1>
				</td>
				<td width="67%" valign="top">
					<font color="#FF0000"><b>
						<xsl:if test="/page/question/errors">
							<font size="+1"><b>Corriger les champs avec un</b><font size="+2">*</font></font>
						</xsl:if>
						<xsl:choose>
							<xsl:when test="/page/question/errors/@html = 'true'">
								<br/><li>vérifier le syntax HTML de la description</li>
							</xsl:when>
							<xsl:when test="/page/@state = 'cancelled'">
								Votre question n'a pas été ajoutée, vous pouvez recommencer maintenant ou 
								<br/>simplement choisir une autre option du menu principal.
							</xsl:when>
							<xsl:when test="/page/@state = 'success'">
								Mise-à-jour effectuée avec succès.
							</xsl:when>
						</xsl:choose>
					</b></font>	

					<form method="post" action="/{/page/@section}/previewRegistration" enctype="multipart/form-data">
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Détails d'inscription
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="/page/registrationBitmap/errors/@title = 'true'"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Titre</b><br/>
								<input name="title" type="text" size="60" maxlength="60" class="input" value="{/page/registrationBitmap/@title}"/>
								<br/><br/>

								<xsl:if test="/page/registrationBitmap/errors/@description = 'true'"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Description</b><br/>
								(maximum de 10000 caractères)<br/>
								<textarea name="description" rows="7" cols="60" class="input">
									<xsl:value-of select="/page/registrationBitmap/descriptionStr/node()"/>
								</textarea>
								<br/><br/>
								
								<b>Champs requis</b><br/>
								Cochez les champs requis pour enregistrement<br/>
								<table>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@lastname = 'true'">
													<input type="checkbox" name="lastname" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="lastname" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Nom </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@firstname = 'true'">
													<input type="checkbox" name="firstname" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="firstname" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Prénom </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@telephone = 'true'">
													<input type="checkbox" name="telephone" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="telephone" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Téléphone </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@address = 'true'">
													<input type="checkbox" name="address" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="address" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Addresse </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@city = 'true'">
													<input type="checkbox" name="city" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="city" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Ville </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@postalcode = 'true'">
													<input type="checkbox" name="postalcode" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="postalcode" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Code postale </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@country = 'true'">
													<input type="checkbox" name="country" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="country" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Pays </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@province = 'true'">
													<input type="checkbox" name="province" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="province" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Province </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@email = 'true'">
													<input type="checkbox" name="email" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="email" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Courriel </td>
									</tr>
									<tr>
										<td> 
											<xsl:choose>
												<xsl:when test="/page/registrationBitmap/@sex = 'true'">
													<input type="checkbox" name="sex" value="true" checked="true"/> 
												</xsl:when>
												<xsl:otherwise>
													<input type="checkbox" name="sex" value="true"/> 
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td> Sexe </td>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>



						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Photo
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:choose>
									<xsl:when test="/page/registrationBitmap/errors/@photo">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:when>
									<xsl:when test="/page/registrationBitmap/errors/@size">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:when>
								</xsl:choose>
								Photo: <br/>
								<xsl:if test="/page/registrationBitmap/@hasImage = 'true'">
									<img src="/images/registrationBitmap?id={/page/registrationBitmap/@id}" border="0"/>
									<br/>
									<input type="checkbox" name="deleteImage" value="true"/>Effacer l'image
									<br/>
								</xsl:if>
								<input type="file" name="photo"/>
							</xsl:with-param>
						</xsl:call-template>
						
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
								<table cellpadding="0" cellspacing="0">
									<tr>
										<td><input type="submit" value="Valider le formulaire"/></td>
										<td><input type="reset" value="Annuler les modifications"/></td>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>
					</form>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
