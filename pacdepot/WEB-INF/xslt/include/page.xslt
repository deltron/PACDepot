<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	<xsl:variable name="spacer">
		<xsl:text> &#160;&#160;&#160;&#160; </xsl:text>
	</xsl:variable>
	
	
	<!-- colors using CSS -->
	<xsl:template match="css">
		<style type="text/css">
			.body { font-family: serif; font-size: 10pt }

			.footer { font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 7pt; color: #333333}

			.input { font-family: Verdana, Arial, Helvetica, sans-serif; }
			.title { font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 18pt; font-weight: bold }
			.text { font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt; }
			
			.tableHeader { font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10pt; font-weight: bold; }
			.menu { 
				text-align: center;
				font-family: Verdana, Arial, Helvetica, sans-serif;
				text-decoration: none;
				font-size: 7pt;
				height: 16px;
			}

			.menu a {
				text-decoration: none;
			}
			
			.menu a:hover {
				text-decoration: underline;
			}
		</style>
	</xsl:template>

	<!-- high-level header layout -->
	<xsl:template match="header">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>	<!-- Logo, ad banner -->
				<td>
					<table border="0" cellspacing="0" cellpadding="0" 
						width="100%" bgcolor="{/page/sponsor/palette/@spacer}">
					  <tr>
						<xsl:apply-templates select="../sponsor/banner"/>
						<xsl:apply-templates select="../sponsor/ad"/>												
					  </tr>
					</table>
				</td>
			</tr>	<!-- /Logo, ad banner -->
			<tr>	<!-- Menu bar -->
				<td>
					<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="{/page/menu/@bgcolor}">
						<tr>
							<td>
								<table border="0" cellpadding="0" cellspacing="0">
									<tr>
										<xsl:apply-templates select="/page/menu/item"/>
									</tr>
								</table>
							</td>
							<td> </td>
						</tr>
					</table>
				</td>
			</tr>	<!-- /Menu bar -->
		</table>
		
	</xsl:template>

	<!-- menu item -->
	<xsl:template match="/page/menu/item">
		<xsl:choose>
			<xsl:when test="/page/@title = @label">
				<td bgcolor="{/page/menu/@selected}" class="menu" width="{@width}"><a href="{@href}"><xsl:choose><xsl:when test="@src"><img src="{@src}" border="0"/></xsl:when><xsl:otherwise><font color="{/page/menu/@text}"><xsl:value-of select="@label"/></font></xsl:otherwise></xsl:choose></a></td>
			</xsl:when>
			<xsl:otherwise>
				<td class="menu" width="{@width}"><a href="{@href}"><xsl:choose><xsl:when test="@src"><img src="{@src}" border="0"/></xsl:when><xsl:otherwise><font color="{/page/menu/@text}"><xsl:value-of select="@label"/></font></xsl:otherwise></xsl:choose></a></td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<!-- sponsor's logo -->
	<xsl:template match="banner">
		<td align="left">
			<a href="#" onClick="window.open('{/page/sponsor/@href}','','');">
				<img src="/images/banner?id={@id}" width="{@width}" height="{@height}" border="0"/>
			</a>
		</td>
	</xsl:template>

	<!-- sponsor's banner ad -->	
	<xsl:template match="ad">
		<td align="right">
			<a href="#" onClick="window.open('{@href}','', '');">
				<img src="/images/ad?id={@id}" width="{@width}" height="{@height}" border="0"/>
			</a>
		</td>
	</xsl:template>
	

	
	<!-- footer -->
	<xsl:template match="footer">
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td width="128"><img src="/images/spacer.gif" width="128" height="1"/></td>
				<td><img src="/images/spacer.gif" width="1" height="16"/></td>
			</tr>					
			<tr>
				<td width="128" bgcolor="#CCCCCC" colspan="2">
					<img src="/images/spacer.gif" width="1" height="1"/>
				</td>
			</tr>
			<tr>
				<td width="128"></td>
				<td align="left" class="footer">
					&#169; 2003 - www.BenoitTurmel.com
				</td>
			</tr>
		</table>
	</xsl:template>


	<!-- page -->
	<xsl:template match="/page">
		<html>
			<head>
				<title>
					<xsl:value-of select="sponsor/@name"/> 
					<xsl:if test="@title">
						- 
						<xsl:value-of select="@title"/> 
					</xsl:if>
					<xsl:if test="@subtitle"> 
						-
						<xsl:value-of select="@subtitle"/>
					</xsl:if>
				</title>
				<xsl:apply-templates select="css"/>
			</head>
			<body class="body" bgcolor="#ffffff">
					<xsl:apply-templates select="header"/>
					<xsl:apply-templates select="body"/>
					<xsl:apply-templates select="footer"/>
			</body>
		</html>
	</xsl:template>	
	
	<xsl:template match="category">
		<xsl:param name="level"/>
		<xsl:param name="type"/>

		<xsl:choose>
			<xsl:when test="$type = 'form'">
				<xsl:choose>
					<xsl:when test="@id = /page/body/item/category//@id">
						<option value="{@id}" selected="true">
							<xsl:choose>
								<xsl:when test="$level = 1">
									&#160;&#160;&#160;
								</xsl:when>
								<xsl:when test="$level > 1">
									&#160;&#160;&#160;
									&#160;&#160;&#160;
								</xsl:when>				
							</xsl:choose>
							<xsl:value-of select="@name"/>
						</option>
					</xsl:when>
					<xsl:otherwise>
						<option value="{@id}">
							<xsl:choose>
								<xsl:when test="$level = 1">
									&#160;&#160;&#160;
								</xsl:when>
								<xsl:when test="$level > 1">
									&#160;&#160;&#160;
									&#160;&#160;&#160;
								</xsl:when>				
							</xsl:choose>
							<xsl:value-of select="@name"/>
						</option>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="$level = 1">
						&#160;&#160;&#160;	<!-- forced whitespace (#5 at http://java.oreilly.com/news/javaxslt_0801.html) -->
					</xsl:when>
					<xsl:when test="$level = 2">
						&#160;&#160;&#160;
						&#160;&#160;&#160;
					</xsl:when>				
					<xsl:when test="$level > 3">
						&#160;&#160;&#160;
						&#160;&#160;&#160;
						&#160;&#160;&#160;
					</xsl:when>				
				</xsl:choose>
				<a href="/browse/category?id={@id}"><xsl:value-of select="@name"/></a>
				<xsl:choose>
					<xsl:when test="/page/@state = 'subcategory'">
						&#160;&#160;&#160;
					</xsl:when>
					<xsl:otherwise>
						<br/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="category">
			<xsl:with-param name="level" select="$level + 1"/>
			<xsl:with-param name="type" select="$type"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="searchResultNavigator">
		<xsl:param name="urlPrefix"/> <!-- kludge -->
		<table border="0" width="100%" style="border : 0px; border-color : #ffffff; 
			border-style : groove; border-collapse: collapse; border-spacing: 0px;" bgcolor="#ffffff">
			<tr>
				<td>
					<div class="text">
						<center>
							<xsl:if test="@currentPage > 0">
								<tt><a href="{$urlPrefix}?{@queryString}&amp;page={@previousPage}">&lt;&lt;</a>&#160;</tt>
							</xsl:if>
							Page <xsl:value-of select="@currentPage + 1"/> 
							<xsl:if test="@hasMorePages">
								<tt>&#160;<a href="{$urlPrefix}?{@queryString}&amp;page={@nextPage}">&gt;&gt;</a> </tt>
							</xsl:if>
						</center>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template match="items">
		<div class="text">
			<xsl:choose>
				<xsl:when test="/page/body/searchResultNavigator/@totalMatches = 0">
					Aucune annonce trouvée 
				</xsl:when>
				<xsl:when test="/page/body/searchResultNavigator/@totalMatches = 1">
					1 annonce trouvée 
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="/page/body/searchResultNavigator/@totalMatches"/> annonces trouvées 
				</xsl:otherwise>
			</xsl:choose>
			<table border="0" width="100%" style="border : 2px; border-color : #CCCCCC; 
				border-style : groove; border-collapse: collapse; border-spacing: 0px;" bgcolor="#CCCCCC">
				<tr class="tableHeader"> 
					<td width="90" style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Diffuseur 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
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
						Prix 
					</td>
					<td style="border-bottom : 2px; border-bottom-style : groove; 
						border-bottom-collapse: collapse; border-bottom-spacing: 0px;"> 
						Courriel 
					</td>
				</tr>
				<xsl:choose>
					<xsl:when test="item">
						<xsl:for-each select="item">
							<tr bgcolor="#FFFFFF" class="text"> 
								<td valigh="top"><a href="/{sponsor/@username}">
										<img src="/images/icon?id={sponsor/@id}" border="0"/></a></td>
								<td valign="top"><xsl:value-of select="@id"/></td>
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
									<a href="/browse/item?id={@id}">
										<xsl:value-of select="substring(@title, 0, 70)"/>
									</a>
								</td>
								<td valign="top">
									<xsl:choose>
										<xsl:when test="@price > 0">
											<xsl:value-of select="format-number(@price, '#0.00')"/>$
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
							</tr>	
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<tr bgcolor="#FFFFFF"> 
							<td valigh="top">-</td>
							<td valign="top">-</td>
							<td valign="top">-</td>
							<td valign="top">-</td>
							<td valigh="top">-</td>
							<td valign="top">-</td>
							<td valign="top">-</td>
						</tr>	
					</xsl:otherwise>
				</xsl:choose>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="title">
		<xsl:param name="name"/>
		<xsl:param name="text"/>

		<div class="title"><xsl:value-of select="$name"/></div>
		<div class="text"><xsl:value-of select="$text"/></div>
	</xsl:template>


	<xsl:template name="subsection">
		<xsl:param name="title"/>
		<xsl:param name="body"/>
		
		<center>
			<table cellpadding="5" width="100%" style="border : 2px; border-color : #CCCCCC; 
				border-style : groove; border-collapse: collapse; border-spacing: 0px;" bgcolor="#CCCCCC">
				<tr>
					<td class="tableHeader">
						<xsl:value-of select="$title"/>
					</td>
				</tr>
				<tr>
					<td style=" border : 2px; border-color : #CCCCCC; border-style : groove; border-spacing: 0px;" bgcolor="#EEEEee">
						<div class="text">
							<xsl:copy-of select="$body"/>
						</div>
					</td>
				</tr>
			</table>
		</center>
	</xsl:template>

	<xsl:template name="inputsection">
		<xsl:param name="body"/>
		<table cellpadding="5" width="100%" style="border : 0px; border-color : #ffffff; 
			border-style : groove; border-collapse: collapse; border-spacing: 0px;" bgcolor="#ffffff">
			<tr>
				<td>
					<div class="text" align="right">
						<xsl:copy-of select="$body"/>
					</div>
				</td>
			</tr>
		</table>
	</xsl:template>	

</xsl:stylesheet>
