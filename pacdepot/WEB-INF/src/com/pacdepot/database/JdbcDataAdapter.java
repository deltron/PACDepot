package com.pacdepot.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.UnavailableException;

import net.charabia.normalizer.lucene.NormalAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import com.pacdepot.domain.Ad;
import com.pacdepot.domain.Answer;
import com.pacdepot.domain.Banner;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.DImage;
import com.pacdepot.domain.Icon;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Question;
import com.pacdepot.domain.RegistrationBitmap;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.BrandException;
import com.pacdepot.exception.CategoryException;
import com.pacdepot.exception.CityException;
import com.pacdepot.exception.ImageException;
import com.pacdepot.exception.ItemException;
import com.pacdepot.exception.SponsorException;
import com.pacdepot.servlet.PacServlet;

/** 
 * DataAdapter implementation using JDBC+Lucene
 * 
 */
public class JdbcDataAdapter extends DataAdapter {
	private String dbURL;

	public JdbcDataAdapter() throws UnavailableException {
		this.dbURL = PacServlet.getDatabaseURL();
		try {
			Class.forName(PacServlet.getJdbcDriverClassName());
		} catch (Exception ex) {
			System.out.println(
				"FATAL	Unable to load JDBC driver: "
					+ PacServlet.getJdbcDriverClassName());
			throw new UnavailableException(ex.getMessage(), 10);
		}
	}

