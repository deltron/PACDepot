<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="body">
		<table width="100%" cellspacing="12">
			<tr>
				<td width="25%" valign="top">

					<xsl:call-template name="title">
						<xsl:with-param name="name">Profile</xsl:with-param>
						<xsl:with-param name="text">Cette section est r�serv�e aux administrateurs du site Internet.  C'est ici que vous pouvez g�rer le contenu de votre page d'accueil</xsl:with-param>
					</xsl:call-template>
		
		</td>
				
		<td width="75%" align="top">
					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Banni�res horizontales
						</xsl:with-param>
						<xsl:with-param name="body">
							<form method="post" enctype="multipart/form-data" action="/members/createAd">
								<b>Ajouter une nouvelle banniere</b><br/>
								Nom de fichier<br/>
								<input type="file" name="ad"/>
								<br/><br/>
										
								URL<br/>
								<input type="text" name="href"/>
								<br/><br/>
								
								<input type="submit" value="T�l�charger"/>
							</form>
						</xsl:with-param>
					</xsl:call-template>
					
					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Les banni�res d'annonce existantes				
						</xsl:with-param>
					<xsl:with-param name="body">
						<form method="post" action="/members/deleteAds">
							<xsl:for-each select="ads/ad">
								<input type="checkbox" name="delete" value="{@id}"/>
									<xsl:choose>
										<xsl:when test="string-length(@href)">
											<a href="{@href}"><xsl:value-of select="@href"/></a>
										</xsl:when>
										<xsl:otherwise>
											(aucun URL associ� � cet annonce)
										</xsl:otherwise>
									</xsl:choose>
									<br/>
									<xsl:value-of select="$spacer"/>
									<img src="/images/ad?id={@id}" width="{@width}" height="{@height}"/>
									<br/><br/>
								</xsl:for-each>
								<br/>
								<input type="submit" value="Effacer s�lection"/>
							</form>
						</xsl:with-param>
					</xsl:call-template>


					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Logo du promotteur
						</xsl:with-param>
						<xsl:with-param name="body">
							<b>Votre logo � pr�sent:</b><br/>
							<img src="/images/banner?id={/page/sponsor/banner/@id}" width="{../sponsor/banner/@width}" 
								height="{../sponsor/banner/@height}"/>
							<br/>
							<form method="post" enctype="multipart/form-data" action="/members/updateBanner">
								<b>Ajouter ou modifier le logo</b><br/>
								Le logo peut avoir une taille maximale de 1024x1024 pixels et 256KB.<br/>
								Veuillez notez que si vous t�l�charger un nouveau logo, le vieux sera �craser.<br/>
								<input type="file" name="banner"/><br/>
								<input type="submit" value="T�l�charger"/>
							</form>
						</xsl:with-param>
					</xsl:call-template>


				
					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Ic�ne du promotteur
						</xsl:with-param>
						<xsl:with-param name="body">
							<b>Votre icone � pr�sent:</b><br/>
							<img src="/images/icon?id={../sponsor/icon/@id}" width="{../sponsor/icon/@width}" 
								height="{../sponsor/icon/@height}"/>	
							<br/>
							<form method="post" enctype="multipart/form-data" action="/members/updateIcon">
								<b>Ajouter ou modifier l'icone</b><br/>
								Veuillez notez que si vous t�l�charger un nouveau icone, le vieux sera �craser.<br/>
								<input type="file" name="icon"/><br/>
								<input type="submit" value="T�l�charger"/>
							</form>
						</xsl:with-param>
					</xsl:call-template>
						
					<xsl:call-template name="subsection">
						<xsl:with-param name="title">
							Page d'accueil
						</xsl:with-param>
						<xsl:with-param name="body">
							<form method="post" action="/members/updateHomepage">
								<xsl:if test="../sponsor/errors/homepage">
									<font color="red">
										<b> (ne sera pas sauvegarder parce que votre HTML contient des erreurs de syntax) </b>
									</font>
								</xsl:if>
								<textarea name="homepage" rows="15" cols="96" class="input">
									<xsl:choose>
										<xsl:when test="../sponsor/errors/homepage">  
											<xsl:copy-of select="../sponsor/homepageStr/node()"/>
										</xsl:when>
										<xsl:otherwise>
											<xsl:copy-of select="../sponsor/homepageStr/node()"/>
										</xsl:otherwise>
									</xsl:choose>
								</textarea>
								<br/>
								<input type="submit" value="Mettre � jour"/>
							</form>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
