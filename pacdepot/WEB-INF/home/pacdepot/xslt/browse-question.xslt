<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>
	<xsl:template match="page/body">
		<center>
			<form method="post" action="/browse/answer">
				<table width="75%">
					<tr>
						<xsl:if test="/page/question/@hasImage = 'true'">
							<td valign="top">
								<img src="/images/question?id={/page/question/@id}" border="0"/>
							</td>
						</xsl:if>
						<td width="15">
							<img src="/images/spacer.gif" width="15" height="1"/>
						</td>
						<td width="75%" valign="top">
							<table cellpadding="5" width="100%" style="border: 2px solid; border-color: #000000;" bgcolor="#EEEEEE">
								<tr>
									<td>
										<div class="text">
											<b><xsl:value-of select="/page/question/@title"/></b><br/>
											<a href="{/page/question/@href}"><xsl:value-of select="/page/question/@href"/></a><br/>
											<xsl:choose>
												<xsl:when test="/page/question/description = 'null'"/>
												<xsl:when test="/page/question/description">
													<xsl:copy-of select="/page/question/description/" />
												</xsl:when>
											</xsl:choose>
											<table>
												<tr>
													<td valign="top">
														<xsl:if test="/page/flags/error">
															<font color="#FF0000" size="+2"><b>*</b></font>
														</xsl:if>
														<xsl:choose>
															<xsl:when test="/page/question/@type = 'none'"/>
															<xsl:when test="/page/question/@type = 'open'">
																<textarea name="openanswer" />
															</xsl:when>
															<xsl:when test="/page/question/@type = 'scale'"> 
																<xsl:value-of select="/page/question/@minLabel"/>
																<input type="radio" name="scalevalue" value="1"/>
																<input type="radio" name="scalevalue" value="2"/>
																<input type="radio" name="scalevalue" value="3"/>
																<input type="radio" name="scalevalue" value="4"/>
																<input type="radio" name="scalevalue" value="5"/>
																<xsl:value-of select="/page/question/@maxLabel"/>
															</xsl:when>
															<xsl:when test="/page/question/@type = 'multichoice'">
																<select name="multichoice">
																	<xsl:if test="/page/question/@choice1">
																		<option value="{/page/question/@choice1}">
																			<xsl:value-of select="/page/question/@choice1"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice2">
																		<option value="{/page/question/@choice2}">
																			<xsl:value-of select="/page/question/@choice2"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice3">
																		<option value="{/page/question/@choice3}">
																			<xsl:value-of select="/page/question/@choice3"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice4">
																		<option value="{/page/question/@choice4}">
																			<xsl:value-of select="/page/question/@choice4"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice5">
																		<option value="{/page/question/@choice5}">
																			<xsl:value-of select="/page/question/@choice5"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice6">
																		<option value="{/page/question/@choice6}">
																			<xsl:value-of select="/page/question/@choice6"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice7">
																		<option value="{/page/question/@choice7}">
																			<xsl:value-of select="/page/question/@choice7"/>
																		</option>
																	</xsl:if>
																	<xsl:if test="/page/question/@choice8">
																		<option value="{/page/question/@choice8}">
																			<xsl:value-of select="/page/question/@choice8"/>
																		</option>
																	</xsl:if>
																</select>
															</xsl:when>
														</xsl:choose>
													</td>

													<td>
														<xsl:if test="/page/@section = 'browse'">
															<input type="submit" value="Continuer &gt;&gt;"/>
														</xsl:if>
													</td>
												</tr>
											</table>
											<br/><br/>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</form>
		</center>
		
		<xsl:if test="/page/@state = 'preview'">
			<xsl:call-template name="inputsection">
				<xsl:with-param name="body">
					<table>
						<tr>
							<form method="post" action="/{/page/@section}/questionSummary">
								<td>
									<input type="submit" value="Confirmer"/>
								</td>	
							</form>
							<form method="post" action="/{/page/@section}/modifyQuestion">
								<td>
									<input type="submit" value="Modifier"/>
								</td>
							</form>
							<form method="post" action="/{/page/@section}/cancelQuestion">
								<td>
									<input type="submit" value="Détruire"/>
								</td>
							</form>
						</tr>
					</table>
				</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
