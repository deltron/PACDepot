package com.pacdepot.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.charabia.normalizer.lucene.NormalAnalyzer;

import org.apache.lucene.index.IndexWriter;

import com.pacdepot.database.DBUtil;
import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.exception.SponsorException;

/**
 * Main controller servlet
 */
public class PacServlet extends HttpServlet {
	private static String _indexDirStr;
	private static String _baseDirStr;
	private static String _tempDirStr;
	private static String _defaultSponsorUsername;
	private static String _databaseUrlStr;
	private static String _jdbcDriverClassName;
	private static String _adapterClassName;
	private static String _adminHost;
	private static String _indexGrammar;
	private static RenderFactory renderFactory;

	public void init(ServletConfig sc) throws ServletException {
		super.init(sc);

		// load paramaters from web.xml
		try {
			_jdbcDriverClassName = getInitParameter("jdbcDriverClassName");
			_adapterClassName = getInitParameter("adapterClassName");
			_databaseUrlStr = getInitParameter("databaseURL");
			DataAdapter.getInstance();
			System.out.println("CONFIG	jdbcDriverClassName = " + _jdbcDriverClassName);
			System.out.println("CONFIG	adapterClassName = " + _adapterClassName);
			System.out.println("CONFIG	databaseURL = " + _databaseUrlStr);
			System.out.println("STATUS	Database Config OK");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FATAL	Can't connect to database");
			throw new UnavailableException(e.getMessage(), 10);
		}

		try {
			_defaultSponsorUsername = getInitParameter("defaultSponsor");
			System.out.println("CONFIG	Default sponsor = " + _defaultSponsorUsername);
		} catch (Exception e) {
			System.out.println("FATAL	No default sponsor");
			throw new UnavailableException(e.getMessage(), 10);
		}

		try {
			_baseDirStr = getInitParameter("baseDir");
			System.out.println("CONFIG	Base directory = " + _baseDirStr);
		} catch (Exception e) {
			System.out.println("FATAL	No base directory");
			throw new UnavailableException(e.getMessage(), 10);
		}

		try {
			_indexDirStr = getInitParameter("indexDir");
			System.out.println("CONFIG	Index directory = " + _indexDirStr);
		} catch (Exception e) {
			System.out.println("FATAL	No index directory");
			throw new UnavailableException(e.getMessage(), 10);
		}

		try {
			_tempDirStr = getInitParameter("tempDir");
			System.out.println("CONFIG	Temp directory = " + _tempDirStr);
		} catch (Exception e) {
			System.out.println("FATAL	No temp directory");
			throw new UnavailableException(e.getMessage(), 10);
		}

		/* ***********************************************
		 * How to disable remote access to the /admin page 
		 * ***********************************************/
		//		try {
		//			_adminHost = getInitParameter("adminHost");
		//			System.out.println("CONFIG	Host allowed for /admin = " + _adminHost);
		//		} catch (Exception e) {
		//			System.out.println("FATAL	/admin is public");
		//			throw new UnavailableException(e.getMessage(), 10);
		//		}

		try {
			_indexGrammar = getInitParameter("indexGrammar");
			System.out.println("CONFIG	indexGrammar = " + _indexGrammar);
		} catch (Exception e) {
			System.out.println("FATAL	No index grammar file");
			throw new UnavailableException(e.getMessage(), 10);
		}

		try {
			renderFactory = new RenderFactory(new HomeRenderer());
			renderFactory.addRenderer(new AdminRenderer());
			renderFactory.addRenderer(new ImagesRenderer());
			renderFactory.addRenderer(new HomeRenderer());
			renderFactory.addRenderer(new AddRenderer());
			renderFactory.addRenderer(new BrowseRenderer());
			renderFactory.addRenderer(new MembersRenderer());
			renderFactory.addRenderer(new SearchRenderer());
			System.out.println("STATUS	Request handlers registered OK");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("FATAL	Unable to register request handlers");
			log(ex.getMessage(), ex);
			throw new UnavailableException(ex.getMessage(), 10);
		}

		//createIndex();
	}

	private void createIndex() {
		// <warning>
		// <text> 
		// Run this only once EVER per installation! 
		// Very important! If you run it again it will erase your index! 
		// (OK, OK, it's repairable but *extremely* time-consuming)
		// </text>
		// <code>
						try {
							IndexWriter writer =
								new IndexWriter(PacServlet.getIndexDir(), new NormalAnalyzer(getIndexGrammar()), true);
							System.out.println("WARNING	Initialized (deleted) index");
						} catch (IOException e) {}
		// </code>
		// </warning>
	}

	public void destroy() {
		super.destroy();
		DBUtil.closeAllConnections();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		Renderer r = renderFactory.build(this, request, response);
		r.render();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException, ServletException {
		Renderer r = renderFactory.build(this, request, response);
		r.render();
	}

	public static String getAdapterClassName() {
		return _adapterClassName;
	}

	public static String getBaseDir() {
		return _baseDirStr;
	}

	public static String getDatabaseURL() {
		return _databaseUrlStr;
	}

	public static String getIndexDir() {
		return _indexDirStr;
	}

	public static Sponsor getDefaultSponsor() throws ServletException {
		try {
			return new Sponsor(_defaultSponsorUsername);
		} catch (SponsorException se) {
			throw new ServletException(se);
		}
	}

	public static String getJdbcDriverClassName() {
		return _jdbcDriverClassName;
	}

	public static String getTempDir() {
		return _tempDirStr;
	}

	public static String getAdminHost() {
		return _adminHost;
	}

	public static File getIndexGrammar() {
		return new File(_indexGrammar);
	}
}