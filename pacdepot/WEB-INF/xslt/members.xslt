<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="body">
		<table width="100%">
			<tr>
				<td width="25%" valign="top">
					<xsl:call-template name="title">
						<xsl:with-param name="name">
							Membres
						</xsl:with-param>
						<xsl:with-param name="text">
							Cette section est réservée aux membres enregistrés et aux administrateurs du site Internet
						</xsl:with-param>
					</xsl:call-template>
				</td>
				<td width="75%" align="top">
					<form method="post" action="/members/login">
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Identification
							</xsl:with-param>
							<xsl:with-param name="body">
								<xsl:if test="/page/@state = 'error'">
									<font color="#FF0000"><b>Identification incorrecte, essayez de nouveau.</b> </font><br/><br/>
								</xsl:if>
									<b>Identification</b><br/>
									<input type="text" size="16" name="username"/>
									<br/><br/>
									
									<b>Mot de passe</b><br/>
									<input type="password" size="16" name="password"/>
									<br/><br/>


							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="inputsection">
							<xsl:with-param name="body">
								<input type="submit" value="Continuer"/>
							</xsl:with-param>
						</xsl:call-template>
					</form>
				</td>
			</tr>
		</table>
	</xsl:template>
</xsl:stylesheet>
