<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="100%" border="0" align="center">
     <tr>
             <td>
    <table width="100%" border="0" cellspacing="0">
	<tr>
		<td> 
                      
                    <hr size="+1" width="100%"> </hr> 
               </td>
     </tr>   
          

         <tr>  
                 <td bgcolor="#ffffff" >

                        <table align="center" width="100%">
                            <tr>
                                  <td>                        

                         		<table align="right" bgcolor="#ffffff">
						<tr>
							<td align="right">
								<center>
									<xsl:if test="item/brand/@id > 0">
										<a href="#" onClick="window.open('{item/brand/@href}','', '');">
											<img   backgroundcolor="#ffCC99"  src="/images/brand?id={item/brand/@id}" border="0"/>
										</a>
										
									</xsl:if>
								</center>
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
							</td>
						</tr>
					</table>

					<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					<font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                         <br/>
                                        <br/>
					<table>
						<tr> 
							<td>Type:</td>
							<td>
								<xsl:choose>
									<xsl:when test="item/@type = 'sale'"> 
										À vendre
									</xsl:when>
									<xsl:when test="item/@type = 'rental'">
										À louer
									</xsl:when>
									<xsl:when test="item/@type = 'free'">
										À donner
									</xsl:when>
									<xsl:when test="item/@type = 'search'">
										Recherche
									</xsl:when>
									<xsl:when test="item/@type = 'publish'">
										Publier
									</xsl:when>
									<xsl:when test="item/@type = 'offer'">
										Offre
									</xsl:when>
									<xsl:otherwise>
										-
									</xsl:otherwise>
								</xsl:choose>
							</td>
						</tr>	
						
						<xsl:if test="item/@price > 0">
							<tr> 
								<td>Prix:</td>
								<td>
									<xsl:value-of select="format-number(item/@price, '#0.00')"/>$
									<xsl:if test="item/@negociable = 'true'">
										(négociable)
									</xsl:if>
								</td>
							</tr>
						</xsl:if>

						<xsl:choose>
							<xsl:when test="item/age/@years > 0">
								<tr>
									<td>Possède depuis:</td>
									<td>
										<xsl:value-of select="item/age/@years"/>
										<xsl:choose>
											<xsl:when test="item/age/@years = 1">
												an</xsl:when>
											<xsl:otherwise>
												ans</xsl:otherwise>
										</xsl:choose>
										<xsl:if test="item/age/@months > 0">,
											<xsl:value-of select="item/age/@months"/> mois
										</xsl:if>
									</td>
								</tr>
							</xsl:when>
							<xsl:when test="item/age/@months > 0">
								<tr>
									<td>Possède depuis:</td>
									<td>
										<xsl:value-of select="item/age/@months"/> mois
									</td>
								</tr>
							</xsl:when>
						</xsl:choose>
						
						<xsl:if test="item/@quantity">
							<tr><td>Quantité:</td><td><xsl:value-of select="item/@quantity"/></td></tr>	
						</xsl:if>
						
						<xsl:if test="item/@href">
							<tr>
								<td valign="top">Lien:</td>
								<td>
									<a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a>
								</td>
							</tr>
						</xsl:if>
                                                 
                                               <tr>
                                                  <td>
                                                      </td>
                                                    </tr>
                                              <tr>
                                                  <td>
                                                      </td>
                                                    </tr>
            
                                              <tr>
                                                 <td>  
                                                    
						<xsl:if test="string-length(item/user/@name) > 0">
							<tr><td>Publié par:</td><td><i><xsl:value-of select="item/user/@name"/></i></td></tr>
						</xsl:if>
                                                   </td>
                                                </tr>
						
                                                    
						<xsl:if test="string-length(item/user/@email) > 0">
							<tr> 
								<td>Courriel:</td>
								<td>
									<i><a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									</a></i>
								</td>
							</tr>
						</xsl:if>
						
						<xsl:if test="string-length(item/user/@telephone) > 0">
							<tr> 
								<td>Téléphone:</td>
								<td><xsl:value-of select="item/user/@telephone"/></td>
							</tr>
						</xsl:if>

						<tr>
							<td>Ville:</td>
							<td><xsl:value-of select="item/city/@name"/></td>
						</tr>	

						<tr> 
							<td height="5"><img src="/images/spacer.gif" alt="" width="110" height="1"/></td>
							<td></td>
						</tr>
					</table>
                                     <p align="justify">
					<xsl:choose>
						<xsl:when test="item/description = 'null'"/>
						<xsl:when test="item/description">
							
							<xsl:copy-of select="item/description/" />
						</xsl:when>
					</xsl:choose>
                                    </p>  
					
				</td>
			</tr>
		</table>

             </td>
         </tr>
     </table>  
      </td>
  </tr>
</table>
  


	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
