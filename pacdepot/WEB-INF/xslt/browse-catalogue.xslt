<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		
<table width="90%" border="0" align="center">
         <tr> 

		
		   <td> 
                      <table width="90%" border="0" bgcolor="#ffffff" >
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                   <tr>
                                                       <td>
                                                           <img src="/images/logo-catalogue.gif"/></td>
                                                   </tr>
                                              </table>
                                 </td>
                                 <td >                                      
                                       <font size="2">   Bienvenue dans notre service d'annonce classée 
                                               Vous pouvez publier vos annonces facilement et gratuitement. Vos annonces seront verifiees et rendues publiques sur Internet 
                                                   ET dans certains établissements du Quebec que les promoteurs se feront un plaisir d’afficher sur des tableaux generalement prevus a cet effet 
                                      </font>
                                 </td>
           
                                 <td width="8%">               

                                       <a onClick="window.open('/html/color-reference.html','', '');" href="#"><h1 align="center">  ?</h1></a>
                                          
                                 </td>
					
                           </tr>
                      </table>
                      <hr  size="+1" width="100%"> </hr> 
               	   </td>
     		</tr> 

                
                  <tr>
                      <td>

                         <table width="90%" >
                           <tr>
                              <td>  
                                    <table align="right" width="70%"  >
                                             <tr>
                                                 <td >
					
				        	         <font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                           
                                                     
                                              <p align="justify">
                                              <xsl:choose>
						      <xsl:when test="item/description = 'null'"/>
						      <xsl:when test="item/description">
							
							<xsl:copy-of select="item/description/" />
						     </xsl:when>
					   </xsl:choose></p> 
                                               <table width="50%">
                                                  <tr>
                                                         
					    
                                        
					                <xsl:if test="item/@price > 0">
							 
								 <td>Prix detail susséré:</td>
								
								    <td> <xsl:value-of select="format-number(item/@price, '#0.00')"/>$
									<xsl:if test="item/@negociable = 'true'">
										(négociable)
									</xsl:if></td>
                                       
					              </xsl:if>
                                                </tr>
                                                 <tr>
                                                
						           <xsl:if test="item/@href">
							
								<td>Lien:</td>
								
									<td><a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a></td>
								
						</xsl:if>
                                             </tr>
                                          </table>
					  </td>
					</tr>
				</table>




         		       <table  bgcolor="#ffffff"  >
                                       <tr>
                                           <td align="center">
                                                 <xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/></td>
                                      </tr>
					<tr>
						<td >
                                             </td>
                                          </tr>

                                            <tr>
						<td >
                                               </td>
                                          </tr>
                                          <tr>
						<td >
                                               </td>
                                          </tr>

                                              <tr>
                                                 <td>
								
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
							</td>
						</tr>
			</table>
                                
					

			
		 </td>
                           <td valign="top">
                                                           <center>
									<xsl:if test="item/brand/@id > 0">
										<a href="#" onClick="window.open('{item/brand/@href}','', '');">
											<img   backgroundcolor="#ffCC99"  src="/images/brand?id={item/brand/@id}" border="0"/>
										</a>
										
									</xsl:if>
							</center></td>
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

