<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="body">
		<xsl:for-each select="category"> 
			<xsl:if test="((position()-1) mod 6) = 0">
				<xsl:apply-templates select="." mode="toc">
					<xsl:with-param name="categories" select="following-sibling::category"/>
				</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>
		<xsl:apply-templates select="items"/>
		<xsl:apply-templates select="searchResultNavigator"/>
	</xsl:template>

	<xsl:template match="category" mode="toc">
		<xsl:param name="categories"/>
		<table width="100%" class="text">
			<xsl:choose>
				<xsl:when test="/page/@state = 'subcategory'">
					<td valign="top">
						<xsl:apply-templates select="." mode="fill"/>
					</td>
				</xsl:when>
				<xsl:otherwise>
					<tr> 
						<td valign="top">
							<xsl:apply-templates select="." mode="fill"/>
						</td>
						<td valign="top">
							<xsl:apply-templates select="$categories[1]" mode="fill"/>
						</td>
						<td valign="top">
							<xsl:apply-templates select="$categories[2]" mode="fill"/>
						</td>
						<td valign="top">
							<xsl:apply-templates select="$categories[3]" mode="fill"/>
						</td>
						<td valign="top">
							<xsl:apply-templates select="$categories[4]" mode="fill"/>
						</td>
						<td valign="top">
							<xsl:apply-templates select="$categories[5]" mode="fill"/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</table>
	</xsl:template>

	<xsl:template match="ancestor" mode="recurse">
		<xsl:if test="@name">
					<a href="/browse/category?id={@id}"> 		
						<xsl:value-of select="@name"/> 
					</a> 
					: 
		</xsl:if>
		<xsl:if test="ancestor">
			<xsl:apply-templates select="ancestor" mode="recurse"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="category" mode="sub">
		<a href="/browse/category?id={@id}"><xsl:value-of select="@name"/></a>
		&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;
	</xsl:template>
	

	<xsl:template match="category" mode="fill">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr> 
				<td>	
					<!-- 
							Example de resultat: 
							
								_Annonces_ : _Immobilier_ : Maison 
					-->
					<xsl:choose>
						<xsl:when test="/page/@state = 'subcategory'">
							<xsl:apply-templates select="/page/body/ancestor" mode="recurse"/>
								<b>
									<xsl:value-of select="@name"/>
								</b>
							</xsl:when>
							<xsl:otherwise>
									<b>
										<a href="/browse/category?id={@id}">
											<xsl:value-of select="@name"/>
										</a>
									</b>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
				<tr> 
					<td>
						<img src="/images/spacer.gif" width="125" height="1"/>
				</td> 
			</tr>
			<tr> 
				<td>
					<xsl:choose>
						<xsl:when test="/page/@state = 'subcategory'">
							<xsl:apply-templates select="category" mode="sub"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:apply-templates select="category">
								<xsl:with-param name="level" select="0"/>
							</xsl:apply-templates>
						</xsl:otherwise>
					</xsl:choose>
				</td> 
			</tr>
		</table> 
	</xsl:template>
</xsl:stylesheet>
