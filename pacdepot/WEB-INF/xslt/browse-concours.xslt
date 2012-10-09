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
                                              <table border="0">
                                                     <tr>
                                                          <td>
                                                               <img src="/images/logo-concours.gif"/></td>
                                 
                                                         
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

                       <table width="90%" align="center" >
                  
                             <tr>
                                   <td>    

                             		<table align="right" bgcolor="#ffffff"  >
						<tr>
							<td align="center">

                                                          <h2><font color="red"> !!!Participez et Gagnez!!!</font></h2>
								
								<xsl:if test="item/@hasImage = 'true'">
									<img  backgroundcolor="#ffCC99" src="/images/item?id={item/@id}" border="0"/>
								</xsl:if>
                                                             
                                                     <xsl:if test="item/@href">
							<tr>
								<td valign="top" align="center">Lien:
								
									<a href="{item/@href}">
										<xsl:value-of select="substring(item/@href, 0, 48)"/>
										<br/>
										<xsl:value-of select="substring(item/@href, 48, 64)"/>
									</a>
								</td>
							</tr>
						</xsl:if>

							</td>
						</tr>
					</table>

				                     	<xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
					                <font size="+2"><b><xsl:value-of select="item/@title"/></b></font>
                                         
                                         <br/>
                                <table>
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
							
						
                                               <br/>
                                           <br/>
                                    </td>
                                 </tr>              
                                                 
                                               <tr>
                                                  <td>
                                                     <table border="0" cellspacing="0">
                                                         <tr>
                                                             <td>
                                                                 <table  border="0" cellspacing="2" >
                                                                   <tr>
                                                                       <td align="center" >
                                                                        <font size="+1"> <u>Bon de Participation</u></font>
                                                                    </td>
                                                                </tr>
                                                     <tr>
                                                            <td>  

                                                              
						
							<tr><td>Nom:---------------------------------------</td></tr>
						
                                                   </td>
                                                </tr>
						
   
						
							<tr> 
								<td>Courriel:------------------------------------
								
									
								</td>
							</tr>
						
						
						
							<tr> 
								<td>Téléphone:----------------------------------
								</td>
							</tr>
						                                              
                                                   

						<tr>
							<td>Reponse:------------------------------------</td>
							
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

  </td>`
 </tr>
</table>  


	</xsl:template>

	<xsl:template match="category">
		<a href="/{/page/body/item/sponsor/@username}/browse/category?id={@id}"><xsl:value-of select="@name"/></a> :
		<xsl:apply-templates select="category"/>
	</xsl:template>
</xsl:stylesheet>
