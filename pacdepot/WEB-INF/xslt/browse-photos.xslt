<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="include/page.xslt"/>
    <xsl:output method="html"/>
    <xsl:template match="page/body">
       


   <table align="center" border="0" width="100%" height="100%" cellspacing="0">
           
       
	<tr>
		<td> 
                      <table width="100%" border="0" bgcolor="#ffffff">
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                     <tr>
                                                          <td>
                                                               <img src="/images/logo-photos.gif"/></td>
                                 
                                                         
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
                    <hr size="+1" width="100%"> </hr> 
               </td>
           </tr>   
          

              <tr>
                 <td >
                
                
                <img   src= "/images/entete-scp-photo.gif"/> </td>
           </tr>
              
          <tr>
                <td  >

                  
            </td>
          </tr>
         <tr>
              <td>


                   <table border="0"  cellspacing="0" width="100%" height="100%" > 
                    
	          
                    <tr>

                        <td>
                         
                                  <table border="5" align="center"  cellspacing="5" width="80%" height="90%">
                                         <tr>
                                             <td border="3"  >                  

                                                 <table border="0" align="center" bgcolor="#d2853f"  cellspacing="10" height="100%">
                                                     
                           
                                                          <tr>
                                                                 <td bgcolor="#F7F3F7" width="90%" align="center" cellpadding="0" >

                                                                       <table  align="left" border="0"  cellspacing="2" height="90%"  >
                         
                        
                                                                               <tr>
                                                                                       <td align="center" valign="top">
				                                                               <font size="+2"><b><xsl:value-of select="item/@title"/></b></font>				
			                                                              </td>
                                                                              </tr>

                                                                                 <tr>
                                                                                        <td cellspacing="0" valign="top" >

                                                                                           <xsl:choose>
			                                                                          <xsl:when test="item/description = 'null'"/>
				                                                                      <xsl:when test="item/description">
                                
                                                                                                           <font size="3">
                                                                                                                <p align="justify">
                                       
					                                                                               <xsl:copy-of select="item/description/" />
                                                                                                               </p>
                                                                                                       </font>
			                                                            	         </xsl:when>
                               

			                                                               </xsl:choose>
                                                                                 </td>
                                                                           </tr>
                                                                        <tr>
                                                                               <td>
                                                                                   <font size="2" >

                                                                                           <xsl:if test="string-length(item/user/@name) > 0">
                                                                                              <strong>    Publié par: <xsl:value-of select="item/user/@name"/></strong>

                                                                                            <xsl:if test="string-length(item/user/@email) > 0"> <br> </br>
                                                                                                     Courriel: <a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
										<xsl:value-of select="item/user/@email"/>
									                                       </a>
							
							
						                                      </xsl:if>
					                                	</xsl:if>
                                                                           </font>
                                                                  </td>
                                                             </tr>
                                                         <tr>
                                                              <td>
                                            
                                                 
                                                              </td>
                                                       </tr>
                                                  </table> 
                            
                                        

                                      </td>

                                <td >
                                               <table height="50%" border="0">
                                                          <tr>
                                                              <td width="100%">
		  	                 
								
						                      <xsl:if test="item/@hasImage = 'true'">
							                  <img   src="/images/item?id={item/@id}" border="0"/>
						                    </xsl:if>
                                                              <p align="right"> 
                                                                    <xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
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
