<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	
	<xsl:template match="page/body">
		<table width="100%">
			<tr>
      	<td width="25%" valign="top">
        	<xsl:call-template name="title">
          	<xsl:with-param name="name">Ajout</xsl:with-param>
            <xsl:with-param name="text">
Il nous fait plaisir de vous donner la possibilit� d'ajouter vous-m�me votre annonce. Pri�re de compl�ter le formulaire en suivant les instructions qui vous sont donn�es.  une fois que vous aurez confirm� votre annonce, le syst�me vous retournera un message comme quoi la mise � jour est effectu�e. Pour des raisons d'�thique, un pr�pos� fera une validation de votre annonce et la rendra publique � tous dans les heures qui suivent.
            </xsl:with-param>
         </xsl:call-template>
       </td>
       <td width="75%" valign="top">
					<!-- message d'erreur -->
						<font color="#FF0000"><b>
						<xsl:choose>
							<xsl:when test="errors/*">
								<font size="+1"><b>Corriger les champs avec un</b><font size="+2">*</font></font>
								<xsl:choose>
									<xsl:when test="errors/size">
										<br/><li>votre image d�passe la taille maximum de 128kb</li>
									</xsl:when>
									<xsl:when test="errors/photo">
										<br/><li>ce format de fichier n'est pas support� (seulement JPG et GIF sont accept�s)</li>
									</xsl:when>
									<xsl:when test="errors/html">
										<br/><li>v�rifier le syntax HTML de la description</li>
									</xsl:when>
								</xsl:choose>
							</xsl:when>
							<xsl:when test="/page/@state = 'cancelled'">
								Votre annonce n'a pas �t� ajout�e, vous pouvez recommencer maintenant ou 
								<br/>simplement choisir une autre option du menu principal.
							</xsl:when>
							<xsl:when test="/page/@state = 'success'">
								Mise-�-jour effectu�e avec succ�s.
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
								<xsl:if test="errors/nature">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Type</b><br/>
								<select name="type" class="text">
									<xsl:choose>
										<xsl:when test="item/@type = 'sale'">
											<option value="sale" selected="true">
												� vendre
											</option>
										</xsl:when>
										<xsl:otherwise>
											<option value="sale">
												� vendre	
											</option>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="item/@type = 'rental'">
											<option value="rental" selected="true">
												� louer
											</option>
										</xsl:when>
										<xsl:otherwise>
											<option value="rental">
												� louer
											</option>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="item/@type = 'free'">
											<option value="free" selected="true">
												� donner
											</option>
										</xsl:when>
										<xsl:otherwise>
											<option value="free">
												� donner
											</option>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="item/@type = 'search'">
											<option value="search" selected="true">
												Recherche
											</option>
										</xsl:when>
										<xsl:otherwise>
											<option value="search">
												Recherche
											</option>
										</xsl:otherwise>
									</xsl:choose>
									<xsl:choose>
										<xsl:when test="item/@type = 'offer'">
											<option value="offer" selected="true">
												Offre
											</option>
										</xsl:when>
										<xsl:otherwise>
											<option value="offer">
												Offre
											</option>
										</xsl:otherwise>
									</xsl:choose>
								</select>
								<br/><br/>

								<xsl:if test="errors/title"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Titre</b><br/>
								Entrer le titre de votre item jusqu'� un maximum de 60 charact�res.<br/>
								<input name="title" type="text" 
									size="61" maxlength="60" class="input" value="{item/@title}"/>
								<br/><br/>

								<xsl:if test="errors/description"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Description</b><br/>
								(maximum de 10000 caract�res)<br/>
								<textarea name="description" rows="7" cols="60" class="input">
									<xsl:value-of select="item/descriptionStr/node()"/>
								</textarea>
								<br/><br/>


								<table width="90%">
									<tr>
										<td>
											<xsl:if test="errors/price">
												<font color="#FF0000" size="+2"><b>*</b></font>
											</xsl:if>
											<b>Prix</b> <br/>
											Exemple: 12345.00<br/>
											<xsl:choose>
												<xsl:when test="item/@price > 0">
													<input name="price" type="text" size="5" 
														value="{format-number(item/@price, '#0.00')}"/>
												</xsl:when>
												<xsl:otherwise>
													<input name="price" type="text" 
														size="5" maxlength="10" value="{item/@price}"/>
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td>
											<xsl:if test="errors/quantity">
												<font color="#FF0000" size="+2"><b>*</b></font>
											</xsl:if>
											<b>Quantit�</b><br/>
											<input name="quantity" type="text" size="5" maxlength="10" value="{item/@quantity}"/>
										</td>
									</tr>
									<tr>
										<td>
											<xsl:if test="errors/age">
												<font color="#FF0000" size="+2"><b>*</b></font>
											</xsl:if>
											<b>Poss�de depuis</b><br/>
											<xsl:choose>
												<xsl:when test="errors/ageYY">
													<input name="ageYears" type="text" 
														size="5" maxlength="4" value="{item/@ageYY}"/> ans, 
												</xsl:when>
												<xsl:otherwise>
													<input name="ageYears" type="text" 
														size="5" maxlength="4" value="{item/age/@years}"/> ans, 
												</xsl:otherwise>
											</xsl:choose>
											<xsl:choose>
												<xsl:when test="errors/ageMM">
													<input name="ageMonths" type="text" 
														size="5" maxlength="4" value="{item/@ageMM}"/> mois
												</xsl:when>
												<xsl:otherwise>
													<input name="ageMonths" type="text" 
														size="5" maxlength="4" value="{item/age/@months}"/> mois
												</xsl:otherwise>
											</xsl:choose>
										</td>
										<td>
											<xsl:if test="errors/lifespan">
												<font color="#FF0000" size="+2"><b>*</b></font>
											</xsl:if>
											<b>D�truire l'annonce apr�s</b><br/>
											<xsl:choose>
												<xsl:when test="item/@lifespan">
													<input name="lifespan" type="text" size="5" maxlength="4" value="{item/@lifespan}"/>
												</xsl:when>
												<xsl:otherwise>
													<input name="lifespan" type="text" size="5" maxlength="4" value="90"/>
												</xsl:otherwise>
											</xsl:choose>
											jours
										</td>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>

						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Coordonn�es de la personne � contacter
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="errors/userName"> 
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Nom</b><br/>
								(maximum de 40 charact�res)<br/>
								<input name="userName" type="text" size="40" 
										maxlength="40" class="input" value="{item/user/@name}"/>
								<br/><br/>

								<xsl:if test="errors/userTelephone">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>T�l�phone</b><br/>
								Exemple: (514) 555-1212 poste 555<br/>
								<input name="userTelephone" type="text" 
									size="40" maxlength="40" class="input" value="{item/user/@telephone}" />
								<br/><br/>

								<xsl:if test="errors/userEmail">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>Courriel</b> <br/>
								Exemple: info@pacdepot.com<br/>
								<input name="userEmail" type="text" 
										size="40" maxlength="40" class="input" value="{item/user/@email}" />
								<br/><br/>
								
								<xsl:if test="errors/href">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								<b>URL</b> <br/>
								Exemple: http://www.pacdepot.com/ <br/>
								<input name="href" type="text" size="40" 
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
								Cat�gorie
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="errors/categoryid">
									<font color="#FF0000" size="+2"><b>*</b></font>
								</xsl:if>
								Votre PAC sera ajout�e dans la cat�gorie suivante. <br/>
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
								Le lieu suivant sera utilis� pour localiser g�ographiquement votre PAC. NOTE : Ceci n'emp�chera pas les gens des autres localit�s de voir votre PAC. Elle pourra �tre vue par TOUT le monde, peu importe le lieu que vous avez choisi. <br/>
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
								Marque et r�f�rence
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
								autre, pr�cisez: <br/>
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
