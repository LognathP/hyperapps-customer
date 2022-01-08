package com.hyperapps.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.hyperapps.model.Categories;
import com.hyperapps.model.CategoryTree;
import com.hyperapps.model.Child_category;
import com.hyperapps.model.Customer;
import com.hyperapps.model.CustomerAddress;
import com.hyperapps.model.OfferHistoryData;
import com.hyperapps.model.Product;
import com.hyperapps.model.PromotionData;
import com.hyperapps.model.SliderImagesData;
import com.hyperapps.model.Store;
import com.hyperapps.model.UserDeviceToken;
import com.hyperapps.model.UserProfile;
import com.hyperapps.request.AddAddressRequest;

@Component
public interface CustomerDao {
	
	public Customer checkCustomer(Customer customer);

	public int addCustomerAddress(AddAddressRequest req);

	public boolean updateCustomerAddress(AddAddressRequest addAddrReq, int address_id);

	public boolean deleteCustomerAddress(int address_id);

	public List<CustomerAddress> getCustomerAddress(int customer_id);

	public boolean isCustomerAvailable(int customerId);

	public boolean isAddressAvailable(int addressId);

	public List<Categories> getCategories(int storeId);

	public List<Product> getProductsList(int storeId, int catId);

	public UserProfile getCustomerProfile(int customer_id);

	boolean updateCustomerProfile(UserProfile custProf);

	public Store getStoreDeliverAreas(int storeId, String store_latitude, String store_longitude);

	public List<CategoryTree> getInventoryList(int store_id);

	public List<SliderImagesData> getSliderImages(int store_id);

	public List<PromotionData> getPromotions(int store_id);

	public List<Product> searchProduct(String storied, String searchstr);

	public List<Child_category> getCategoryDetails(int store_id, int paranetCatgoryId, int subCategoryId);

	public List<OfferHistoryData> getOnGoingOfferDetails(int orderId, int customerId);

	public boolean addfeedback(int user_id, String details);

	public boolean checkDeviceToken(UserDeviceToken ut);

	public void addDeviceToken(UserDeviceToken ut);
	
	public String getDeviceToken(String id);

	public String getMailId(String id);

	public ArrayList<String> getBusinessDeviceToken(String custId);

	public Store getStoreDetails(int storeId);

	void updateDeviceToken(UserDeviceToken ut);

	public String getCustomerNameById(int custId);

	public int getStoreDeliveryRadius(int storeId);

	public Customer addCustomer(Customer customer);

	}