	// -- sponsors --------------------------------------------------------------------------------    
	public void createSponsor(
		String username,
		String name,
		String href,
		boolean pub) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"INSERT INTO Sponsors (id, username, name, href, public) "
						+ " VALUES (?,?,?,?,?)");
			stmt.setString(1, Long.toString(DBUtil.getNextID("Sponsors", con)));
			stmt.setString(2, (String)username);
			stmt.setString(3, (String)name);
			stmt.setString(4, (String)href);
			stmt.setBoolean(5, pub);
			stmt.executeUpdate();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getAllSponsors() {
		Connection con = null;
		Statement stmt = null;
		List allSponsors = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, username, name, href, public, customTemplates "
						+ "  FROM Sponsors "
						+ " ORDER BY username ASC");

			while (rs.next())
				try {
					allSponsors.add(
						new Sponsor(
							rs.getLong("id"),
							rs.getString("username"),
							rs.getString("name"),
							rs.getString("href"),
							rs.getBoolean("public"),
							DBUtil.getLongString(rs, 5),
							rs.getBoolean("customTemplates")));
				} catch (SponsorException e) {
					System.out.println(e);
				}

			return allSponsors.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getPublicSponsors() {
		Connection con = null;
		Statement stmt = null;
		List sponsors = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, username, name, href, public, customTemplates "
						+ "  FROM Sponsors "
						+ " ORDER BY username ASC");

			while (rs.next())
				try {
					if (rs.getBoolean("public")) {
						sponsors.add(
							new Sponsor(
								rs.getLong("id"),
								rs.getString("username"),
								rs.getString("name"),
								rs.getString("href"),
								rs.getBoolean("public"),
								DBUtil.getLongString(rs, 5),
								rs.getBoolean("customTemplates")));
					}
				} catch (SponsorException e) {
					System.out.println(e);
				}

			return sponsors.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteSponsors(Iterator members) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String idstring = new String();
			Sponsor m = (Sponsor) members.next();
			idstring += m.getId();
			idstring += "' ";
			while (members.hasNext()) {
				m = (Sponsor) members.next();
				idstring += " OR id = '";
				idstring += m.getId();
				idstring += "' ";
			}

			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Sponsors WHERE id = '" + idstring);
			stmt.execute();

			stmt =
				con.prepareStatement(
					"DELETE FROM Icons WHERE id = '" + idstring);
			stmt.execute();

			stmt =
				con.prepareStatement(
					"DELETE FROM Banners WHERE id = '" + idstring);
			stmt.execute();

			stmt =
				con.prepareStatement(
					"DELETE FROM Ads WHERE sponsorid = '" + idstring);
			stmt.execute();

		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public long getSponsorId(String username) throws SponsorException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT id FROM Sponsors WHERE username = '"
						+ username
						+ "'");
			if (rs.next())
				return rs.getLong("id");
			else
				throw new SponsorException("No sponsor pathname specified");
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return 1;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void updateHomepage(Sponsor sponsor, String homepage) {
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT id "
						+ "  FROM Sponsors "
						+ " WHERE id = '"
						+ sponsor.getId()
						+ "'");

			if (rs.next())
				pstmt =
					con.prepareStatement(
						"UPDATE Sponsors "
							+ "   SET homepage = ? "
							+ " WHERE id = ?");
			else
				return;

			try {
				DBUtil.setLongString(pstmt, 1, homepage);
			} catch (NullPointerException npe) {
				pstmt.setNull(1, Types.NULL);
			}
			pstmt.setLong(2, sponsor.getId());
			pstmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Sponsor getSponsor(long id) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			rs =
				stmt.executeQuery(
					"SELECT username, name, href, public, homepage, customTemplates "
						+ "  FROM Sponsors "
						+ " WHERE id = "
						+ id);

			if (rs.next())
				try {
					return new Sponsor(
						id,
						rs.getString("username"),
						rs.getString("name"),
						rs.getString("href"),
						rs.getBoolean("public"),
						DBUtil.getLongString(rs, 5),
						rs.getBoolean("customTemplates"));
				} catch (SponsorException e) {
					System.out.println(e);
					return null;
				} else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end of sponsors --------------------------------------------------------------------------------

	// -- ads --------------------------------------------------------------------------------	
	public void createAd(Ad ad) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			pstmt =
				con.prepareStatement(
					"INSERT INTO Ads (sponsorid, id, contentType, object, width, height, href) "
						+ "     VALUES (?,?,?,?,?,?,?)");
			pstmt.setLong(1, ad.getSponsor().getId());
			pstmt.setLong(2, DBUtil.getNextID("Ads", con));
			pstmt.setString(3, ad.getImage().getContentType());
			pstmt.setBytes(4, ad.getImage().getBytes());
			pstmt.setInt(5, ad.getImage().getWidth());
			pstmt.setInt(6, ad.getImage().getHeight());
			pstmt.setString(7, ad.getHref());
			pstmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(pstmt, con);
		}
	}

	public Iterator getAllAds(Sponsor sponsor) {
		Connection con = null;
		Statement stmt = null;
		List allAds = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, sponsorid, object, contentType, width, height, href "
						+ "  FROM Ads "
						+ " WHERE sponsorid = '"
						+ sponsor.getId()
						+ "'");

			while (rs.next())
				allAds.add(
					new Ad(
						rs.getLong("id"),
						sponsor,
						new DImage(
							rs.getBytes("object"),
							rs.getString("contentType"),
							rs.getInt("width"),
							rs.getInt("height")),
						rs.getString("href")));

			return allAds.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} catch (ImageException e) {
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteAds(Iterator ads) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String idstring = new String();
			Ad ad = (Ad) ads.next();
			idstring += ad.getId();
			idstring += "' ";
			while (ads.hasNext()) {
				ad = (Ad) ads.next();
				idstring += " OR id = '";
				idstring += ad.getId();
				idstring += "' ";
			}
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Ads " + "WHERE id = '" + idstring);
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Ad getAd(long id) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {

			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT sponsorid, object, contentType, width, height, href "
						+ "  FROM Ads "
						+ " WHERE id = "
						+ id);

			if (rs.next())
				return new Ad(
					id,
					new Sponsor(rs.getLong("sponsorid")),
					new DImage(
						rs.getBytes("object"),
						rs.getString("contentType"),
						rs.getInt("width"),
						rs.getInt("height")),
					rs.getString("href"));
			else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} catch (SponsorException se) {
			se.printStackTrace();
			return null;
		} catch (ImageException e) {
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end of ads --------------------------------------------------------------------------------

	// -- icons --------------------------------------------------------------------------------	
	public void updateIcon(Icon icon) {
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT id "
						+ "  FROM Icons "
						+ " WHERE id = '"
						+ icon.getId()
						+ "'");

			if (rs.next())
				pstmt =
					con.prepareStatement(
						"UPDATE Icons "
							+ "   SET contentType = ?, object = ?, width = ?, height = ? "
							+ " WHERE id = ?");
			else
				pstmt =
					con.prepareStatement(
						"INSERT INTO Icons (contentType, object, width, height, id) "
							+ "VALUES (?,?,?,?,?)");

			pstmt.setString(1, icon.getImage().getContentType());
			pstmt.setBytes(2, icon.getImage().getBytes());
			pstmt.setInt(3, icon.getImage().getWidth());
			pstmt.setInt(4, icon.getImage().getHeight());
			pstmt.setLong(5, icon.getId());
			pstmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Icon getIcon(long id) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT contentType, object, width, height "
						+ "  FROM Icons "
						+ " WHERE id = '"
						+ id
						+ "'");
			if (rs.next())
				return new Icon(
					id,
					new DImage(
						rs.getBytes("object"),
						rs.getString("contentType"),
						rs.getInt("width"),
						rs.getInt("height")));
			else
				return null;
		} catch (Exception sqe) {
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end icons --------------------------------------------------------------------------------

	// -- banners --------------------------------------------------------------------------------	
	public void updateBanner(Banner banner) {
		Connection con = null;
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT id "
						+ "  FROM Banners "
						+ " WHERE id = '"
						+ banner.getId()
						+ "'");
			if (rs.next())
				pstmt =
					con.prepareStatement(
						"UPDATE Banners "
							+ "   SET contentType=?, object=?, width=?, height=? "
							+ " WHERE id = ?");
			else
				pstmt =
					con.prepareStatement(
						"INSERT INTO Banners (contentType, object, width, height, id) "
							+ "VALUES (?,?,?,?,?)");
			pstmt.setString(1, banner.getImage().getContentType());
			pstmt.setBytes(2, banner.getImage().getBytes());
			pstmt.setInt(3, banner.getImage().getWidth());
			pstmt.setInt(4, banner.getImage().getHeight());
			pstmt.setLong(5, banner.getId());
			pstmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Banner getBanner(long id) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT contentType, object, width, height  FROM Banners WHERE id = "
						+ id);
			if (rs.next()) {
				Banner banner = new Banner();
				banner.setAttribute("id", id);
				banner.setAttribute(
					"image",
					new DImage(
						rs.getBytes("object"),
						rs.getString("contentType"),
						rs.getInt("width"),
						rs.getInt("height")));
				return banner;
			} else
				return null;
		} catch (Exception sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end banners --------------------------------------------------------------------------------

	// -- cities --------------------------------------------------------------------------------
	public void createCity(String name) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id = DBUtil.getNextID("Cities", con);
			stmt =
				con.prepareStatement(
					"INSERT INTO Cities " + "(id, name) " + "VALUES (?,?)");
			stmt.setString(1, Long.toString(id));
			stmt.setString(2, name);
			stmt.executeUpdate();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getAllCities() {
		Connection con = null;
		Statement stmt = null;
		List allCities = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, name "
						+ " FROM Cities "
						+ " ORDER BY name ASC");
			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(2);
				allCities.add(new City(id, name));
			}
			return allCities.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public City getCity(long id) {
		Connection con = null;
		Statement stmt = null;
		List allCities = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs =
				stmt.executeQuery(
					"SELECT name " + " FROM Cities WHERE id = " + id);
			if (rs.next())
				return new City(id, rs.getString("name"));
			return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteCities(Iterator cities) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String idstring = new String();
			City city = (City) cities.next();
			idstring += city.getId();
			idstring += "' ";
			while (cities.hasNext()) {
				city = (City) cities.next();
				idstring += " OR id = '";
				idstring += city.getId();
				idstring += "' ";
			}
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Cities " + " WHERE id = '" + idstring);
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end cities --------------------------------------------------------------------------------

	// -- brands ----------------------------------------------------------------------------------------------------
	public void createBrand(Brand brand) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id = DBUtil.getNextID("Brands", con);
			stmt =
				con.prepareStatement(
					"INSERT INTO Brands "
						+ "(id, name, href, contentType, image, width, height) "
						+ "VALUES (?,?,?,?,?,?,?)");
			stmt.setString(1, Long.toString(id));
			stmt.setString(2, brand.getName());
			stmt.setString(3, brand.getHref());
			stmt.setString(4, brand.getImage().getContentType());
			stmt.setBytes(5, brand.getImage().getBytes());
			stmt.setInt(6, brand.getImage().getWidth());
			stmt.setInt(7, brand.getImage().getHeight());

			stmt.executeUpdate();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getAllBrands() {
		Connection con = null;
		Statement stmt = null;
		List allBrands = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, name, href, contentType, width, height, image "
						+ "FROM Brands "
						+ "ORDER BY name ASC");
			while (rs.next())
				try {
					allBrands.add(
						new Brand(
							rs.getLong("id"),
							rs.getString("name"),
							rs.getString("href"),
							new DImage(
								rs.getBytes("image"),
								rs.getString("contentType"),
								rs.getInt("width"),
								rs.getInt("height"))));
				} catch (ImageException e) {
				}
			return allBrands.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteBrands(Iterator brands) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String idstring = new String();
			Brand brand = (Brand) brands.next();
			idstring += brand.getId();
			idstring += "' ";
			while (brands.hasNext()) {
				brand = (Brand) brands.next();
				idstring += " OR id = '";
				idstring += brand.getId();
				idstring += "' ";
			}

			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Brands " + "WHERE id = '" + idstring);
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Brand getBrand(long id) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			rs =
				stmt.executeQuery(
					"SELECT id, name, href, contentType, width, height, image "
						+ "FROM Brands "
						+ "WHERE id = "
						+ id);

			if (rs.next())
				return new Brand(
					id,
					rs.getString("name"),
					rs.getString("href"),
					new DImage(
						rs.getBytes("image"),
						rs.getString("contentType"),
						rs.getInt("width"),
						rs.getInt("height")));
			else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} catch (ImageException e) {
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end brands -----------------------------------------------------------------------------------------------

	// -- categories -----------------------------------------------------------------------------------------------
	public void createCategory(Category category) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id = DBUtil.getNextID("Categories", con);
			stmt =
				con.prepareStatement(
					"INSERT INTO Categories (id, parentid, name) "
						+ "VALUES (?,?,?)");
			stmt.setString(1, Long.toString(id));
			stmt.setString(2, Long.toString(category.getParentid()));
			stmt.setString(3, category.getName());
			stmt.executeUpdate();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Category getCategory(long id) {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, parentid, name,template "
						+ "FROM Categories WHERE id = "
						+ id);
			if (rs.next())
				return new Category(
					rs.getLong("id"),
					rs.getLong("parentid"),
					rs.getString("name"),
					rs.getString("template"));
			else
				return new Category();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getAllCategories() {
		Connection con = null;
		Statement stmt = null;
		List allCategories = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, parentid, name " + "FROM Categories");
			while (rs.next())
				allCategories.add(
					new Category(
						rs.getLong("id"),
						rs.getLong("parentid"),
						rs.getString("name")));
			return allCategories.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getParentCategories() {
		Connection con = null;
		Statement stmt = null;
		List allCategories = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, parentid, name "
						+ "FROM Categories WHERE parentid = 0 "
						+ "ORDER BY name ASC");

			while (rs.next()) {
				long id = rs.getLong(1);
				long parentid = rs.getLong(2);
				String name = rs.getString(3);
				allCategories.add(new Category(id, 0, name));
			}

			return allCategories.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getChildCategories(long parentid) {
		Connection con = null;
		Statement stmt = null;
		List allCategories = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, parentid, name "
						+ "FROM Categories WHERE parentid = "
						+ parentid
						+ " "
						+ "ORDER BY name ASC");
			while (rs.next()) {
				long id = rs.getLong(1);
				String name = rs.getString(3);
				allCategories.add(new Category(id, parentid, name));
			}
			return allCategories.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteCategories(Iterator categories) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			String idstring = new String();
			Category category = (Category) categories.next();
			idstring += category.getId();
			idstring += "' ";
			while (categories.hasNext()) {
				category = (Category) categories.next();
				idstring += " OR id = '";
				idstring += category.getId();
				idstring += "' ";
			}
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Categories " + "WHERE id = '" + idstring);
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	// -- end categories -------------------------------------------------------------------------------------------

	// -- item -----------------------------------------------------------------------------------------------------
	public long createItem(Item item) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id;
			try {
				id = item.getAttributeAsLong("id");
				stmt =
					con.prepareStatement("DELETE FROM Items WHERE id = " + id);
				stmt.executeUpdate();
				try {
					deleteFromIndex(item);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			} catch (Exception e) {
				id = DBUtil.getNextID("Items", con);
			}
			item.setAttribute("id", id);

				stmt =
					con
						.prepareStatement(
							"INSERT INTO Items "
							+ "(id, type, nature, title, description, "
							+ "name, telephone, email, href, price, "
							+ "negociable, quantity, ageInMonths, lifespan, "
							+ "brandid, userbrand, hasImage, image, width, height, "
							+ "cityid, categoryid, sponsorid, date, contentType, userid) "
							+ "VALUES ( "
							+ "?,?,?,?,?," // 5
		+"?,?,?,?,?," // 10
		+"?,?,?,?,?," // 25
		+"?,?,?,?,?," // 20
		+"?,?,?,?,?" // 25
	+",?)"); // 26

			stmt.setLong(1, id);
			stmt.setString(2, (String) item.getAttribute("type"));
			stmt.setString(3, null); // nature, deprecated
			//stmt.setString(4, getLATIN1(item.getAttribute("title")));

			stmt.setString(4, (String) item.getAttribute("title"));

			try {
				DBUtil.setLongString(
					stmt,
					5,
					(String) item.getAttribute("description"));
			} catch (NullPointerException npe) {
				stmt.setNull(5, Types.NULL);
			}

			stmt.setString(
				6,
			(String)
					((User) item.getAttribute("user")).getAttribute("name"));
			stmt.setString(
				7,
			(String)
					((User) item.getAttribute("user")).getAttribute(
						"telephone"));
			stmt.setString(
				8,
			(String)
					((User) item.getAttribute("user")).getAttribute("email"));

			stmt.setString(9, (String) item.getAttribute("href"));

			try {
				stmt.setString(10, (String) item.getAttribute("price"));
			} catch (Exception e) {
				stmt.setNull(10, Types.NULL);
			}

			try {
				stmt.setInt(12, item.getAttributeAsInt("quantity"));
			} catch (Exception e) {
				stmt.setNull(12, Types.NULL);
			}

			try {
				stmt.setInt(13, item.getAttributeAsInt("ageInMonths"));
			} catch (Exception e) {
				stmt.setNull(13, Types.NULL);
			}

			try {
				stmt.setInt(14, item.getAttributeAsInt("lifespan"));
			} catch (Exception e) {
				stmt.setInt(14, 3);
			}

			try {
				stmt.setLong(15, ((Brand) item.getAttribute("brand")).getId());
			} catch (Exception e) {
				stmt.setNull(15, Types.NULL);
			}

			stmt.setString(16, (String) item.getAttribute("userbrand"));

			stmt.setBoolean(11, false); // negociable, deprecated
			stmt.setBoolean(17, false); // hasImage, deprecated

			try {
				stmt.setBytes(
					18,
					((DImage) item.getAttribute("image")).getBytes());
				stmt.setInt(
					19,
					((DImage) item.getAttribute("image")).getWidth());
				stmt.setInt(
					20,
					((DImage) item.getAttribute("image")).getHeight());
				stmt.setString(
					25,
					((DImage) item.getAttribute("image")).getContentType());
			} catch (Exception e) {
				stmt.setNull(18, Types.NULL);
				stmt.setNull(19, Types.NULL);
				stmt.setNull(20, Types.NULL);
				stmt.setNull(25, Types.NULL);
			}

			stmt.setLong(21, ((City) item.getAttribute("city")).getId());
			stmt.setLong(
				22,
				((Category) item.getAttribute("category")).getId());
			stmt.setLong(23, ((Sponsor) item.getAttribute("sponsor")).getId());
			stmt.setDate(
				24,
				new java.sql.Date((new java.util.Date()).getTime()));

			try {
				stmt.setLong(
					26,
					Long.parseLong((String) item.getAttribute("userid")));
			} catch (Exception e) {
				stmt.setNull(26, Types.NULL);
			}

			stmt.executeUpdate();

			return id;
		} catch (Exception sqe) {
			sqe.printStackTrace();
			throw new Exception();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	private static synchronized void writeIndex(Item item) throws IOException {
		// index into Lucene
		IndexWriter writer =
			new IndexWriter(
				PacServlet.getIndexDir(),
				new NormalAnalyzer(PacServlet.getIndexGrammar()),
				false);
		Document doc = new Document();
		doc.add(
			Field.Keyword("id", ((Long) item.getAttribute("id")).toString()));
		try {
			doc.add(
				Field.Text(
					"description",
					(String) item.getAttribute("description")));
		} catch (Exception e) {
			doc.add(Field.Text("description", new String()));
		}
		doc.add(Field.Text("title", (String) item.getAttribute("title")));
		doc.add(
			Field.Text(
				"content",
				(String) item.getAttribute("title")
					+ " "
					+ (String) item.getAttribute("description")
					+ " "
					+ (Long) item.getAttribute("id")
					+ " "
					+ ((User) item.getAttribute("user")).getAttribute("email")
					+ " "
					+ ((User) item.getAttribute("user")).getAttribute("name")
					+ " "
					+ ((User) item.getAttribute("user")).getAttribute(
						"telephone")));

		writer.addDocument(doc);
		writer.close();
	}

	public void confirmItem(Item item) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			long id = item.getAttributeAsLong("id");
			ResultSet rs =
				stmt.executeQuery(
					"SELECT confirmed FROM Items WHERE id = " + id);

			if (rs.next() && !rs.getBoolean("confirmed")) {
				pstmt =
					con.prepareStatement(
						"UPDATE Items SET confirmed = 1 WHERE id = " + id);
				pstmt.executeUpdate();

				try {
					writeIndex(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}

	}

	public Item getItem(long id) throws ItemException {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

			ResultSet rs =
				stmt.executeQuery(
					"SELECT id, type, nature, title, description, "
						+ "       name, telephone, email, href, price, "
						+ "       negociable, quantity, ageInMonths, lifespan, "
						+ "       brandid, userbrand, hasImage, image, width, height, "
						+ "       cityid, categoryid, sponsorid, date, contentType, userid "
						+ "FROM Items "
						+ "WHERE id = "
						+ id);

			if (rs.next())
				return createItem(rs);
			else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	private Item createItem(ResultSet rs) throws SQLException {
		Item item = new Item();
		item.setAttribute("id", rs.getLong("id"));
		item.setAttribute("title", rs.getString("title"));
		item.setAttribute("type", rs.getString("type"));
		item.setAttribute("nature", rs.getString("nature"));
		item.setAttribute("description", DBUtil.getLongString(rs, 5));
		item.setAttribute("href", rs.getString("href"));
		item.setAttribute(
			"price",
			(new DecimalFormat("#0.00").format(rs.getDouble("price")))
				.toString());
		item.setAttribute("ageInMonths", rs.getInt("ageInMonths"));
		item.setAttribute("lifespan", rs.getInt("lifespan"));
		item.setAttribute(
			"date",
			java.sql.Date.valueOf(rs.getDate("date").toString()));
		item.setAttribute("userid", rs.getString("userid"));

		if (rs.getInt("quantity") > 0)
			item.setAttribute("quantity", rs.getInt("quantity"));

		try {
			item.setAttribute(
				"category",
				new Category(rs.getLong("categoryid")));
		} catch (CategoryException e) {
		}

		User user = new User();
		user.setAttribute("id", rs.getString("userid"));
		user.setAttribute("name", rs.getString("name"));
		user.setAttribute("email", rs.getString("email"));
		user.setAttribute("telephone", rs.getString("telephone"));
		item.setAttribute("user", user);
		item.setAttribute("userid", rs.getString("userid"));

		try {
			item.setAttribute("brand", new Brand(rs.getLong("brandid")));
			item.setAttribute("userbrand", rs.getString("userbrand"));
		} catch (BrandException e) {
			item.setAttribute("userbrand", rs.getString("userbrand"));
		}

		try {
			item.setAttribute(
				"image",
				new DImage(
					rs.getBytes("image"),
					rs.getString("contentType"),
					rs.getInt("width"),
					rs.getInt("height")));
		} catch (ImageException e) {
		}

		try {
			item.setAttribute("city", new City(rs.getLong("cityid")));
		} catch (CityException e) {
		}
		try {
			item.setAttribute("sponsor", new Sponsor(rs.getLong("sponsorid")));
		} catch (SponsorException e) {
		}

		return item;
	}

	private Item createItemQuick(ResultSet rs) throws SQLException {
		Item item = new Item();
		item.setAttribute("id", rs.getLong("id"));
		item.setAttribute("title", rs.getString("title"));
		item.setAttribute("type", rs.getString("type"));
		item.setAttribute("nature", rs.getString("nature"));
		item.setAttribute(
			"price",
			(new DecimalFormat("#0.00").format(rs.getDouble("price")))
				.toString());
		item.setAttribute(
			"date",
			java.sql.Date.valueOf(rs.getDate("date").toString()));
		item.setAttribute("confirmed", rs.getString("confirmed"));

		User user = new User();
		user.setAttribute("name", rs.getString("name"));
		user.setAttribute("email", rs.getString("email"));
		item.setAttribute("user", user);

		try {
			item.setAttribute("sponsor", new Sponsor(rs.getLong("sponsorid")));
		} catch (Exception e) {
			e.printStackTrace();

		}

		return item;
	}

	public Iterator getItems(ItemCriteria criteria) {
		return getItems(criteria, 16);
	}

	public Iterator getItems(ItemCriteria criteria, final int maxHits) {
		Connection con = null;
		Statement stmt = null;
		int floor = criteria.getPage() * maxHits;
		int ceiling = floor + maxHits;
		List allItems = new ArrayList();
		Set hits = new HashSet();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs = null;

			if (!criteria.isEmpty()) {
				rs =
					stmt.executeQuery(
						"SELECT id, type, nature, title, description, "
							+ "       name, telephone, email, href, price, "
							+ "       negociable, quantity, ageInMonths, lifespan, "
							+ "       brandid, userbrand, hasImage, image, width, height, "
							+ "       cityid, categoryid, sponsorid, date, contentType, userid "
							+ "FROM Items WHERE "
							+ criteria.getSQL()
							+ " ORDER BY id DESC ");

				while (rs.next())
					allItems.add(createItem(rs));
			}

			return allItems.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getItemsQuick(ItemCriteria criteria) {
		return getItemsQuick(criteria, 16);
	}

	public Iterator getItemsQuickSearch(ItemCriteria criteria) {
		return getItemsQuickSearch(criteria, 16);
	}

	public Iterator getItemsQuick(ItemCriteria criteria, final int maxHits) {
		Connection con = null;
		Statement stmt = null;
		int floor = criteria.getPage() * maxHits;
		int ceiling = floor + maxHits;
		List allItems = new ArrayList();
		Set hits = new HashSet();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs = null;

			rs =
				stmt.executeQuery(
					"SELECT id, type, nature, title, name, telephone, email, price, sponsorid, date, confirmed FROM Items WHERE "
						+ criteria.getSQL()
						+ " ORDER BY confirmed, id DESC ");

			// WORKAROUND: rs.getFetchSize() always returns 0 so we have to count manually
			int total;
			for (total = 0; rs.next(); total++);
			criteria.setTotalMatches(total);

			int count;
			for (count = floor;
				((count < ceiling) && (count < total));
				count++) {
				rs.absolute(count + 1);
				allItems.add(createItemQuick(rs));
			}

			if (count == total)
				criteria.hasMorePages(false);

			return allItems.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getItemsQuickSearch(
		ItemCriteria criteria,
		final int maxHits) {
		List allItems = new ArrayList();
		int floor = criteria.getPage() * maxHits;
		int ceiling = floor + maxHits;

		if (criteria.getLuceneQueryString() == null
			|| criteria.getLuceneQueryString().length() == 0)
			return getItemsQuick(criteria);
		else if (criteria.isDefault()) {
			try {
				Query query =
					QueryParser.parse(
						criteria.getLuceneQueryString(),
						"content",
						new NormalAnalyzer(PacServlet.getIndexGrammar()));
				Hits hits =
					new IndexSearcher(PacServlet.getIndexDir()).search(query);
				criteria.setTotalMatches(hits.length());
				if (hits.length() > 0) {
					int count;
					for (count = floor;
						(count < ceiling) && (count < hits.length());
						count++)
						allItems.add(
							new Item(
								Long.parseLong(hits.doc(count).get("id"))));
					if (count == hits.length())
						criteria.hasMorePages(false);
				} else {
					criteria.hasMorePages(false);
				}
			} catch (Exception e) {
			}
			return allItems.iterator();
		} else {
			Set indexHits = new HashSet();
			try {
				Query query =
					QueryParser.parse(
						criteria.getLuceneQueryString(),
						"content",
						new NormalAnalyzer(PacServlet.getIndexGrammar()));
				Hits hits =
					new IndexSearcher(PacServlet.getIndexDir()).search(query);
				for (int count = 0;(count < hits.length()); count++)
					indexHits.add(hits.doc(count).get("id"));
			} catch (Exception e) {
			}

			Connection con = null;
			Statement stmt = null;
			try {
				con = DBUtil.getConnection(dbURL);
				stmt = con.createStatement();
				ResultSet resultSet =
					stmt.executeQuery(
						"SELECT id, type, nature, title, name, telephone, email, price, sponsorid, date, confirmed FROM Items WHERE "
							+ criteria.getSQL()
							+ " ORDER BY id DESC ");

				int count = 0;
				while (resultSet.next())
					if (indexHits.contains(resultSet.getString("id"))) {
						count++;
						if ((count > floor) && (count < ceiling))
							allItems.add(createItemQuick(resultSet));
					}
				if (count < ceiling)
					criteria.hasMorePages(false);

				criteria.setTotalMatches(count);
			} catch (SQLException sqe) {
				sqe.printStackTrace();
				return null;
			} finally {
				DBUtil.close(stmt, con);
			}

			return allItems.iterator();
		}
	}

	public void deleteItem(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Items WHERE id = "
						+ item.getAttributeAsLong("id"));
			stmt.execute();

			try {
				deleteFromIndex(item);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	/**@see confirmItem */
	public void retireItem(Item item) {
		Connection con = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			long id = item.getAttributeAsLong("id");
			ResultSet rs =
				stmt.executeQuery(
					"SELECT confirmed FROM Items WHERE id = " + id);

			if (rs.next() && rs.getBoolean("confirmed")) {
				pstmt =
					con.prepareStatement(
						"UPDATE Items SET confirmed = 0 WHERE id = " + id);
				pstmt.executeUpdate();

				try {
					deleteFromIndex(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	private static synchronized void deleteFromIndex(Item item)
		throws IOException {
		IndexReader reader = IndexReader.open(PacServlet.getIndexDir());
		reader.delete(
			new Term("id", ((Long) item.getAttribute("id")).toString()));
		reader.close();
	}

	// -- end item -------------------------------------------------------------------------------------------------

	// -- question --------------------------------------------------------------------------------------------------
	public long createQuestion(Question question) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id;
			try {
				id = question.getAttributeAsLong("id");
				stmt =
					con.prepareStatement(
						"DELETE FROM Questions WHERE id = " + id);
				stmt.executeUpdate();
			} catch (Exception e) {
				id = DBUtil.getNextID("Questions", con);
			}
			question.setAttribute("id", id);

			stmt =
				con.prepareStatement(
					"SELECT MAX(sequence) FROM Questions WHERE itemid = "
						+ question.getAttribute("itemid"));
			ResultSet rs = stmt.executeQuery();
			int sequence = 1;
			if (rs.next())
				sequence = rs.getInt(1) + 1;

				stmt =
					con.prepareStatement("INSERT INTO Questions (" + "id, " //1
		+"itemid, " //2
		+"sequence, " //3
		+"title, " //4
		+"description," //5
		+"href, " //6
		+"type, " //7
		+"minLabel, " //8
		+"maxLabel, " //9
		+"choice1, " //10
		+"choice2, " //11
		+"choice3, " //12
		+"choice4, " //13
		+"choice5, " //14
		+"choice6, " //15
		+"choice7, " //16
		+"choice8, " //17
		+"contentType, " //18
		+"image " //19
	+") VALUES ( "
		+ "?,?,?,?,?,"
		+ "?,?,?,?,?,"
		+ "?,?,?,?,?,"
		+ "?,?,?,?"
		+ ")");

			stmt.setLong(1, id);
			stmt.setString(2, question.getAttribute("itemid").toString());
			stmt.setLong(3, sequence);
			stmt.setString(4, (String)question.getAttribute("title"));

			try {
				DBUtil.setLongString(
					stmt,
					5,
					(String) question.getAttribute("description"));
			} catch (NullPointerException npe) {
				stmt.setNull(5, Types.NULL);
			}

			stmt.setString(6, (String) question.getAttribute("href"));
			stmt.setString(7, (String) question.getAttribute("type"));
			stmt.setString(8, (String) question.getAttribute("minLabel"));
			stmt.setString(9, (String) question.getAttribute("maxLabel"));
			stmt.setString(10, (String)question.getAttribute("choice1"));
			stmt.setString(11, (String)question.getAttribute("choice2"));
			stmt.setString(12, (String)question.getAttribute("choice3"));
			stmt.setString(13, (String)question.getAttribute("choice4"));
			stmt.setString(14, (String)question.getAttribute("choice5"));
			stmt.setString(15, (String)question.getAttribute("choice6"));
			stmt.setString(16, (String)question.getAttribute("choice7"));
			stmt.setString(17, (String)question.getAttribute("choice8"));

			try {
				stmt.setString(
					18,
					((DImage) question.getAttribute("image")).getContentType());
				stmt.setBytes(
					19,
					((DImage) question.getAttribute("image")).getBytes());
			} catch (Exception e) {
				stmt.setNull(18, Types.NULL);
				stmt.setNull(19, Types.NULL);
			}

			stmt.executeUpdate();

			return id;
		} catch (Exception sqe) {
			sqe.printStackTrace();
			return 0;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Question getQuestion(long id) {
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT " + "id, " //1
		+"itemid, " //2
		+"sequence, " //3
		+"title, " //4
		+"description," //5
		+"href, " //6
		+"type, " //7
		+"minLabel, " //8
		+"maxLabel, " //9
		+"choice1, " //10
		+"choice2, " //11
		+"choice3, " //12
		+"choice4, " //13
		+"choice5, " //14
		+"choice6, " //15
		+"choice7, " //16
		+"choice8," //17
		+"contentType," //18
		+"image" //19
	+"  FROM Questions " + "  WHERE id = " + id);

			if (rs.next())
				return createQuestion(rs);
			else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getQuestions(long itemid) {
		Connection con = null;
		Statement stmt = null;
		List questions = new ArrayList();

		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT " + "id, " //1
		+"itemid, " //2
		+"sequence, " //3
		+"title, " //4
		+"description," //5
		+"href, " //6
		+"type, " //7
		+"minLabel, " //8
		+"maxLabel, " //9
		+"choice1, " //10
		+"choice2, " //11
		+"choice3, " //12
		+"choice4, " //13
		+"choice5, " //14
		+"choice6, " //15
		+"choice7, " //16
		+"choice8," //17
		+"contentType," //18
		+"image" //19
	+"  FROM Questions " + " WHERE itemid = " + itemid + " ORDER BY sequence ");

			while (rs.next())
				questions.add(createQuestion(rs));

			return questions.iterator();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	private Question createQuestion(ResultSet rs) throws SQLException {
		Question question = new Question();
		question.setAttribute("id", rs.getLong("id"));
		question.setAttribute("itemid", rs.getLong("itemid"));
		question.setAttribute("sequence", rs.getString("sequence"));
		question.setAttribute("title", rs.getString("title"));
		question.setAttribute("description", DBUtil.getLongString(rs, 5));
		question.setAttribute("href", rs.getString("href"));
		question.setAttribute("type", rs.getString("type"));
		question.setAttribute("minLabel", rs.getString("minLabel"));
		question.setAttribute("maxLabel", rs.getString("maxLabel"));
		question.setAttribute("choice1", rs.getString("choice1"));
		question.setAttribute("choice2", rs.getString("choice2"));
		question.setAttribute("choice3", rs.getString("choice3"));
		question.setAttribute("choice4", rs.getString("choice4"));
		question.setAttribute("choice5", rs.getString("choice5"));
		question.setAttribute("choice6", rs.getString("choice6"));
		question.setAttribute("choice7", rs.getString("choice7"));
		question.setAttribute("choice8", rs.getString("choice8"));
		try {
			question.setAttribute(
				"image",
				new DImage(rs.getBytes("image"), rs.getString("contentType")));
			question.setAttribute("hasImage", "true");
		} catch (Exception ignored) {
		}
		return question;
	}

	public void deleteQuestion(Question question) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"DELETE FROM Questions WHERE id = "
						+ question.getAttributeAsLong("id"));
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- end question --------------------------------------------------------------------------------------------------

	// -- answer --------------------------------------------------------------------------------------------------
	public long createAnswer(Answer answer) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id = DBUtil.getNextID("Answers", con);
			answer.setAttribute("id", id);

				stmt =
					con.prepareStatement("INSERT INTO Answers (" + "id, " //1
		+"questionid, " //2
		+"itemid, " //3
		+"userid, " //4
		+"openanswer, " //5
		+"scalevalue, " //6
		+"multichoice " //7
	+") VALUES ( " + "?,?,?,?,?," + "?,?" + ")");

			stmt.setLong(1, id);
			stmt.setString(2, (String) answer.getAttribute("questionid"));
			stmt.setString(3, (String) answer.getAttribute("itemid"));
			stmt.setString(4, answer.getAttribute("userid").toString());

			try {
				DBUtil.setLongString(
					stmt,
					5,
					(String) answer.getAttribute("openanswer"));
			} catch (NullPointerException npe) {
				stmt.setNull(5, Types.NULL);
			}

			stmt.setString(6, (String) answer.getAttribute("scalevalue"));
			stmt.setString(7, (String) answer.getAttribute("multichoice"));

			stmt.executeUpdate();

			return id;
		} catch (Exception sqe) {
			sqe.printStackTrace();
			return 0;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public Iterator getAnswers(Item item, User user) {
		Connection con = null;
		PreparedStatement stmt = null;
		List answers = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					" SELECT Questions.type, Questions.sequence, Answers.* "
						+ " FROM Questions, Answers "
						+ " WHERE Questions.itemid = Answers.itemid "
						+ " AND Questions.id = Answers.questionid "
						+ " AND Answers.itemid = "
						+ item.getId()
						+ " AND Answers.userid = "
						+ user.getId()
						+ " ORDER BY userid, questionid ");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				answers.add(createAnswer(rs));
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
		return answers.iterator();
	}

	public Iterator getAnswers(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;
		List answers = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					" SELECT * FROM Answers "
						+ " WHERE itemid = "
						+ item.getId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				answers.add(createAnswer(rs));
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
		return answers.iterator();
	}

	public Iterator getAnswersDistinctUsers(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;
		List users = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					" SELECT DISTINCT Answers.userid, Users.* FROM Answers, Users WHERE Answers.userid = Users.id AND Answers.itemid = "
						+ item.getId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				users.add(createUser(rs));
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
		return users.iterator();
	}

	private Answer createAnswer(ResultSet rs) throws SQLException {
		Answer answer = new Answer();
		answer.setAttribute("id", rs.getLong("id"));
		answer.setAttribute("questionid", rs.getLong("questionid"));
		answer.setAttribute("itemid", rs.getLong("itemid"));
		answer.setAttribute("userid", rs.getLong("userid"));
		answer.setAttribute("openanswer", rs.getString("openanswer"));
		answer.setAttribute("scalevalue", rs.getString("scalevalue"));
		answer.setAttribute("multichoice", rs.getString("multichoice"));

		try {
			// From join with Questions
			answer.setAttribute("type", rs.getString("type"));
			answer.setAttribute("sequence", rs.getString("sequence"));
		} catch (Exception e) {
		}

		return answer;
	}

	public void deleteAnswer(Answer answer) {
		execSQL("DELETE FROM Answers WHERE id = " + answer.getId());
	}
	// -- end answer --------------------------------------------------------------------------------------------------

	// -- Registration bitmap --------------------------------------------------------------------------------------------------
	public long createRegistrationBitmap(RegistrationBitmap rb) {
		EntitySQLAdapter adapter = new RegistrationBitmapSQLAdapter();
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			long id;
			try {
				id = rb.getAttributeAsLong("id");
				stmt = con.prepareStatement(adapter.getDeleteSQL(id));
				stmt.executeUpdate();
			} catch (Exception e) {
				id = DBUtil.getNextID(adapter.getTableName(), con);
			}
			rb.setAttribute("id", id);
			stmt = con.prepareStatement(adapter.getInsertSQL());
			stmt.setLong(1, id);
			stmt.setBoolean(2, rb.getAttributeAsBoolean("firstname"));
			stmt.setBoolean(3, rb.getAttributeAsBoolean("lastname"));
			stmt.setBoolean(4, rb.getAttributeAsBoolean("telephone"));
			stmt.setBoolean(5, rb.getAttributeAsBoolean("address"));
			stmt.setBoolean(6, rb.getAttributeAsBoolean("city"));
			stmt.setBoolean(7, rb.getAttributeAsBoolean("postalcode"));
			stmt.setBoolean(8, rb.getAttributeAsBoolean("country"));
			stmt.setBoolean(9, rb.getAttributeAsBoolean("province"));
			stmt.setBoolean(10, rb.getAttributeAsBoolean("email"));
			stmt.setBoolean(11, rb.getAttributeAsBoolean("sex"));

			try {
				stmt.setString(
					13,
					((DImage) rb.getAttribute("image")).getContentType());
				stmt.setBytes(
					12,
					((DImage) rb.getAttribute("image")).getBytes());
			} catch (Exception e) {
				stmt.setNull(12, Types.NULL);
				stmt.setNull(13, Types.NULL);
			}

			try {
				DBUtil.setLongString(
					stmt,
					14,
					(String) rb.getAttribute("description"));
			} catch (NullPointerException npe) {
				stmt.setNull(14, Types.NULL);
			}

			stmt.setString(15, (String) rb.getAttribute("title"));
			stmt.executeUpdate();
			return id;
		} catch (Exception sqe) {
			sqe.printStackTrace();
			return 0;
		} finally {
			DBUtil.close(stmt, con);
		}

	}

	public RegistrationBitmap getRegistrationBitmap(long id) {
		EntitySQLAdapter adapter = new RegistrationBitmapSQLAdapter();
		Connection con = null;
		Statement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(adapter.getSelectSQL(id));
			if (rs.next())
				return createRegistrationBitmap(rs);
			else
				return null;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return null;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	private RegistrationBitmap createRegistrationBitmap(ResultSet rs)
		throws SQLException {
		RegistrationBitmap rb = new RegistrationBitmap();
		rb.setAttribute("id", rs.getLong("id"));
		rb.setAttribute("firstname", rs.getBoolean("firstname"));
		rb.setAttribute("lastname", rs.getBoolean("lastname"));
		rb.setAttribute("telephone", rs.getBoolean("telephone"));
		rb.setAttribute("address", rs.getBoolean("address"));
		rb.setAttribute("city", rs.getBoolean("city"));
		rb.setAttribute("postalcode", rs.getBoolean("postalcode"));
		rb.setAttribute("country", rs.getBoolean("country"));
		rb.setAttribute("province", rs.getBoolean("province"));
		rb.setAttribute("email", rs.getBoolean("email"));
		rb.setAttribute("sex", rs.getBoolean("sex"));
		rb.setAttribute("title", rs.getString("title"));
		rb.setAttribute("description", DBUtil.getLongString(rs, 14));
		try {
			rb.setAttribute(
				"image",
				new DImage(rs.getBytes("image"), rs.getString("contentType")));
			rb.setAttribute("hasImage", "true");
		} catch (Exception ignored) {
		}
		return rb;
	}

	public void deleteRegistrationBitmap(RegistrationBitmap rb) {
		EntitySQLAdapter adapter = new RegistrationBitmapSQLAdapter();
		Connection con = null;
		PreparedStatement stmt = null;
		long id;
		try {
			con = DBUtil.getConnection(dbURL);
			id = rb.getAttributeAsLong("id");
			stmt = con.prepareStatement(adapter.getDeleteSQL(id));
			stmt.execute();
		} catch (NullPointerException ignored) {
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
	// -- End Registration Bitmap -------------------------------------------------------------------------------------------------

	// -- Users ------------------------------------------------------------------------------------------------
	public long createUser(User user) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
				stmt = con.prepareStatement("INSERT INTO Users (id," + //1
		"itemid," + //2
		"username," + //3
		"name," + //4
		"password," + //5
		"groupname," + //6
		"email," + //7
		"telephone," + //8
		"cityid," + //9
		"sponsorid," + //10
		"address," + //11
		"city," + //12
		"postalcode," + //13
		"country," + //14
		"province," + //15
		"sex," + //16
		"firstname," + //17
		"lastname  )" + //18
	"   VALUES " + "(?,?,?,?,?," + "?,?,?,?,?," + "?,?,?,?,?," + "?,?,?)");

			long id = DBUtil.getNextID("Users", con);
			stmt.setLong(1, id);
			try {
				stmt.setLong(
					2,
					((Long) user.getAttribute("itemid")).longValue());
			} catch (Exception e) {
				stmt.setNull(2, Types.NULL);
			}
			stmt.setString(3, (String) user.getAttribute("username"));
			stmt.setString(4, (String) user.getAttribute("name"));
			stmt.setString(5, (String) user.getAttribute("password"));
			stmt.setString(6, (String) user.getAttribute("group"));
			stmt.setString(7, (String) user.getAttribute("email"));
			stmt.setString(8, (String) user.getAttribute("telephone"));
			stmt.setString(9, (String) user.getAttribute("cityid"));
			stmt.setString(10, (String) user.getAttribute("sponsorid"));

			stmt.setString(11, (String) user.getAttribute("address"));
			stmt.setString(12, (String) user.getAttribute("city"));
			stmt.setString(13, (String) user.getAttribute("postalcode"));
			stmt.setString(14, (String) user.getAttribute("country"));
			stmt.setString(15, (String) user.getAttribute("province"));
			stmt.setString(16, (String) user.getAttribute("sex"));
			stmt.setString(17, (String) user.getAttribute("firstname"));
			stmt.setString(18, (String) user.getAttribute("lastname"));

			stmt.execute();
			return id;
		} catch (SQLException sqe) {
			sqe.printStackTrace();
			return 0;
		} finally {
			DBUtil.close(stmt, con);
		}
	}

	public void deleteUser(User user) {
		execSQL(
			"DELETE FROM Users WHERE id = " + user.getAttributeAsLong("id"));
	}

	public User getUser(User user) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"SELECT * FROM Users WHERE username = '"
						+ user.getAttribute("username")
						+ "'");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				user.setAttribute("id", rs.getString("id"));
				user.setAttribute("name", rs.getString("name"));
				user.setAttribute("password", rs.getString("password"));
				user.setAttribute("group", rs.getString("groupname"));
				user.setAttribute("email", rs.getString("email"));
				user.setAttribute("telephone", rs.getString("telephone"));
				user.setAttribute("cityid", rs.getString("cityid"));
				user.setAttribute("sponsorid", rs.getString("sponsorid"));
			}
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
		return user;
	}

	public Iterator getAllUsers() {
		Connection con = null;
		PreparedStatement stmt = null;
		List users = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.prepareStatement("SELECT * FROM Users ORDER BY id ASC");
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				users.add(createUser(rs));
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}

		return users.iterator();
	}

	public Iterator getUsers(Item item) {
		Connection con = null;
		PreparedStatement stmt = null;
		List users = new ArrayList();
		try {
			con = DBUtil.getConnection(dbURL);
			stmt =
				con.prepareStatement(
					"SELECT * FROM Users WHERE itemid = " + item.getId());
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				users.add(createUser(rs));
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
		return users.iterator();
	}

	private User createUser(ResultSet rs) throws SQLException {
		User user = new User();
		user.setAttribute("id", rs.getString("id"));
		user.setAttribute("username", rs.getString("username"));
		user.setAttribute("name", rs.getString("name"));
		user.setAttribute("password", rs.getString("password"));
		user.setAttribute("group", rs.getString("groupname"));
		user.setAttribute("email", rs.getString("email"));
		user.setAttribute("telephone", rs.getString("telephone"));
		user.setAttribute("cityid", rs.getString("cityid"));
		user.setAttribute("sponsorid", rs.getString("sponsorid"));

		user.setAttribute("address", rs.getString("address"));
		user.setAttribute("city", rs.getString("city"));
		user.setAttribute("postalcode", rs.getString("postalcode"));
		user.setAttribute("country", rs.getString("country"));
		user.setAttribute("province", rs.getString("province"));
		user.setAttribute("sex", rs.getString("sex"));
		user.setAttribute("firstname", rs.getString("firstname"));
		user.setAttribute("lastname", rs.getString("lastname"));
		return user;
	}

	// -- end users -------------------------------------------------------------------------------------------------

	private void execSQL(String sql) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = DBUtil.getConnection(dbURL);
			stmt = con.prepareStatement(sql);
			stmt.execute();
		} catch (SQLException sqe) {
			sqe.printStackTrace();
		} finally {
			DBUtil.close(stmt, con);
		}
	}
}