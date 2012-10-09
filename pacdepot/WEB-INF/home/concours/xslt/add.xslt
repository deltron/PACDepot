<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	
	<xsl:template match="page/body">
		<table width="100%" cellpadding="0" cellspacing="1">
			<tr>
				<td width="33%" valign="top"> <!-- section de gauche -->
					<h1>Concours</h1>
				</td>
				<td width="67%"> <!-- section de droite -->
					
					<!-- message d'erreur -->
					<font color="#FF0000"><b>
						<xsl:choose>
							<xsl:when test="errors/*">
								<font size="+1"><b>Corriger les champs avec un</b><font size="+2">*</font></font>
								<xsl:choose>
									<xsl:when test="errors/size">
										<br/><li>votre image dépasse la taille maximum de 128kb</li>
									</xsl:when>
									<xsl:when test="errors/photo">
										<br/><li>ce format de fichier n'est pas supporté (seulement JPG et GIF sont acceptés)</li>
									</xsl:when>
									<xsl:when test="errors/html">
										<br/><li>vérifier le syntax HTML de la description</li>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="/page/@state = 'cancelled'">
								Votre annonce n'a pas été ajoutée, vous pouvez recommencer maintenant ou 
								<br/>simplement choisir une autre option du menu principal.
							</xsl:when>
							<xsl:when test="/page/@state = 'success'">
								Mise-à-jour effectuée avec succès.
							</xsl:when>
						</xsl:choose>
					</b></font>	
					<!-- fin de message d'erreur -->

					<form method="post" action="/{/page/@section}/previewItem" enctype="multipart/form-data">
						
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Contenu de votre pac
							</xsl:with-param>
							<xsl:with-param name="body">
								<input type="hidden" name="type" value="contest"/>

								<xsl:if test="errors/title"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Titre</b><br/>
								Entrer le titre de votre item jusqu'à un maximum de 60 charactères.<br/>
								<input name="title" type="text" 
									size="61" maxlength="60" class="input" value="{item/@title}"/>
								<br/><br/>

								<xsl:if test="errors/description"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Description</b><br/>
								(maximum de 10000 caractères)<br/>
								<textarea name="description" rows="7" cols="60" class="input">
									<xsl:value-of select="item/descriptionStr/node()"/>
								</textarea>
								<br/><br/>
								
								<xsl:if test="errors/href">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>URL</b> <br/>
								http://www.pacdepot.com/<br/>
								<input name="href" type="text" size="61" 
									maxlength="40" class="input" value="{item/@href}"/>
								<br/><br/>
							</xsl:with-param>
						</xsl:call-template>

						
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Photo
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:choose>
									<xsl:when test="errors/photo">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:when>
									<xsl:when test="errors/size">
										<font color="#FF0000" size="+2"><b>*</b></font>
									</xsl:when>
								</xsl:choose>
								<b>Photo</b><br/>
								<xsl:if test="item/@hasImage = 'true'">
									<img src="/images/item?id={item/@id}" border="0"/> <br/>
									<input type="checkbox" name="deleteImage" value="true"/>Effacer l'image <br/>
								</xsl:if>
								<input type="file" name="photo"/>
							</xsl:with-param>
						</xsl:call-template>

						
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Catégorie
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="errors/categoryid">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								Votre PAC sera ajoutée dans la catégorie suivante. <br/>
								<select name="categoryid" size="5" class="text">
									<xsl:apply-templates select="/page/categories/category">
										<xsl:with-param name="level" select="0"/>
										<xsl:with-param name="type" select="'form'"/>
									</xsl:apply-templates>
								</select>
								<br/><br/>
							</xsl:with-param>
						</xsl:call-template>

						
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Endroit
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="errors/cityid">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								Le lieu suivant sera utilisé pour localiser géographiquement votre PAC. 
								NOTE : Ceci n'empêchera pas les gens des autres localités de voir votre PAC.
								Elle pourra être vue par TOUT le monde, peu importe le lieu que vous avez choisi. <br/>
								<select name="cityid" size="5" class="text">
									<xsl:for-each select="/page/cities/city">
										<xsl:choose>
											<xsl:when test="@id = /page/body/item/city/@id">
												<option value="{@id}" selected="true">
													<xsl:value-of select="@name"/>
												</option>
											</xsl:when>
											<xsl:otherwise>
												<option value="{@id}">
													<xsl:value-of select="@name"/>
												</option>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</select>
								<br/><br/>
							</xsl:with-param>
						</xsl:call-template>

						
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Marque et référence
							</xsl:with-param>
							<xsl:with-param name="body">

								<xsl:if test="errors/brandid">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<select size="5" name="brandid" class="text">
									<xsl:for-each select="/page/brands/brand">
										<xsl:choose>
											<xsl:when test="@id = /page/body/item/brand/@id">
												<option value="{@id}" selected="true">
													<xsl:value-of select="@name"/>
												</option>
											</xsl:when>
											<xsl:otherwise>
												<option value="{@id}">
													<xsl:value-of select="@name"/>
												</option>
											</xsl:otherwise>
										</xsl:choose>
									</xsl:for-each>
								</select> 
								<br/>
								<xsl:if test="errors/userbrand">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								autre, précisez: <br/>
								<input type="text" name="userbrand" value="{item/@userbrand}" size="10"/> 
								<br/><br/>
							</xsl:with-param>
						</xsl:call-template>


						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
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
							</xsl:with-param>
						</xsl:call-template>
					</form>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
