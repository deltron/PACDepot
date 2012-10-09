<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="/page/body">
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

		<form method="post" action="/{/page/@section}/previewQuestion" enctype="multipart/form-data">
			<table width="100%">
				<tr>
					<td>
						<table width="100%">
							<tr>
								<td>
									<xsl:if test="/page/question/errors/@itemid = 'true'"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Numero de l'item
								</td>
								<td><input name="itemid" type="text" 
										size="8" maxlength="60" class="input" value="{/page/question/@itemid}"/></td>
							</tr>
							<tr>
								<td>
									<xsl:if test="/page/question/errors/@sequence = 'true'"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Séquence
								</td>
								<td><input name="sequence" type="text" 
										size="8" maxlength="4" class="input" value="{/page/question/@sequence}"/></td>
							</tr>
							<tr>
								<td>
									<xsl:if test="/page/question/errors/@title = 'true'"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Titre
								</td>
								<td><input name="title" type="text" 
										size="60" maxlength="60" class="input" value="{/page/question/@title}"/></td>
							</tr>
							<tr> 
								<td>
									<xsl:if test="/page/question/errors/@href = 'true'">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									URL:
									<br/>
									<font size="-2">
										http://www.pacdepot.com/
									</font>
								</td>
								<td>
									<input name="href" type="text" size="40" 
										maxlength="40" class="input" value="{/page/question/@href}"/>
								</td>
							</tr>
							<tr>
								<td valign="top">
									<xsl:if test="/page/question/errors/@description = 'true'"> 
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:if>
									Description:<br/>
									<font size="-2">
										(maximum de<br/>
										10000 caractères)
									</font>
								</td>
								<td>
									<textarea name="description" rows="7" cols="60" class="input">
										<xsl:value-of select="/page/question/descriptionStr/node()"/>
									</textarea>
								</td>
							</tr>
							<tr>
								<td valign="top">
									Type de réponse:
									<br/>
									<font size="-2">(faites un choix parmi)</font>
								</td>
								<td>
									<table cellspacing="0" cellpadding="8" style="border: 1px solid black">
										<tr>
											<td bgcolor="#525D76" height="24" style="border-bottom: 1px solid">
												<input type="radio" name="type" value="open" checked="true"/>
												<font face="arial,helvetica.sanserif" color="#ffffff">	
													<strong> Réponse ouverte </strong>
												</font>
											</td>
										</tr>
										<tr>
											<td>
												<xsl:if test="/page/errors/@openAnswer"> 
													<font color="#FF0000" size="+2"><b>*</b></font>
												</xsl:if>
												<textarea name="openAnswer"/>
											</td>
										</tr>
									</table>
									<br/>
									<table cellspacing="0" cellpadding="8" style="border: 1px solid black">
										<tr>
											<td bgcolor="#525D76" height="24" style="border-bottom: 1px solid">
												<input type="radio" name="type" value="scale"/>
												<font face="arial,helvetica.sanserif" color="#ffffff">	
													<strong> Échelle </strong>
												</font>
											</td>
										</tr>
										<tr>
											<td>
												<input type="text" name="minLabel" size="8" value="{/page/question/scale/@minLabel}"/>
												<input type="radio" name="scaleValue" value="1"/>
												<input type="radio" name="scaleValue" value="2"/>
												<input type="radio" name="scaleValue" value="3"/>
												<input type="radio" name="scaleValue" value="4"/>
												<input type="radio" name="scaleValue" value="5"/>
												<input type="text" name="maxLabel" size="8" value="{/page/question/scale/@maxLabel}"/>
											</td>
										</tr>
									</table>
									<br/>
									<table cellspacing="0" cellpadding="8" style="border: 1px solid black">
										<tr>
											<td bgcolor="#525D76" height="24" style="border-bottom: 1px solid">
												<input type="radio" name="type" value="multichoice"/>
												<font face="arial,helvetica.sanserif" color="#ffffff">	
													<strong> Choix multiple </strong>
												</font>
											</td>
										</tr>
										<tr>
											<td>
												Exemple: 
												<select>
													<option>Choix 1</option>
													<option>Choix 2</option>
													<option>Choix 3</option>
												</select>
												<br/><br/>
												<table>
													<tr>
														<td>
															1:
															<input type="text" name="choice1" size="8" 
																value="{/page/question/multichoice/@choice1}"/>
														</td>
														<td>
															2:
															<input type="text" name="choice2" size="8" 
																value="{/page/question/multichoice/@choice2}"/>
														</td>
													</tr>
													<tr>
														<td>
															3:
															<input type="text" name="choice3" size="8"
																value="{/page/question/multichoice/@choice3}"/>
														</td>
														<td>
															4:
															<input type="text" name="choice4" size="8" 
																value="{/page/question/multichoice/@choice4}"/>
														</td>
													</tr>
													<tr>
														<td>
															5:
															<input type="text" name="choice3" size="8"
																value="{/page/question/multichoice/@choice5}"/>
														</td>
														<td>
															6:
															<input type="text" name="choice4" size="8" 
																value="{/page/question/multichoice/@choice6}"/>
														</td>
													</tr>
													<tr>
														<td>
															7:
															<input type="text" name="choice3" size="8"
																value="{/page/question/multichoice/@choice7}"/>
														</td>
														<td>
															8:
															<input type="text" name="choice4" size="8" 
																value="{/page/question/multichoice/@choice8}"/>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
					<td align="right" valign="top"> <!-- right side -->
						<table>
							<tr>
								<td>
									<xsl:choose>
										<xsl:when test="/page/question/errors/@photo">
											<font color="#FF0000" size="+2"><b>*</b></font>
										</xsl:when>
										<xsl:when test="/page/question/errors/@size">
											<font color="#FF0000" size="+2"><b>*</b></font>
										</xsl:when>
									</xsl:choose>
									Photo: <br/>
									<xsl:if test="/page/question/@hasImage = 'true'">
										<img src="/images/question?id={/page/question/@id}" border="0"/>
										<br/>
										<input type="checkbox" name="deleteImage" value="true"/>Effacer l'image
										<br/>
									</xsl:if>
									<input type="file" name="photo"/>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			
			<center>
				<table>
					<tr>
						<td><input type="submit" value="Valider le formulaire"/></td>
						<td><input type="reset" value="Annuler les modifications"/></td>
					</tr>
				</table>
			</center>
		</form>
	</xsl:template>
</xsl:stylesheet>
