<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="90%" border="0" align="center">
     <tr>
             <td>
    <table width="100%" border="0" cellspacing="0" >
	<tr>
		<td> 
                      <table width="100%" border="0" bgcolor="#ffffff">
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                     <tr>
                                                          <td>
                                                               <img src="/images/logo-emploi.gif"/></td>
                                 
                                                         
                                                   </tr>
                                              </table>

     
                                 </td>
 
                                 <td >                                      
                                       <font size="2" align="justify">   Bienvenue dans notre service d'annonce classée 
                                               Vous pouvez publier vos annonces facilement et gratuitement. Vos annonces seront verifiees et rendues publiques sur Internet 
                                                   ET dans certains établissements du Quebec que les promoteurs se feront un plaisir d’afficher sur des tableaux generalement prevus a cet effet 
                                      </font>
                                 </td>
					
                           </tr>
                    </table>
                    <hr size="+1" width="100%"> </hr> 
               </td>
     </tr>   
          

         <tr>  
                 <td bgcolor="#ffffff" >

                         <table width="90%"  border="0" align="center">
 
                             <tr>
                                  <td>
                      		       <table align="right" bgcolor="#ffffff" >
						<tr>
							<td align="right"   >
								
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
							</td>
						</tr>
					</table>

					<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					<font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                        
                                        
                                           
			<table cellspacing="2">
				<tr> 
                			   <td>
                                               </td>
                                   </tr>
                                     <tr>
                                         <td>
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
			 <tr>
                              <td>

                                <table cellpadding="0" >
                                      <tr>
                                         

                                  		<xsl:if test="string-length(item/user/@name) > 0">
							<td><u> Contact:</u></td>
                                                              <td>  <i> <xsl:value-of select="item/user/@name"/></i></td>
				        	</xsl:if>
                                    </tr>
                                      <tr>
                                    						
                                                    
						<xsl:if test="string-length(item/user/@email) > 0">
							 
								<td>Courriel:</td>
							
									<td><i><a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									</a></i></td>
								
						</xsl:if>
                                   </tr>
                                       <tr> 
                                         
						
						<xsl:if test="string-length(item/user/@telephone) > 0">
							
								<td>Téléphone:</td>
								<td><xsl:value-of select="item/user/@telephone"/></td>
							
						</xsl:if>
                                     </tr>
                                       <tr>     
                                                
                                                     <xsl:if test="item/@href">
							
								<td valign="top">Lien:</td>
								
									<td><i> <a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a></i>
								</td>
							
						</xsl:if>
                                         </tr>

						<tr>
							<td>Lieu de travail:</td>
							<td ><i><xsl:value-of select="item/city/@name"/></i></td>

                                                </tr>
                                         </table>

                                                </td>
						</tr>	

						
					</table>
                                      </td>
			             </tr>
		                </table>
					

					
				</td>
			</tr>
		</table>
             </td>`
         </tr>
     </table>  

  


	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
