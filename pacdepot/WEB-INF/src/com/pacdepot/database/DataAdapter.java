package com.pacdepot.database;

import java.util.Iterator;

import com.pacdepot.domain.Ad;
import com.pacdepot.domain.Answer;
import com.pacdepot.domain.Banner;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.Icon;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Question;
import com.pacdepot.domain.RegistrationBitmap;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.ItemException;
import com.pacdepot.exception.SponsorException;
import com.pacdepot.servlet.PacServlet;

/**
 * Defines an interface to a data source.
 */
public abstract class DataAdapter {
	private static DataAdapter _instance;

	public static synchronized DataAdapter getInstance() {
		if (_instance == null) {
			String adapterClassName = PacServlet.getAdapterClassName();
			try {
				_instance = (DataAdapter) Class.forName(adapterClassName).newInstance();
			} catch (Exception ex) {
				System.out.println(
					"FATAL	DataAdapter is unable to instanciate classname: " + adapterClassName);
			}
		}
		return _instance;
	}

	public abstract void createSponsor(String username, String name, String href, boolean pub);
	public abstract Iterator getAllSponsors();
	public abstract Iterator getPublicSponsors();
	public abstract void deleteSponsors(Iterator members);
	public abstract long getSponsorId(String username) throws SponsorException;
	public abstract Sponsor getSponsor(long id);

	public abstract void updateHomepage(Sponsor sponsor, String homepage);

	public abstract void updateBanner(Banner banner);
	public abstract Banner getBanner(long id);

	public abstract void updateIcon(Icon icon);
	public abstract Icon getIcon(long id);

	public abstract void deleteAds(Iterator ads);
	public abstract void createAd(Ad ad);
	public abstract Iterator getAllAds(Sponsor sponsor);
	public abstract Ad getAd(long id);

	public abstract void createCity(String name);
	public abstract City getCity(long id);
	public abstract Iterator getAllCities();
	public abstract void deleteCities(Iterator cities);

	public abstract void deleteBrands(Iterator brands);
	public abstract Iterator getAllBrands();
	public abstract void createBrand(Brand brand);
	public abstract Brand getBrand(long id);

	public abstract void createCategory(Category category);
	public abstract Iterator getAllCategories();
	public abstract Category getCategory(long id);
	public abstract Iterator getParentCategories();
	public abstract Iterator getChildCategories(long parentid);
	public abstract void deleteCategories(Iterator categories);

	public abstract long createItem(Item item) throws Exception;
	public abstract Item getItem(long id) throws ItemException;
	public abstract Iterator getItems(ItemCriteria criteria);
	public abstract Iterator getItemsQuick(ItemCriteria criteria);
	public abstract Iterator getItemsQuickSearch(ItemCriteria criteria);
	public abstract void confirmItem(Item item);
	public abstract void deleteItem(Item item);
	public abstract void retireItem(Item item);

	public abstract long createQuestion(Question question);
	public abstract Question getQuestion(long id);
	public abstract Iterator getQuestions(long itemid);
	public abstract void deleteQuestion(Question question);

	public abstract long createAnswer(Answer answer);
	public abstract Iterator getAnswers(Item item);
	public abstract Iterator getAnswers(Item item, User user);
	public abstract Iterator getAnswersDistinctUsers(Item item);
	public abstract void deleteAnswer(Answer answer);
		
	public abstract long createRegistrationBitmap(RegistrationBitmap rb);
	public abstract RegistrationBitmap getRegistrationBitmap(long id);
	public abstract void deleteRegistrationBitmap(RegistrationBitmap rb);
	
	public abstract long createUser(User user);
	public abstract void deleteUser(User user);
	public abstract User getUser(User user);
	public abstract Iterator getAllUsers();
	public abstract Iterator getUsers(Item item);
	
}
