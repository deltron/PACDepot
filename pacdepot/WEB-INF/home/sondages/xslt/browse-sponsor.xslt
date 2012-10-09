<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:template match="page/body">
		<xsl:apply-templates select="items"/>
		<xsl:apply-templates select="searchResultNavigator"/>
	</xsl:template>
</xsl:stylesheet>
