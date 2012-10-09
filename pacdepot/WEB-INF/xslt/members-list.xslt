<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="body">
		<table width="100%" cellpadding="0" cellspacing="0">
			<form method="post" action="/members/select">
				<tr>
					<td>
						<xsl:apply-templates select="items"/>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
								<table>
									<tr>
										<td>
											<input name="action" type="radio" value="deleteItems"/>Détruire
										</td>
										<td>
											<input name="action" type="radio" value="printItems"/>Imprimer
										</td>
										<td>
											<input type="submit" value="Executer"/>
										</td>
									</tr>
								</table>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
				<tr>
					<td>
						<xsl:apply-templates select="searchResultNavigator">
							<xsl:with-param name="urlPrefix" select="'/members/list'"/>
						</xsl:apply-templates>
					</td>
				</tr>
			</form>
		</table>
	</xsl:template>

	<xsl:template match="items">
			<table border="0" width="100%" style="border : 1px; border-color : #CCCCCC; 
				border-collapse: collapse; border-spacing: 0px;" bgcolor="#CCCCCC">
				<tr class="tableHeader"> 
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 

					</td> <!-- Confirmed -->
					<td width="90" style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						No. 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Date 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Type 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Titre 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Contact 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
					</td> <!-- checkbox -->
				</tr>
				<xsl:choose>
					<xsl:when test="item">
						<xsl:for-each select="item">
							<tr bgcolor="#FFFFFF" class="text"> 
								<td valign="top">
									<xsl:if test="@confirmed = 0">
										<font color="#FF0000"><b>C</b></font>
									</xsl:if>
								</td>
								<td valign="top"><a href="/members/previewItem?id={@id}"><xsl:value-of select="@id"/></a></td>
								<td valign="top"><xsl:value-of select="@date"/></td>
								<td valign="top">
									<xsl:choose>
										<xsl:when test="@type = 'sale'">
											À vendre
										</xsl:when>
										<xsl:when test="@type = 'rental'">
											À louer
										</xsl:when>
										<xsl:when test="@type = 'free'">
											À donner
										</xsl:when>
										<xsl:when test="@type = 'search'">
											Recherche
										</xsl:when>
										<xsl:when test="@type = 'offer'">
											Offre
										</xsl:when>
										<xsl:when test="@type = 'contest'">
											Concours
										</xsl:when>
										<xsl:when test="@type = 'survey'">
											Sondage
										</xsl:when>
										<xsl:when test="@type = 'photo'">
											Photo
										</xsl:when>
										<xsl:when test="@type = 'coupon'">
											Coupon
										</xsl:when>
										<xsl:when test="@type = 'repertory'">
											Répertoire
										</xsl:when>
										<xsl:when test="@type = 'publication'">
											Publication
										</xsl:when>
										<xsl:otherwise>
											-
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td valign="top">
									<xsl:choose>
										<xsl:when test="@title">
											<a href="/members/previewItem?id={@id}">
												<xsl:value-of select="substring(@title, 0, 72)"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											-
										</xsl:otherwise>
									</xsl:choose>
								</td>
								<td valign="top">
									<xsl:choose>
										<xsl:when test="string-length(user/@email) > 0">
											<a href="mailto:{user/@email}?subject={@title} (#{@id})">
												<xsl:value-of select="user/@email"/>
											</a>
										</xsl:when>
										<xsl:otherwise>
											-
										</xsl:otherwise>
									</xsl:choose>
								</td>	
								<td>
									<input type="checkbox" name="selection" value="{@id}"/>
								</td>
							</tr>	
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td colspan="7" bgcolor="#FFFFFF" class="text">Aucune annonce.</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</table>
	</xsl:template>
</xsl:stylesheet>
