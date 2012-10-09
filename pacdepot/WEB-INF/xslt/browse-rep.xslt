<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		
<table width="90%" border="0" align="center">
         <tr> 

		
		   <td> 
                      <table width="100%" border="0" bgcolor="#ffffff">
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                   <tr>
                                                       <td>
                                                           <img src="/images/logo-rep.gif"/></td>
                                                   </tr>
                                              </table>
                                 </td>
                                 <td >                                      
                                       <font size="2" align="justify">   Bienvenue dans notre service d'annonce classée 
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

                         <table width="90%" align="center">
                           <tr>
                              <td>  
                                    <table align="right" width="70%"  >
                                             <tr>
                                                 <td >
					
				        	         <font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                         
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
					    <table border="0">
                                            <tr>
                                               
                                               
                                                      <xsl:if test="string-length(item/user/@name) > 0">
						   <td> <u>Contact:</u></td><td><I><xsl:value-of select="item/user/@name"/></I></td>
						          </xsl:if>
                                                 
                                          </tr>
                                   
                                             <tr>      
						  <xsl:if test="string-length(item/user/@email) > 0">
							
								<td>Courriel:</td>
								
									<td><I><a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									</a></I></td>
								
							
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
							
								<td>Lien:</td>
								
									<td><i><a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a></i></td>
								
						</xsl:if>

                                          </tr>
                                      </table>

						  </td>
						</tr>
					</table>




         		       <table  bgcolor="#ffffff"  >
                                    <tr>
                                       <td>
                   
					
                                      

                                      <table cellpacing="0" border="0">


                                          <tr>
                                                 <td align="center"><font size="+2" style="bold" > <xsl:value-of select="item/city/@name"/></font></td>
                                        </tr>
                                           <tr>
                                                <td align="center">

						      								
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
                                                    </td>
                                              </tr>
                                                <tr>
                                                    <td align="center">      
                                                         <xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
                                         


                                                     

							</td>

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

  


	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>

