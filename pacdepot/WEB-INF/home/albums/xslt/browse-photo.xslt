<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<center>
			<table width="1%" cellpadding="0" cellspacing="0">
				<tr>
					<td class="title"><xsl:value-of select="item/@title"/></td>
				</tr>
				<xsl:if test="(item/@hasImage = 'true') or (item/brand/@id > 2)">
					<tr>
						<td align="center">
							<xsl:if test="item/brand/@id > 0">
								<a href="#" onClick="window.open('{item/brand/@href}','', '');">
									<img src="/images/brand?id={item/brand/@id}" border="0"/>
								</a>
							</xsl:if>
							<xsl:if test="item/@hasImage = 'true'">
								<img src="/images/item?id={item/@id}" border="0"/>
							</xsl:if>
						</td>
					</tr>
				</xsl:if>
				<tr class="text">
					<td valign="top">
						<xsl:apply-templates select="item/category"/> #<xsl:value-of select="item/@id"/>
						<div align="right">
							<xsl:if test="string-length(item/user/@name) > 0">
								<xsl:value-of select="item/user/@name"/>
								<br/>
							</xsl:if>

							<xsl:value-of select="item/city/@name"/>
							<br/>

								
							<xsl:if test="string-length(item/user/@telephone) > 0">
								<xsl:value-of select="item/user/@telephone"/>
								<br/>
							</xsl:if>
							

							<xsl:if test="string-length(item/user/@email) > 0">
								<a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
									<xsl:value-of select="item/user/@email"/>
								</a>
								<br/>
							</xsl:if>
							<a href="{item/@href}"><xsl:value-of select="item/@href"/></a><br/>

						</div>
						<xsl:choose>
							<xsl:when test="item/description = 'null'"/>
							<xsl:when test="item/description">
								<xsl:copy-of select="item/description/" />
							</xsl:when>
						</xsl:choose>
					</td>
				</tr>
				<xsl:if test="/page/@state = 'preview'">
					<tr>
						<td valign="bottom">
							<xsl:call-template name="inputsection">
								<xsl:with-param name="body">
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
												<form method="post" action="/{/page/@section}/retireItem">
													<td>
														<input type="submit" value="Retirer"/>
													</td>
												</form>
											</xsl:if>
										</tr>
									</table>
								</xsl:with-param>
							</xsl:call-template>
						</td>
					</tr>
				</xsl:if>
			</table>
		</center>
	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
