<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%">
			<tr>
				<td>
					<table align="right">
						<tr>
							<td align="right">
								<center>
									<xsl:if test="item/brand/@id > 0">
										<a href="#" onClick="window.open('{item/brand/@href}','', '');">
											<img src="/images/brand?id={item/brand/@id}" border="0"/>
										</a>
										<br/>
										<br/>
									</xsl:if>
								</center>
								<xsl:if test="item/@hasImage = 'true'">
									<img src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
							</td>
						</tr>
					</table>

					<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					<font size="+2"><b><xsl:value-of select="item/@title"/></b></font>

					<table>
						<tr> 
							<td>Type:</td>
							<td>
								<xsl:choose>
									<xsl:when test="item/@type = 'sale'"> 
										À vendre
									</xsl:when>
									<xsl:when test="item/@type = 'rental'">
										À louer
									</xsl:when>
									<xsl:when test="item/@type = 'free'">
										À donner
									</xsl:when>
									<xsl:when test="item/@type = 'search'">
										Recherche
									</xsl:when>
									<xsl:when test="item/@type = 'publish'">
										Publier
									</xsl:when>
									<xsl:when test="item/@type = 'offer'">
										Offre
									</xsl:when>
									<xsl:otherwise>
										-
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>	
						<tr>
							<td>Nature:</td>
							<td valign="top">
								<xsl:choose>
									<xsl:when test="item/@nature = 'item'"> 
										Article
									</xsl:when>
									<xsl:when test="item/@nature = 'service'">
										Service
									</xsl:when>
									<xsl:when test="item/@nature = 'job'">
										Emploi
									</xsl:when>
									<xsl:when test="item/@nature = 'event'">
										Évennement
									</xsl:when>
									<xsl:when test="item/@nature = 'document'">
										Document
									</xsl:when>
									<xsl:when test="item/@nature = 'photo'">
										Photo
									</xsl:when>
									<xsl:when test="item/@nature = 'information'">
										Information
									</xsl:when>
									<xsl:otherwise>
										-
									</xsl:otherwise>
								</xsl:choose>
							</td>										
						</tr>

						<xsl:if test="item/@price > 0">
							<tr> 
								<td>Prix:</td>
								<td>
									<xsl:value-of select="format-number(item/@price, '#0.00')"/>$
									<xsl:if test="item/@negociable = 'true'">
										(négociable)
									</xsl:if>
								</td>
							</tr>
						</xsl:if>

						<xsl:choose>
							<xsl:when test="item/age/@years > 0">
								<tr>
									<td>Possède depuis:</td>
									<td>
										<xsl:value-of select="item/age/@years"/>
										<xsl:choose>
											<xsl:when test="item/age/@years = 1">
												an</xsl:when>
											<xsl:otherwise>
												ans</xsl:otherwise>
										</xsl:choose>
										<xsl:if test="item/age/@months > 0">,
											<xsl:value-of select="item/age/@months"/> mois
										</xsl:if>
									</td>
								</tr>
							</xsl:when>
							<xsl:when test="item/age/@months > 0">
								<tr>
									<td>Possède depuis:</td>
									<td>
										<xsl:value-of select="item/age/@months"/> mois
									</td>
								</tr>
							</xsl:when>
						</xsl:choose>
						
						<xsl:if test="item/@quantity">
							<tr><td>Quantité:</td><td><xsl:value-of select="item/@quantity"/></td></tr>	
						</xsl:if>
						
						<xsl:if test="item/@userbrand">
							<tr>
								<td>Marque:</td>
								<td><xsl:value-of select="item/@userbrand"/></td>
							</tr>
						</xsl:if>

						<xsl:if test="string-length(item/user/@name) > 0">
							<tr><td>Nom:</td><td><xsl:value-of select="item/user/@name"/></td></tr>
						</xsl:if>

						<tr>
							<td>Endroit:</td>
							<td><xsl:value-of select="item/city/@name"/></td>
						</tr>	

						<xsl:if test="string-length(item/user/@email) > 0">
							<tr> 
								<td>Courriel:</td>
								<td>
									<a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									</a>
								</td>
							</tr>
						</xsl:if>
						
						<xsl:if test="string-length(item/user/@telephone) > 0">
							<tr> 
								<td>Téléphone:</td>
								<td><xsl:value-of select="item/user/@telephone"/></td>
							</tr>
						</xsl:if>

						<xsl:if test="item/@href">
							<tr>
								<td valign="top">URL:</td>
								<td>
									<a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a>
								</td>
							</tr>
						</xsl:if>

						<tr> 
							<td height="5"><img src="/images/spacer.gif" alt="" width="110" height="1"/></td>
							<td></td>
						</tr>
					</table>

					<xsl:choose>
						<xsl:when test="item/description = 'null'"/>
						<xsl:when test="item/description">
							<font size="+1"><b>Description</b></font><br/>
							<xsl:copy-of select="item/description/" />
						</xsl:when>
					</xsl:choose>

					<xsl:if test="/page/@state = 'preview'">
						<br/>
						<br/>
						<center>
							<table>
								<tr>
									<td>
										<form method="post" action="/{/page/@section}/confirmItem">
											<input type="submit" value="Confirmer"/>
										</form>
									</td>
									<td>
										<form method="post" action="/{/page/@section}/modifyItem">
											<input type="submit" value="Modifier"/>
										</form>
									</td>
									<td>
										<form method="post" action="/{/page/@section}/cancelItem">
											<input type="submit" value="Détruire"/>
										</form>
									</td>
								</tr>
							</table>
						</center>
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
