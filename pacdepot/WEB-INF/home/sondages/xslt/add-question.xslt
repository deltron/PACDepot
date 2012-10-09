<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="/page/body">
		<table width="100%">
			<tr>
				<td width="33%" valign="top">
					<h1>Question</h1>
				</td>
				<td width="67%">
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
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Détails de votre question
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="/page/question/errors/@title = 'true'"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Titre</b><br/>
								<input name="title" type="text" size="60" maxlength="60" class="input" value="{/page/question/@title}"/>
								<br/><br/>

											
								<xsl:if test="/page/question/errors/@href = 'true'">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>URL</b><br/>
								http://www.pacdepot.com/<br/>
								<input name="href" type="text" size="40" maxlength="40" class="input" value="{/page/question/@href}"/>
								<br/><br/>
								
								<xsl:if test="/page/question/errors/@description = 'true'"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Description</b><br/>
								(maximum de 10000 caractères)<br/>
								<textarea name="description" rows="7" cols="60" class="input">
									<xsl:value-of select="/page/question/descriptionStr/node()"/>
								</textarea>
								<br/><br/>
							</xsl:with-param>
						</xsl:call-template>

						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Photo
							</xsl:with-param>
							<xsl:with-param name="body">
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
							</xsl:with-param>
						</xsl:call-template>


						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Type de réponse
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="/page/question/errors/@type = 'true'"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Type de réponse</b><br/>
								(faites un choix parmi)<br/><br/>

								
								<xsl:choose>
									<xsl:when test="/page/question/@type = 'none'">
										<input type="radio" name="type" value="none" checked="true"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="radio" name="type" value="none" />
									</xsl:otherwise>
								</xsl:choose>
								<b>Pas de réponse</b><br/>
								Vous pouvez utilisez cet option pour faire une page de 
								remerciement à la fin des questions ou pour insérer des
								intructions spéciales dans la séquence des questions.
								<br/><br/>
								
								<xsl:choose>
									<xsl:when test="/page/question/@type = 'open'">
										<input type="radio" name="type" value="open" checked="true"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="radio" name="type" value="open"/>
									</xsl:otherwise>
								</xsl:choose>
								<b>Réponse ouverte</b><br/>
								Exemple:<br/>
								<textarea name="openAnswer"/>
								<br/><br/>
				
								<xsl:choose>
									<xsl:when test="/page/question/@type = 'scale'">
										<input type="radio" name="type" value="scale" checked="true"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="radio" name="type" value="scale"/>
									</xsl:otherwise>
								</xsl:choose>
								<b>Échelle</b><br/>
								<input type="text" name="minLabel" size="8" value="{/page/question/@minLabel}"/>
								<input type="radio" name="scaleValue" value="1"/>
								<input type="radio" name="scaleValue" value="2"/>
								<input type="radio" name="scaleValue" value="3"/>
								<input type="radio" name="scaleValue" value="4"/>
								<input type="radio" name="scaleValue" value="5"/>
								<input type="text" name="maxLabel" size="8" value="{/page/question/@maxLabel}"/>
								<br/><br/>

								<xsl:choose>
									<xsl:when test="/page/question/@type = 'multichoice'">
										<input type="radio" name="type" value="multichoice" checked="true"/>
									</xsl:when>
									<xsl:otherwise>
										<input type="radio" name="type" value="multichoice"/>
									</xsl:otherwise>
								</xsl:choose>
								<b>Choix multiple</b><br/>
								Exemple: <br/>
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
											<input type="text" name="choice1" size="40" value="{/page/question/@choice1}"/>
										</td>
										<td>
											2:
											<input type="text" name="choice2" size="40" value="{/page/question/@choice2}"/>
										</td>
									</tr>
									<tr>
										<td>
											3:
											<input type="text" name="choice3" size="40" value="{/page/question/@choice3}"/>
										</td>
										<td>
											4:
											<input type="text" name="choice4" size="40" value="{/page/question/@choice4}"/>
										</td>
									</tr>
									<tr>
										<td>
											5:
											<input type="text" name="choice5" size="40" value="{/page/question/@choice5}"/>
										</td>
										<td>
											6:
											<input type="text" name="choice6" size="40" value="{/page/question/@choice6}"/>
										</td>
									</tr>
									<tr>
										<td>
											7:
											<input type="text" name="choice7" size="40" value="{/page/question/@choice7}"/>
										</td>
										<td>
											8:
											<input type="text" name="choice8" size="40" value="{/page/question/@choice8}"/>
										</td>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>
						
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
								<table>
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
