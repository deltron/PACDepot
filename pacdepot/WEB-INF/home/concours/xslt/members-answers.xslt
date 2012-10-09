<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%">
			<tr>
				<td width="33%" valign="top">
					Réponse pour item # <a href="/browse/item?id={item/@id}"> <xsl:value-of select="item/@id"/> </a> : <br/>
					<a href="/browse/item?id={item/@id}"> <xsl:value-of select="item/@title"/> </a>
				</td>
				<td width="67%">
					<xsl:for-each select="/page/answerUser">
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Usager #<xsl:value-of select="@id"/>
							</xsl:with-param>
							<xsl:with-param name="body">
								<table width="100%">
									<tr>
										<td width="25%"> Nom: </td>
										<td width="25%"> <xsl:value-of select="@lastname"/> </td>
										<td width="25%"> Prénom: </td>
										<td width="25%"> <xsl:value-of select="@firstname"/> </td>
									</tr>
									<tr>
										<td width="25%"> Téléphone: </td>
										<td width="25%"> <xsl:value-of select="@telephone"/> </td>
										<td width="25%"> Addresse: </td>
										<td width="25%"> <xsl:value-of select="@address"/> </td>
									</tr>
									<tr>
										<td width="25%"> Ville: </td>
										<td width="25%"> <xsl:value-of select="@city"/> </td>
										<td width="25%"> Code postale: </td>
										<td width="25%"> <xsl:value-of select="@postalcode"/> </td>
									</tr>
									<tr>
										<td width="25%"> Pays: </td>
										<td width="25%"> <xsl:value-of select="@country"/> </td>
										<td width="25%"> Province: </td>
										<td width="25%"> <xsl:value-of select="@province"/> </td>
									</tr>
									<tr>
										<td width="25%"> Courriel: </td>
										<td width="25%"> <xsl:value-of select="@email"/> </td>
										<td width="25%"> Sexe: </td>
										<td width="25%"> <xsl:value-of select="@sex"/> </td>
									</tr>
								</table>
								<hr/>
								<xsl:variable name="userid" select="@id"/>
								<xsl:for-each select="/page/answer[@userid = $userid]">
									<xsl:value-of select="@sequence"/>)
									<xsl:choose>
										<xsl:when test="@type = 'open'">
											Réponse ouverte:
											<xsl:value-of select="@openanswer"/>
										</xsl:when>
										<xsl:when test="@type = 'scale'"> 
											Échelle:
											<xsl:value-of select="@scalevalue"/>
										</xsl:when>
										<xsl:when test="@type = 'multichoice'">
											Multichoice:
											<xsl:value-of select="@multichoice"/>
										</xsl:when>
										<xsl:when test="@type = 'none'">
											Pas de question 
										</xsl:when>
									</xsl:choose>
									<br/>
								</xsl:for-each>
							</xsl:with-param>
						</xsl:call-template>
					</xsl:for-each>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
