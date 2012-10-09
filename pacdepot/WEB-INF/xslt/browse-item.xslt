<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%">
			<tr>
				<xsl:if test="(item/@hasImage = 'true') or (item/brand/@id > 2)">
					<td width="33%" valign="top">
						<center>
							<xsl:if test="item/brand/@id > 0">
								<a href="#" onClick="window.open('{item/brand/@href}','', '');">
									<img src="/images/brand?id={item/brand/@id}" border="0"/>
								</a>
								<br/>
							</xsl:if>
							<xsl:if test="item/@hasImage = 'true'">
								<img src="/images/item?id={item/@id}" border="0"/>
							</xsl:if>
						</center>
					</td>
				</xsl:if>
				<td width="67%" valign="top">
					<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					<font size="+2"><b><xsl:value-of select="item/@title"/></b></font><br/>
					<br/>
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
						<xsl:when test="item/@type = 'offer'">
							Offre
						</xsl:when>
						<xsl:when test="item/@type = 'contest'">
							Concours
						</xsl:when>
						<xsl:when test="item/@type = 'survey'">
							Sondage
						</xsl:when>
						<xsl:when test="item/@type = 'photo'">
							Photo
						</xsl:when>
						<xsl:when test="item/@type = 'coupon'">
							Coupon
						</xsl:when>
						<xsl:when test="item/@type = 'repertory'">
							Répertoire
						</xsl:when>
						<xsl:when test="item/@type = 'publication'">
							Publication
						</xsl:when>
						<xsl:otherwise>
							-
						</xsl:otherwise>
					</xsl:choose>
					<br/>

					<xsl:if test="item/@price > 0">
						Prix:
						<xsl:value-of select="format-number(item/@price, '#0.00')"/>$
						<xsl:if test="item/@negociable = 'true'">
							(négociable)
						</xsl:if>
						<br/>
					</xsl:if>

					<xsl:choose>
						<xsl:when test="item/age/@years > 0">
							Possède depuis:
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
							<br/>
						</xsl:when>
						<xsl:when test="item/age/@months > 0">
							Possède depuis:
							<xsl:value-of select="item/age/@months"/> mois
							<br/>
						</xsl:when>
					</xsl:choose>
						
					<xsl:if test="item/@quantity">
						Quantité:
						<xsl:value-of select="item/@quantity"/>
						<br/>
					</xsl:if>
						
					<xsl:if test="item/@userbrand">
						Marque:
						<xsl:value-of select="item/@userbrand"/>
						<br/>
					</xsl:if>

					<xsl:if test="string-length(item/user/@name) > 0">
						Nom:
						<xsl:value-of select="item/user/@name"/>
						<br/>
					</xsl:if>

					Endroit:
					<xsl:value-of select="item/city/@name"/>
					<br/>

					<xsl:if test="string-length(item/user/@email) > 0">
						Courriel:
						<a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
							<xsl:value-of select="item/user/@email"/>
						</a>
						<br/>
					</xsl:if>
						
					<xsl:if test="string-length(item/user/@telephone) > 0">
						Téléphone:
						<xsl:value-of select="item/user/@telephone"/>
						<br/>
					</xsl:if>

					<xsl:if test="item/@href">
						URL:
						<a href="{item/@href}">
							<xsl:value-of select="item/@href"/>
						</a>
						<br/>
					</xsl:if>

					<br/>
					<xsl:choose>
						<xsl:when test="item/description = 'null'"/>
						<xsl:when test="item/description">
							<font size="+1"><b>Description</b></font><br/>
							<xsl:copy-of select="item/description/" />
						</xsl:when>
					</xsl:choose>

					<xsl:if test="/page/@state = 'preview'">
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
								<table>
									<tr>
										<xsl:choose>
											<xsl:when test="/page/@section = 'add'">
												<form method="post" action="/{/page/@section}/confirmItem">
													<td>
														<input type="submit" value="Confirmer"/>
													</td>	
												</form>
											</xsl:when>
											<xsl:when test="/page/@section = 'members'">
												<xsl:if test="/page/user/@group = 'administrator'">
													<form method="post" action="/{/page/@section}/confirmItem">
														<td>
															<input type="submit" value="Confirmer"/>
														</td>	
													</form>
												</xsl:if>
											</xsl:when>
										</xsl:choose>
										<form method="post" action="/{/page/@section}/modifyItem">
											<td>
												<input type="submit" value="Modifier"/>
											</td>
										</form>
										<form method="post" action="/{/page/@section}/cancelItem">
											<td>
												<input type="submit" value="Détruire"/>
											</td>
										</form>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>
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
