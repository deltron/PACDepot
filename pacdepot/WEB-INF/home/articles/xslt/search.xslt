<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	
	<xsl:template match="body">
		<table width="100%">
			<tr>
				<td width="25%" valign="top">
					<xsl:call-template name="title">
						<xsl:with-param name="name">Recherche</xsl:with-param>
						<xsl:with-param name="text">Cherchez et vous trouverez</xsl:with-param>
					</xsl:call-template>
				</td>
				<td width="75%" valigh="top">
					<form method="get" action="search/item">
						<xsl:call-template name="subsection">
							<xsl:with-param name="title">
								Critères de recherche
							</xsl:with-param>
							<xsl:with-param name="body">
								<b>Mots-clés</b><br/>
								Ajouter un + pour obligé la présence d'un mot<br/>
								Ajouter un - pour obligé l'absence d'un mot<br/>
								Ajouter un * pour le "wildcard"<br/>
								Ajouter des "" pour une phrase exacte<br/>
								Exemple: +bleu -rouge auto* "une phrase"<br/>
								<input type="text" name="query" size="70"/>
								<br/><br/>

								<b>Endroit</b><br/>
								Vous pouvez sélectionner plusiers endroit en appuyant sur
								CTRL en cliquand sur le nom de ville.<br/>
								<select name="cities" size="5" multiple="true" class="text">
									<option value="all" selected="true">
											(tous)           
									</option>
									<xsl:for-each select="/page/cities/city">
										<option value="{@id}">
											<xsl:value-of select="@name"/>
										</option>
									</xsl:for-each>
								</select>
								<br/><br/>
	
								<b>Catégories</b><br/>
								Vous pouvez sélectionner plusiers catégories en appuyant sur
								CTRL en cliquand sur le nom.<br/>
								<select name="categories" size="5" multiple="true" class="text">
										<option value="all" selected="true">
											(tous)
										</option>
										<xsl:apply-templates select="/page/categories/category">
											<xsl:with-param name="level" select="0"/>
											<xsl:with-param name="type" select="'form'"/>
										</xsl:apply-templates>
									</select>
								</xsl:with-param>
							</xsl:call-template>

							<xsl:call-template name="inputsection">
								<xsl:with-param name="body">
									<input type="submit" value="Rechercher" />
								</xsl:with-param>
							</xsl:call-template>
						</form>
					</td>
				</tr>
			</table>
		</xsl:template>
</xsl:stylesheet>
