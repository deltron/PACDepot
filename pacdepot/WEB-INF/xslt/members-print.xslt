<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>

	<xsl:template match="page">
		<xsl:apply-templates select="body/items"/>
	</xsl:template>
	<xsl:template match="category">
		<xsl:value-of select="@name"/> :
		<xsl:apply-templates select="category"/>
	</xsl:template>

	<xsl:template match="items">
		<xsl:for-each select="item">
			<xsl:apply-templates select="." mode="print"/>
			<xsl:if test="(position()) mod 2 = 0">
				<p style="page-break-after:always"/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>
				
	<xsl:template match="item" mode="print">
		<table>
			<tr>
				<td>
					<div style="border-width: 1px; border-style: solid; padding: 5px 5px 5px 5px; width: 550px; height: 408px; font-size:10pt">
						<center>
							<table>
								<tr>
									<center>
										<img src="/images/banner?id={sponsor/@id}"/>
									</center>
								</tr>
							</table>
						</center>
						<font size="+2"><b><xsl:value-of select="@title"/></b></font>
						<br/>
						<xsl:apply-templates select="category"/>
						Annonce #<xsl:value-of select="@id"/>
						<table width="100%">
							<tr>
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
										<xsl:when test="@type = 'publish'">
											Publier
										</xsl:when>
										<xsl:when test="@type = 'offer'">
											Offre
										</xsl:when>
										<xsl:otherwise>
											-
										</xsl:otherwise>
									</xsl:choose>
									/
									<xsl:choose>
										<xsl:when test="@nature = 'item'"> 
											Article
										</xsl:when>
										<xsl:when test="@nature = 'service'">
											Service
										</xsl:when>
										<xsl:when test="@nature = 'job'">
											Emploi
										</xsl:when>
										<xsl:when test="@nature = 'event'">
											Évennement
										</xsl:when>
										<xsl:when test="@nature = 'document'">
											Document
										</xsl:when>
										<xsl:when test="@nature = 'photo'">
											Photo
										</xsl:when>
										<xsl:when test="@nature = 'information'">
											Information
										</xsl:when>
										<xsl:otherwise>
											-
										</xsl:otherwise>
									</xsl:choose>
									<br/>
									<xsl:if test="@price > 0">
										<xsl:value-of select="format-number(@price, '#0.00')"/>$
											<xsl:if test="@negociable = 'true'">
												(négociable)
											</xsl:if>
											<br/>
									</xsl:if>
									<xsl:value-of select="city/@name"/>
								</td>
								<td align="right">
									<xsl:if test="@hasImage = 'true'">
										<img src="/images/item?id={@id}" border="0"/>
									</xsl:if>
								</td>
							</tr>
						</table>
					</div>
				</td>
				<td>
					<table>
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0" style="font-size:7pt">
									<tr>
										<td>
											<div style="border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; border-top-style: dashed; 
												padding: 5px 5px 5px 5px; height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px;">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
									<tr>
										<td>
											<div style=" border-width: 1px; border-left-style: dashed; 
												border-bottom-style: dashed; padding: 5px 5px 5px 5px; 
												height:51px; width: 200px; ">
												<b><xsl:value-of select="user/@name"/></b>
												<br/>
												<xsl:value-of select="user/@telephone"/>
												<br/>
												<xsl:value-of select="user/@email"/>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
			<div style="position:relative; left:-210px; paddin-bottom:5px; 
				bottom:-18pt; top:-18pt; font-size:8pt;  font-family: Verdana, Arial, Helvetica, sans-serif;">

				<center>
					&#169; 2002 - www.pacdepot.com
				</center>
			</div>
	</xsl:template>
</xsl:stylesheet>
