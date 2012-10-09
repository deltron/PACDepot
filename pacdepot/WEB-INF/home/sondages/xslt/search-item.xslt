<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="body">
		<xsl:apply-templates select="items"/>
		<xsl:apply-templates select="searchResultNavigator">
			<xsl:with-param name="urlPrefix" select="'/search/item'"/>
		</xsl:apply-templates>
	</xsl:template>
</xsl:stylesheet>
