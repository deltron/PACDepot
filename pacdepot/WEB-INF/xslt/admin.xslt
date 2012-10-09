<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="include/page.xslt"/>
	<xsl:output method="html"/>

	<xsl:template match="page/body">
		<xsl:call-template name="subsection">
			<xsl:with-param name="title">Maintenance</xsl:with-param>
			<xsl:with-param name="body">
					<ul>
						<li><a href="/admin/flushXSLTCache">Flush XSLT Cache</a></li>
					</ul>
			 </xsl:with-param>
		</xsl:call-template>
				
		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Ajouter un usager
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/createUser">
					<b>Nom d'usager</b><br/>
					<input type="text" size="16" name="username"/>
					<br/><br/>
							
					<b>Nom complet</b><br/>
					<input type="text" size="80" name="name"/>
					<br/><br/>
					
					<b>Mot de passe</b><br/>
					<input type="password" size="16" name="password"/>
					<br/><br/>
					
					<b>Groupe</b><br/>
					<select name="group">
						<option value="none">aucun</option>
						<option value="registered">enregistré</option>
						<option value="administrator">admin</option>
					</select>
					<br/><br/>
					
					<b>Téléphone</b><br/>
					<input type="text" size="80" name="telephone"/>
					<br/><br/>
					
					<b>Courriel</b><br/>
					<input type="text" size="80" name="email"/>
					<br/><br/>
					
					<b>Endroit</b><br/>
					<select name="cityid">
						<xsl:for-each select="content/city">
							<option value="{@id}"><xsl:value-of select="@name"/></option>
						</xsl:for-each>
					</select>
					<br/><br/>
					
					<b>Promotteur</b><br/>
					<select name="sponsorid">
						<xsl:for-each select="content/sponsor">
							<option value="{@id}"><xsl:value-of select="@name"/></option>
						</xsl:for-each>
					</select>
					<br/><br/>
					
					<input type="submit" value="Ajouter"/>
				</form>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Effacer des usagers
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/deleteUsers">
					<table border="1">
						<tr>
							<td><b>id</b></td>
							<td><b>username</b></td>
							<td><b>password</b></td>
							<td><b>group</b></td>
							<td><b>email</b></td>
							<td><b>telephone</b></td>
							<td><b>cityid</b></td>
							<td><b>sponsorid</b></td>
							<td><i>delete</i></td>
						</tr>
						<xsl:for-each select="content/user">
							<tr>
								<td><xsl:value-of select="@id"/></td>
								<td><xsl:value-of select="@username"/></td>
								<td><xsl:value-of select="@password"/></td>
								<td><xsl:value-of select="@group"/></td>
								<td><xsl:value-of select="@email"/></td>
								<td><xsl:value-of select="@telephone"/></td>
								<td><xsl:value-of select="@cityid"/></td>
								<td><xsl:value-of select="@sponsorid"/></td>
								<td><input type="checkbox" name="delete" value="{@id}"/></td>
							</tr>
						</xsl:for-each>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><input type="submit" value="Delete"/></td>
						</tr>
					</table>
				</form>
			</xsl:with-param>
		</xsl:call-template>
		

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Ajouter un promotteur
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/createSponsor">
					<b>Nom d'usager</b><br/>
					Example: couchetard<br/>
					<input type="text" size="16" name="username"/>
					<br/><br/>

					<b>Nom descriptif</b><br/>
					Exemple: Couche-Tard<br/>
					<input type="text" size="80" name="name"/>
					<br/><br/>
					
					<b>URL</b><br/>
					Exemple: http://www.couche-tard.com/<br/>
					<input type="text" size="80" name="href"/>
					<br/><br/>
					
					<b>Public</b><br/>
					<input type="radio" name="public" value="true" checked="true"/>Oui 
					<input type="radio" name="public" value="false"/>Non
					<br/>
					<input type="submit" value="Ajouter"/>
				</form>
			</xsl:with-param>
		</xsl:call-template>



		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Effacer des Promotteurs
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/deleteSponsors">
					<table width="100%" border="1">
						<tr>
							<td><b>id</b></td>
							<td><b>username</b></td>
							<td><b>name</b></td>
							<td><b>href</b></td>
							<td><b>public</b></td>
							<td><i>delete</i></td>
						</tr>
						<xsl:for-each select="content/sponsor">
							<tr>
								<td><xsl:value-of select="@id"/></td>
								<td><xsl:value-of select="@username"/></td>
								<td><xsl:value-of select="@name"/></td>
								<td><a href="{@href}"><xsl:value-of select="@href"/></a></td>
								<td><xsl:value-of select="@public"/></td>
								<td><input type="checkbox" name="delete" value="{@id}"/></td>
							</tr>
						</xsl:for-each>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><input type="submit" value="Delete"/></td>
						</tr>
					</table>
				</form>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Ajouter une ville
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/createCity">
					<b>Nom de la ville</b><br/>
					<input type="text" size="16" name="name"/>
					<br/><br/>

					<input type="submit" value="Ajouter"/>
				</form>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Effacer des villes
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/deleteCities">
					<table border="1">
						<tr>
							<td><b>id</b></td>
							<td><b>name</b></td>
							<td><i>delete</i></td>
						</tr>
						<xsl:for-each select="content/city">
							<tr>
								<td><xsl:value-of select="@id"/></td>
								<td><xsl:value-of select="@name"/></td>
								<td><input type="checkbox" name="delete" value="{@id}"/></td>
							</tr>
						</xsl:for-each>
						<tr>
							<td></td>
							<td></td>
							<td><input type="submit" value="Delete"/></td>
						</tr>
					</table>
				</form>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Ajouter une marque de commerce
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" enctype="multipart/form-data" action="/admin/createBrand">
					<b>Nom de la marque</b><br/>
					<input type="text" size="16" name="name"/>
					<br/><br/>

					<b>URL</b><br/>
					<input type="text" size="16" name="href"/>
					<br/><br/>

					<b>Logo</b><br/>
					<input type="file" name="brand"/>
					<br/><br/>
					
					<input type="submit" value="Ajouter"/>
				</form>
			</xsl:with-param>
		</xsl:call-template>

		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Effacer des marques de commerce
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/deleteBrands">
					<table width="100%" border="1">
						<tr>
							<td><b>id</b></td>
							<td><b>name</b></td>
							<td><b>href</b></td>
							<td><b>image</b></td>
							<td><i>delete</i></td>
						</tr>
						<xsl:for-each select="content/brand">
							<tr>
								<td><xsl:value-of select="@id"/></td>
								<td><xsl:value-of select="@name"/></td>
								<td><a href="{@href}"><xsl:value-of select="@href"/></a></td>
								<td><img src="{@src}" height="{@height}" width="{@width}"/></td>
								<td><input type="checkbox" name="delete" value="{@id}"/></td>
							</tr>
						</xsl:for-each>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td><input type="submit" value="Delete"/></td>
						</tr>
					</table>
				</form>
			</xsl:with-param>
		</xsl:call-template>



		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Ajouter une catégorie
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/createCategory">
					<b>Nom</b><br/>
					<input type="text" size="16" name="name"/>
					<br/><br/>
					
					<b>Parent</b><br/>
					<select name="parentid" size="5" class="body">
						<option value="0" selected="true">(aucun)</option>
						<xsl:apply-templates select="content/category">
							<xsl:with-param name="level" select="0"/>
						</xsl:apply-templates>
					</select>
					<br/><br/>

					<input type="submit" value="Ajouter"/>
				</form>
			</xsl:with-param>
		</xsl:call-template>
		
		<xsl:call-template name="subsection">
			<xsl:with-param name="title">
				Effacer des catégories
			</xsl:with-param>
			<xsl:with-param name="body">
				<form method="post" action="/admin/deleteCategories">
					<table border="1">
						<tr>
							<td><b>id</b></td>
							<td><b>parentid</b></td>
							<td><b>name</b></td>
							<td><i>delete</i></td>
						</tr>
						<xsl:for-each select="content//category">
							<tr>
								<td><xsl:value-of select="@id"/></td>
								<td><xsl:value-of select="@parentid"/></td>
								<td><xsl:value-of select="@name"/></td>
								<td><input type="checkbox" name="delete" value="{@id}"/></td>
							</tr>
						</xsl:for-each>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td><input type="submit" value="Delete"/></td>
						</tr>
					</table>
				</form>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>


	<xsl:template match="category">
		<xsl:param name="level"/>
		<option value="{@id}">
			<xsl:choose>
				<xsl:when test="$level = 1">
					&#160;&#160;&#160;
				</xsl:when>
				<xsl:when test="$level = 2">
					&#160;&#160;&#160;
					&#160;&#160;&#160;
				</xsl:when>				
				<xsl:when test="$level > 2">
					&#160;&#160;&#160;
					&#160;&#160;&#160;
					&#160;&#160;&#160;
				</xsl:when>				
			</xsl:choose>
			<xsl:value-of select="@name"/>
		</option>
		
		<xsl:apply-templates select="category">
			<xsl:with-param name="level" select="$level + 1"/>
		</xsl:apply-templates>
	</xsl:template>
	
</xsl:stylesheet>
