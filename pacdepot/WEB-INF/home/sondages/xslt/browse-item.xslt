<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%">
			<tr>
				<xsl:if test="(item/@hasImage = 'true') or (item/brand/@id > 2)">
					<td width="33%" valign="top" rowspan="2">
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
					<xsl:apply-templates select="item/category"/> #<xsl:value-of select="item/@id"/><br/><br/>
					<font size="+2"><b><xsl:value-of select="item/@title"/></b></font><br/>
					<a href="{item/@href}"><xsl:value-of select="item/@href"/></a>
					<xsl:choose>
						<xsl:when test="item/description = 'null'"/>
						<xsl:when test="item/description">
							<br/><br/>
							<xsl:copy-of select="item/description/" />
						</xsl:when>
					</xsl:choose>
				</td>
			</tr>
			<tr>
				<td valign="bottom">
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
							<xsl:choose>
								<xsl:when test="/page/@state = 'preview'">
									<table cellpadding="0" cellspacing="0">
										<tr>
											<form method="post" action="/{/page/@section}/confirmItem">
												<td>
													<input type="submit" value="Confirmer"/>
												</td>
											</form>
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
											<xsl:if test="/page/@section = 'members'">
												<form method="post" action="/{/page/@section}/questionSummary">
													<td>
														<input type="submit" value="Détail"/>
													</td>
												</form>
												<form method="post" action="/{/page/@section}/answers">
													<td>
														<input type="submit" value="Résultats"/>
													</td>
												</form>
												<form method="post" action="/{/page/@section}/retireItem">
													<td>
														<input type="submit" value="Retirer"/>
													</td>
												</form>
											</xsl:if>
										</tr>
									</table>
								</xsl:when>
								<xsl:otherwise>
									<table cellpadding="0" cellspacing="0">
										<form method="post" action="/{/page/@section}/register?id={/page/body/item/@id}">
											<tr>
												<td>
													<input type="submit" value="Continuer &gt;&gt;"/>
												</td>
											</tr>
										</form>
									</table>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
