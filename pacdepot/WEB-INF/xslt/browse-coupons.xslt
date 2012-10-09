<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<table width="90%" border="0" align="center">
     <tr>
             <td>
    <table width="100%" border="0" cellspacing="0">
	<tr>
		<td> 
                      <table width="100%" border="0" bgcolor="#ffffff">
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                     <tr>
                                                          <td>
                                                               <img src="/images/logo-coupons.gif"/></td>
                                 
                                                         
                                                   </tr>
                                              </table>

     
                                 </td>
 
                                 <td >                                      
                                       <font size="2">   Bienvenue dans notre service d'annonce classée 
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

                   <table border="0"  width="90%"  align="center" cellspacing="0" >
                         <tr>
                              <td>
                                    <table border="0" align="right" width="50%"  cellspacing="0" >
                                          
                                         <tr>
                                             
                                                        
                                                     
                                        <td width="50%">

                                            


                                                  <table border="0" cellspacing="0"  align="center" width="100%">
                                          
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
    
                                                     <td  valign="top" width="8%"> 
                                                   
 
                                                             <img align="right"   src="/images/ciseaux5.gif"/> </td>
                                                                               
                                                         <td >
   
                         		              <table  bgcolor="#ffffff" border="1"  align="center" width="100%" >
                                                                            

                                                         <xsl:if test="item/@href">
	 					              <tr>
            							<td valign="top" align="center">
								
									<a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a>
								</td>
							</tr>
						</xsl:if>

						<tr>
							<td align="center">


								
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
                                                          
                                                </td>
                                               </tr>
                                                    <tr>
                                                       <td>


                                                      <p align="center">   <a href="http://www.pacdepot.com/">www.pacdepot.com</a></p>
                                                      </td>

                                                    
                                       
							
						</tr>
					</table>
                                    </td>
				        <td valign="top"> <img  src="/images/ciseaux4.gif"/>
                                                

                                                </td>	
                                    </tr>
                                      <tr>
                                      <td valign="top"> 
                                         

                                      <img    src="/images/ciseaux5.gif"/></td>
                                       <td >
                                             </td>

                                        <td>
                                              <img    src="/images/ciseaux4.gif"/></td>



				</tr>
			</table>
                           
                                      

                                        
                                    
                                  </td>
                                 	
				</tr>
			</table>

					<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					
                                           
					<table>
						<tr> 
							<td>
                                                           <font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                                      </td>
                                               </tr>
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
					</xsl:choose></p>
							</td>
						</tr>	
						
                                                 
                                               <tr>
                                                     <td>
                                                          <table  border="0" width="50%">
                                                             <tr>
                                                  
						<xsl:if test="string-length(item/user/@name) > 0">
							<td><u>Contact:</u></td>
                                                 <td><i><xsl:value-of select="item/user/@name"/></i></td>
						</xsl:if>
                                                   
                                                </tr>
						
                                                  <tr>
                                                      
                                                             <xsl:if test="string-length(item/user/@email) > 0">
                                          							
								         <td>Courriel:</td>
								
									<td><i><a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									</a></i>
								</td>
							
						</xsl:if>
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
             </td>
         </tr>
     </table>  

  


	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
