<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:import href="include/page.xslt"/>
    <xsl:output method="html"/>
   

   <xsl:template match="page/body">
       

  <table align="center" border="0" width="90%"  cellspacing="0">
            
<tr>
		<td> 
                      <table width="100%" border="0" bgcolor="#ffffff">
                            <tr>
                                 <td width="10%">       
                                              <table border="0" >
                                                     <tr>
                                                          <td>
                                                               <img src="/images/logo-pac.gif"/></td>
                                 
                                                         
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
                    <hr size="+1" width="100%"> </hr> 
               </td>
     </tr>   

 <tr>
		<td >
              <table width="90%" align="center" border="0">
                      <tr>
                      <td>
                    <p> </p>
			<font size="+2" color="red" ><b><xsl:value-of select="item/@title"/></b></font>
                     </td>
                    </tr>
                   
                            
                    <tr>
                   <td valign="top">

                                     <xsl:choose>
                                        <xsl:when test="item/@hasImage ='true'">

                                   <table  border="0"  align="right">
				<tr>
					<td >
                                     
                                             

                                      	<img src="/images/item?id={item/@id}" border="0"/>
                                   </td>
                               </tr>
                                  
                                          
                                 <tr>
                                       <td >
                                           <table >
                                              <tr>
                                                  <td>
                                                <i> Publié par:</i></td> <td><i><xsl:value-of select="item/user/@name"/></i>
                                      </td>
                                  </tr>
                                   <tr>
                                            
                                           <td><i> Courriel:</i></td>
                                            <td><i> <a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
						             <xsl:value-of select="item/user/@email"/>
                                                                   </a></i>                                                   

                                                  </td>
				           </tr>
			             </table> 
                                     </td>
				 </tr>
			      </table> 

                             <table border="0" cellspacing="5">
                                <tr>
                                  <td valign="top">         
                       
                                      <xsl:choose>
				              <xsl:when test="item/description = 'null'"/>
				                  <xsl:when test="item/description">
                                                <p align="justify" valign="top"> 
						<xsl:copy-of select="item/description/" /></p>
				       </xsl:when>
			           </xsl:choose>
                                 </td>
                               </tr>
                            </table>
                         
                 </xsl:when>

                <xsl:otherwise>
                          <table border="0">
                                 <tr>
                                        <td valign="top"> 
                                            <xsl:choose>
				                   <xsl:when test="item/description = 'null'"/>
				                      <xsl:when test="item/description">
                                                      <p align="justify" valign="top"> 
					
					              <xsl:copy-of select="item/description/" /></p>
                                                 </xsl:when>
			               </xsl:choose>

                                  </td>
                            </tr>
                          <tr>
                                 <td>

                                       <i> Publié par:</i></td> <td><i><xsl:value-of select="item/user/@name"/></i>
                                </td>
                         </tr>
                      <tr>
                            
                                      
                                          <td> <i>Courriel:</i></td> <td> <i> <a href="mailto:{item/user/@email}?subject={item/@title} (#{item/@id})">
						             <xsl:value-of select="item/user/@email"/>
                                                                   </a></i>                                                   
		           </td>
                 </tr>
             </table>
				
                                            

        </xsl:otherwise>
                                             


			</xsl:choose>
                             
                         <p> </p>
                        <xsl:apply-templates select="item/category"/> Annonce #<xsl:value-of select="item/@id"/><br/><br/>
		     
              		   		
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
